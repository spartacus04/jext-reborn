import JSZip from 'jszip';
import Ajv from 'ajv';
import type { Disc, songData } from './config';
import { discStore } from './store';

export const isMinecraftRP = async (blob: Blob) : Promise<boolean> => {
	const zip = await JSZip.loadAsync(blob);

	return zip.file('pack.mcmeta') !== null;
};

export const isDiscsJson = async (blob: Blob) : Promise<boolean> => {
	const text = await blob.text();

	const schema = {
		type: 'array',
		items: {
			type: 'object',
			properties: {
				title: {
					type: 'string',
				},
				author: {
					type: 'string',
				},
				'disc-namespace': {
					type: 'string',
				},
				'model-data': {
					type: 'integer',
				},
				'creeper-drop': {
					type: 'boolean',
				},
				lores: {
					type: 'array',
					items: {
						type: 'string',
					},
				},
				'loot-tables': {
					type: 'array',
					items: {
						type: 'string',
					},
				},
			},
			required: [
				'author',
				'creeper-drop',
				'disc-namespace',
				'lores',
				'model-data',
				'title',
			],
		},
	};

	const validate = new Ajv().compile(schema);

	return validate(JSON.parse(text));
};

export const importResourcePack = async (discs: Blob, RP: Blob) : Promise<void> => {
	const discsText = await discs.text();

	const discsJson : Disc[] = JSON.parse(discsText);

	const zip = await new JSZip().loadAsync(RP);

	const importedDiscs : songData[] = await Promise.all(discsJson.map(async disc => {
		const namespace = disc['disc-namespace'].substring(disc['disc-namespace'].indexOf('.') + 1);
		const musicBlob = await zip.file(`assets/minecraft/sounds/records/${namespace}.ogg`).async('blob');
		const textureBlob = await zip.file(`assets/minecraft/textures/item/music_disc_${namespace}.png`).async('blob');

		return {
			name: disc.title,
			author: disc.author,
			namespace: disc['disc-namespace'],
			creeperDrop: disc['creeper-drop'],
			isMono: true,
			lores: disc.lores.join('\n'),
			lootTables: disc['loot-tables'] ?? [],
			uploadedFile: new File([musicBlob], `music_disc.${namespace}.ogg`),
			oggFile: musicBlob,
			monoFile: musicBlob,
			texture: textureBlob,
		};
	}));

	discStore.set(importedDiscs);
};