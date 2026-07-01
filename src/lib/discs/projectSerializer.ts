import { get } from 'svelte/store';
import { ResourcePackData } from './resourcePackManager';
import { discsStore } from './discManager';
import { MusicDisc } from './musicDisc';
import { NbsDisc } from './nbsDisc';
import type { BaseDisc } from './baseDisc';
import { arrayBufferToBase64, blobToArraBuffer, base64ToArrayBuffer } from '$lib/utils';

const blobToBase64 = async (blob: Blob): Promise<string> => {
	if (!blob || blob.size === 0) return '';
	const arrayBuffer = await blobToArraBuffer(blob);
	return arrayBufferToBase64(arrayBuffer);
};

const base64ToBlob = (base64: string, type: string): Blob => {
	if (!base64) return new Blob();
	const buffer = base64ToArrayBuffer(base64);
	return new Blob([buffer], { type });
};

export const exportProjectAsJson = async (): Promise<Blob> => {
	const discs = get(discsStore);
	const packData = get(ResourcePackData);

	// 1. Serialize ResourcePackData
	const serializedPackData = {
		name: packData.name,
		description: packData.description,
		version: packData.version,
		icon: {
			type: packData.icon.type,
			data: await blobToBase64(packData.icon)
		},
		packs: await Promise.all(
			packData.packs.map(async (p) => ({
				name: p.name,
				icon: {
					type: p.icon.type,
					data: await blobToBase64(p.icon)
				},
				contents: {
					type: p.contents.type,
					data: await blobToBase64(p.contents)
				}
			}))
		)
	};

	// 2. Serialize Discs
	const serializedDiscs = await Promise.all(
		discs.map(async (disc) => {
			const base = {
				title: disc.title,
				author: disc.author,
				namespace: disc.namespace,
				tooltip: disc.tooltip,
				modelData: disc.modelData,
				creeperDroppable: disc.creeperDroppable,
				discLootTables: disc.discLootTables,
				fragmentLootTables: disc.fragmentLootTables,
				colorOuter: disc.colorOuter,
				colorInner: disc.colorInner,
				colorFill: disc.colorFill,
				customTemplate: disc.customTemplate,
				templateInputs: disc.templateInputs,
				discTexture: {
					type: disc.discTexture.type,
					data: await blobToBase64(disc.discTexture)
				},
				fragmentTexture: {
					type: disc.fragmentTexture.type,
					data: await blobToBase64(disc.fragmentTexture)
				}
			};

			if (disc instanceof MusicDisc) {
				return {
					...base,
					discType: 'music',
					audioFile: {
						name: (disc.audioFile as File).name || 'audio',
						type: disc.audioFile.type,
						data: await blobToBase64(disc.audioFile)
					},
					monoChannel: disc.monoChannel,
					normalizeVolume: disc.normalizeVolume,
					disableTranscoding: disc.disableTranscoding,
					qualityPreset: disc.qualityPreset
				};
			} else if (disc instanceof NbsDisc) {
				return {
					...base,
					discType: 'nbs',
					nbsFile: {
						name: (disc.nbsFile as File).name || 'music.nbs',
						type: disc.nbsFile.type,
						data: await blobToBase64(disc.nbsFile)
					}
				};
			}

			return null;
		})
	);

	const projectPayload = {
		jextProjectVersion: 1,
		resourcePackData: serializedPackData,
		discs: serializedDiscs.filter((d) => d !== null)
	};

	const jsonString = JSON.stringify(projectPayload, null, 2);
	return new Blob([jsonString], { type: 'application/json' });
};

export const importProjectFromJson = async (projectPayload: any): Promise<void> => {
	if (!projectPayload || projectPayload.jextProjectVersion !== 1) {
		throw new Error('Unsupported project file format or version');
	}

	const rp = projectPayload.resourcePackData;
	const iconBlob = base64ToBlob(rp.icon.data, rp.icon.type);

	const packs = await Promise.all(
		rp.packs.map(async (p: any) => {
			const icon = base64ToBlob(p.icon.data, p.icon.type);
			const contentsBlob = base64ToBlob(p.contents.data, p.contents.type);
			const contentsFile = new File([contentsBlob], p.name, { type: p.contents.type });
			return {
				name: p.name,
				icon,
				contents: contentsFile
			};
		})
	);

	ResourcePackData.set({
		icon: iconBlob,
		name: rp.name,
		description: rp.description,
		version: rp.version,
		packs
	});

	const restoredDiscs: BaseDisc[] = [];

	for (const d of projectPayload.discs) {
		const discTextureBlob = base64ToBlob(d.discTexture.data, d.discTexture.type);
		const fragmentTextureBlob = base64ToBlob(d.fragmentTexture.data, d.fragmentTexture.type);

		let disc: BaseDisc;

		if (d.discType === 'music') {
			const audioBlob = base64ToBlob(d.audioFile.data, d.audioFile.type);
			const audioFile = new File([audioBlob], d.audioFile.name, { type: d.audioFile.type });

			const musicDisc = new MusicDisc(audioFile, false);
			musicDisc.monoChannel = d.monoChannel;
			musicDisc.normalizeVolume = d.normalizeVolume;
			musicDisc.disableTranscoding = d.disableTranscoding;
			musicDisc.qualityPreset = d.qualityPreset;
			disc = musicDisc;
		} else if (d.discType === 'nbs') {
			const nbsBlob = base64ToBlob(d.nbsFile.data, d.nbsFile.type);
			const nbsFile = new File([nbsBlob], d.nbsFile.name, { type: d.nbsFile.type });
			const arrayBuffer = await nbsFile.arrayBuffer();

			disc = new NbsDisc(nbsFile, arrayBuffer, false);
		} else {
			continue;
		}

		disc.title = d.title;
		disc.author = d.author;
		disc.namespace = d.namespace;
		disc.tooltip = d.tooltip;
		disc.modelData = d.modelData;
		disc.creeperDroppable = d.creeperDroppable;
		disc.discLootTables = d.discLootTables;
		disc.fragmentLootTables = d.fragmentLootTables;
		disc.colorOuter = d.colorOuter || '';
		disc.colorInner = d.colorInner || '';
		disc.colorFill = d.colorFill || '';
		disc.customTemplate = d.customTemplate || null;
		disc.templateInputs = d.templateInputs || {};

		disc.setDiscTexture(discTextureBlob);
		disc.setFragmentTexture(fragmentTextureBlob);

		restoredDiscs.push(disc);
	}

	discsStore.set(restoredDiscs);
};
