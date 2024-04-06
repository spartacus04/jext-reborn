import type { FFmpeg } from '@ffmpeg/ffmpeg';
import { get } from 'svelte/store';
import JSZip from 'jszip';

import {
	discsStore,
	resourcePackStore,
	getDuration,
	processImage,
	loadFFmpeg,
	prepareAudio,
	getVersionFromTime
} from '../';

export const processResources = async (
	ffmpeg: FFmpeg | null,
	onError: (err: string) => unknown,
	onProgress: (
		total: number | undefined,
		done: number | undefined,
		status: string | undefined,
		index: number
	) => unknown
) => {
	const discs = get(discsStore);

	let current = 0;

	const total = discs.filter((disc) => disc.uploadData).length;

	for (const disc of discs) {
		if (disc.uploadData) {
			onProgress(total, current, `Processing ${disc['disc-namespace']}`, 1);
			onProgress(undefined, undefined, undefined, 2);
			const usedModelDatas = discs.map((disc) => disc['model-data']).filter((model) => model != -1);

			// first available counting from 1
			let modelData = 1;
			while (usedModelDatas.includes(modelData)) {
				modelData++;
			}

			const disc_texture = await processImage(disc.uploadData.uploadedTexture);
			const fragment_texture = await processImage(disc.uploadData.uploadedFragmentTexture);

			const track = await prepareAudio(
				disc.uploadData.uploadedTrack,
				{
					mono: disc.uploadData.mono,
					normalize: disc.uploadData.normalize,
					quality: disc.uploadData.quality
				},
				onProgress,
				ffmpeg
			);

			if (!track) onError(`Failed to process track for ${disc['disc-namespace']}`);
			else {
				disc.duration = await getDuration(track);
				disc['model-data'] = modelData;

				disc.packData = {
					fragmentTexture: fragment_texture,
					texture: disc_texture,
					track: track
				};

				disc['disc-namespace'] = `${disc.title}${disc.author}${disc['model-data']}`
					.replace(/[^a-zA-Z0-9]/g, '')
					.replaceAll('1', 'one')
					.replaceAll('2', 'two')
					.replaceAll('3', 'three')
					.replaceAll('4', 'four')
					.replaceAll('5', 'five')
					.replaceAll('6', 'six')
					.replaceAll('7', 'seven')
					.replaceAll('8', 'eight')
					.replaceAll('9', 'nine')
					.replaceAll('0', 'zero')
					.toLowerCase();

				disc.uploadData = null;
			}

			current++;
		}
	}

	discsStore.set(discs);
};

export const generateResourcePack = async (): Promise<Blob> => {
	const rp = new JSZip();

	const discs = get(discsStore);
	const resourcePackData = get(resourcePackStore);

	// pack.mcmeta
	rp.file(
		'pack.mcmeta',
		JSON.stringify(
			{
				pack: {
					pack_format: resourcePackData.version,
					description: resourcePackData.description
				}
			},
			null,
			2
		)
	);

	// pack.png
	rp.file('pack.png', resourcePackData.icon);

	// sounds.json
	const soundsjson: {
		[key: string]: {
			sounds: {
				name: string;
				stream: boolean;
			}[];
		};
	} = {};

	discs.forEach((disc) => {
		soundsjson[`${disc['disc-namespace']}`] = {
			sounds: [
				{
					name: `records/${disc['disc-namespace']}`,
					stream: true
				}
			]
		};
	});

	rp.file('assets/minecraft/sounds.json', JSON.stringify(soundsjson, null, 2));

	// music_disc_11.json

	rp.file(
		'assets/minecraft/models/item/music_disc_11.json',
		JSON.stringify(
			{
				parent: 'item/generated',
				textures: {
					layer0: 'item/music_disc_11'
				},
				overrides: discs.map((disc) => {
					return {
						predicate: {
							custom_model_data: disc['model-data']
						},
						model: `item/music_disc_${disc['disc-namespace']}`
					};
				})
			},
			null,
			2
		)
	);

	// disc_fragment_5.json

	rp.file(
		'assets/minecraft/models/item/disc_fragment_5.json',
		JSON.stringify(
			{
				parent: 'item/generated',
				textures: {
					layer0: 'item/disc_fragment_5'
				},
				overrides: discs.map((disc) => {
					return {
						predicate: {
							custom_model_data: disc['model-data']
						},
						model: `item/fragment_${disc['disc-namespace']}`
					};
				})
			},
			null,
			2
		)
	);

	// each disc's model, texture, and track

	for (const disc of discs) {
		// disc model
		rp.file(
			`assets/minecraft/models/item/music_disc_${disc['disc-namespace']}.json`,
			JSON.stringify(
				{
					parent: 'item/generated',
					textures: {
						layer0: `item/music_disc_${disc['disc-namespace']}`
					}
				},
				null,
				2
			)
		);

		// fragment model
		rp.file(
			`assets/minecraft/models/item/fragment_${disc['disc-namespace']}.json`,
			JSON.stringify(
				{
					parent: 'item/generated',
					textures: {
						layer0: `item/fragment_${disc['disc-namespace']}`
					}
				},
				null,
				2
			)
		);

		// disc texture
		rp.file(
			`assets/minecraft/textures/item/music_disc_${disc['disc-namespace']}.png`,
			disc.packData!.texture
		);

		// fragment texture
		rp.file(
			`assets/minecraft/textures/item/fragment_${disc['disc-namespace']}.png`,
			disc.packData!.fragmentTexture
		);

		// track
		rp.file(`assets/minecraft/sounds/records/${disc['disc-namespace']}.ogg`, disc.packData!.track);
	}

	rp.file(
		'jext.json',
		JSON.stringify(
			discs.map((disc) => {
				// eslint-disable-next-line @typescript-eslint/no-unused-vars
				const { packData, uploadData, ...rest } = disc;
				return rest;
			}),
			null,
			2
		)
	);

	return await rp.generateAsync({ type: 'blob' });
};

export const generateGeyserResourcePack = async (): Promise<Blob> => {
	const rp = new JSZip();

	const discs = get(discsStore);
	const resourcePackData = get(resourcePackStore);

	// manifest.json

	rp.file(
		'manifest.json',
		JSON.stringify(
			{
				format_version: 2,
				header: {
					description: resourcePackData.description,
					min_engine_version: [1, 14, 0],
					name: 'JEXT Custom Discs',
					uuid: 'e60d0bb3-6d2a-4a15-b393-74666ce2e15f',
					version: getVersionFromTime()
				},
				modules: [
					{
						description: resourcePackData.description,
						type: 'resources',
						uuid: 'd0b32a92-e81d-4b19-ab72-97d421ca0be8',
						version: getVersionFromTime()
					}
				]
			},
			null,
			2
		)
	);

	// pack_icon.png

	rp.file('pack_icon.png', resourcePackData.icon);

	// item_texture.json

	const itemTextures: {
		[key: string]: {
			textures: string[];
		};
	} = {};

	for (const disc of discs) {
		itemTextures[`music_disc_${disc['disc-namespace']}`] = {
			textures: [`textures/items/music_disc_${disc['disc-namespace']}`]
		};

		itemTextures[`fragment_${disc['disc-namespace']}`] = {
			textures: [`textures/items/fragment_${disc['disc-namespace']}`]
		};
	}

	rp.file(
		'textures/item_texture.json',
		JSON.stringify(
			{
				resource_pack_name: 'JEXT Custom Discs',
				texture_name: 'atlas.items',
				texture_data: itemTextures
			},
			null,
			2
		)
	);

	// sound_definitions.json

	const soundDefinitions: {
		[key: string]: {
			__use_legacy_max_distance: true;
			category: 'record';
			max_distance: 64.0;
			sounds: {
				name: string;
				load_on_low_memory: true;
				stream: boolean;
				volume: 0.5;
			}[];
		};
	} = {};

	for (const disc of discs) {
		soundDefinitions[disc['disc-namespace']] = {
			__use_legacy_max_distance: true,
			category: 'record',
			max_distance: 64.0,
			sounds: [
				{
					name: `sounds/jext/${disc['disc-namespace']}`,
					load_on_low_memory: true,
					stream: true,
					volume: 0.5
				}
			]
		};
	}

	rp.file(
		'sounds/sound_definitions.json',
		JSON.stringify(
			{
				format_version: '1.14.0',
				sound_definitions: soundDefinitions
			},
			null,
			2
		)
	);

	// each disc's textures and sound
	for (const disc of discs) {
		rp.file(`textures/items/music_disc_${disc['disc-namespace']}.png`, disc.packData!.texture);
		rp.file(
			`textures/items/fragment_${disc['disc-namespace']}.png`,
			disc.packData!.fragmentTexture
		);
		rp.file(`sounds/jext/${disc['disc-namespace']}.ogg`, disc.packData!.track);
	}

	return await rp.generateAsync({ type: 'blob' });
};

export const mergeResourcePacks = async (base: Blob): Promise<Blob> => {
	const rpData = get(resourcePackStore);

	const packs = [base, ...rpData.packs.map((pack) => pack.value)];

	const rp = new JSZip();

	const mergedJsonFiles: { [key: string]: any } = {};

	for (const pack of packs.toReversed()) {
		const packZip = await JSZip.loadAsync(pack);

		for (const path in packZip.files) {
			const file = packZip.files[path];

			if (!file.dir) {
				if (file.name.endsWith('.json') || file.name.endsWith('.mcmeta')) {
					if (!mergedJsonFiles[file.name])
						mergedJsonFiles[file.name] = JSON.parse(await file.async('text'));
					else
						mergedJsonFiles[file.name] = Object.mergeDeep(
							mergedJsonFiles[file.name],
							JSON.parse(await file.async('text'))
						);
				}

				rp.file(path, file.async('blob'));
			}
		}
	}

	for (const file in mergedJsonFiles) {
		rp.file(file, JSON.stringify(mergedJsonFiles[file], null, 2));
	}

	return await rp.generateAsync({ type: 'blob' });
};

export const outputEverything = async (
	onJavaRp: (rp: Blob) => unknown,
	onProgress: (
		total: number | undefined,
		done: number | undefined,
		status: string | undefined,
		index: number
	) => unknown,
	onBedrockRp: (rp: Blob) => unknown
) => {
	const ffmpeg = await loadFFmpeg(onProgress);

	onProgress(3, 1, 'Constructing discs', 0);

	await processResources(
		ffmpeg,
		(err) => {
			alert(err);
		},
		onProgress
	);

	onProgress(undefined, undefined, undefined, 1);

	onProgress(3, 2, 'Building ResourcePack', 0);

	const generatedRp = await generateResourcePack();
	const mergedRp = await mergeResourcePacks(generatedRp);
	onProgress(3, 3, 'Done!', 0);

	onJavaRp(mergedRp);
	onBedrockRp(await generateGeyserResourcePack());
};
