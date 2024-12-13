import JSZip from 'jszip';
import Ajv from 'ajv';
import type { BaseDisc } from '$lib/discs/baseDisc';
import { MusicDisc } from '$lib/discs/musicDisc';
import { NbsDisc } from '$lib/discs/nbsDisc';
import { get } from 'svelte/store';
import { ResourcePackData } from '$lib/discs/resourcePackManager';

const newBaseSchema = {
	type: 'array',
	items: {
		type: 'object',
		properties: {
			title: {
				type: 'string'
			},
			author: {
				type: 'string'
			},
			duration: {
				type: 'integer'
			},
			'disc-namespace': {
				type: 'string'
			},
			'model-data': {
				type: 'integer'
			},
			'creeper-drop': {
				type: 'boolean'
			},
			lores: {
				type: 'array',
				items: {
					type: 'string'
				}
			},
			'loot-tables': {
				type: 'object',
				additionalProperties: {
					type: 'integer'
				}
			},
			'fragment-loot-tables': {
				type: 'object',
				additionalProperties: {
					type: 'integer'
				}
			}
		},
		required: ['title', 'author', 'disc-namespace', 'model-data', 'creeper-drop', 'lores']
	}
};

const oldBaseSchema = {
	type: 'array',
	items: {
		type: 'object',
		properties: {
			title: {
				type: 'string'
			},
			author: {
				type: 'string'
			},
			duration: {
				type: 'integer'
			},
			'disc-namespace': {
				type: 'string'
			},
			'model-data': {
				type: 'integer'
			},
			'creeper-drop': {
				type: 'boolean'
			},
			lores: {
				type: 'array',
				items: {
					type: 'string'
				}
			},
			'loot-tables': {
				type: 'array',
				items: {
					type: 'string'
				}
			},
			'fragment-loot-tables': {
				type: 'array',
				items: {
					type: 'string'
				}
			}
		},
		required: ['title', 'author', 'disc-namespace', 'model-data', 'creeper-drop', 'lores']
	}
};

const nbsSchema = {
	type: 'array',
	items: {
		type: 'object',
		properties: {
			title: {
				type: 'string'
			},
			author: {
				type: 'string'
			},
			'disc-namespace': {
				type: 'string'
			},
			'model-data': {
				type: 'integer'
			},
			'creeper-drop': {
				type: 'boolean'
			},
			lores: {
				type: 'array',
				items: {
					type: 'string'
				}
			},
			'loot-tables': {
				type: 'object',
				additionalProperties: {
					type: 'integer'
				}
			},
			'fragment-loot-tables': {
				type: 'object',
				additionalProperties: {
					type: 'integer'
				}
			}
		},
		required: ['title', 'author', 'disc-namespace', 'model-data', 'creeper-drop', 'lores']
	}
};

export const RPChecker = async (blob: Blob): Promise<'JextRP' | 'RP' | 'NotValid'> => {
	const zip = await JSZip.loadAsync(blob);

	if (zip.file('pack.mcmeta') !== null) {
		if (zip.file('jext.json') !== null) {
			return 'JextRP';
		}

		return 'RP';
	}

	return 'NotValid';
};

export const areJextFilesValid = async (blob: Blob): Promise<boolean> => {
	const zip = await JSZip.loadAsync(blob);

	const baseText = await zip.file('jext.json')!.async('text');
	const nbsFile = zip.file('jext.nbs.json');
	const nbsText = nbsFile ? await nbsFile.async('text') : '[]';

	const validate = new Ajv().compile(newBaseSchema);
	const oldValidate = new Ajv().compile(oldBaseSchema);
	const nbsValidate = new Ajv().compile(nbsSchema);

	if (
		(validate(JSON.parse(baseText)) || oldValidate(JSON.parse(baseText))) &&
		nbsValidate(JSON.parse(nbsText))
	) {
		return true;
	}

	return false;
};

export const JextReader = async (blob: Blob): Promise<BaseDisc[]> => {
	const zip = await JSZip.loadAsync(blob);

	const text = await zip.file('jext.json')!.async('text');
	const textNbs = (await zip.file('jext.nbs.json')?.async('text')) ?? '[]';
	const packmcmeta = await zip.file('pack.mcmeta')!.async('text');
	const { pack } = JSON.parse(packmcmeta);
	const icon = await zip.file('pack.png')!.async('blob');

	const oldValidate = new Ajv().compile(oldBaseSchema);
	const parsedMusicDiscs = JSON.parse(text) as any[];
	const parsedNbsDiscs = JSON.parse(textNbs) as any[];

	if (oldValidate(parsedMusicDiscs)) {
		parsedMusicDiscs.forEach((element) => {
			element['fragment-loot-tables'] = {};
			element['loot-tables'] = {};
		});
	}

	const musicDiscs = await Promise.all(
		parsedMusicDiscs.map(async (element) => {
			const namespace = element['disc-namespace'];

			const retrievedFiles = {
				discTexture: await zip
					.file(`assets/minecraft/textures/item/music_disc_${namespace}.png`)!
					.async('blob'),
				fragmentTexture: await zip
					.file(`assets/minecraft/textures/item/fragment_${namespace}.png`)!
					.async('blob'),
				audioFile: await zip.file(`assets/minecraft/sounds/records/${namespace}.ogg`)!.async('blob')
			};

			const disc = new MusicDisc(new File([retrievedFiles.audioFile], ''), false);

			disc.title = element.title;
			disc.author = element.author;
			disc.namespace = namespace;
			disc.modelData = element['model-data'];
			disc.creeperDroppable = element['creeper-drop'];
			disc.tooltip = element.lores.join('\n');

			disc.setDiscTexture(retrievedFiles.discTexture);
			disc.setFragmentTexture(retrievedFiles.fragmentTexture);

			disc.discLootTables = element['loot-tables'];
			disc.fragmentLootTables = element['fragment-loot-tables'];

			disc.cachedFinalAudioFile = retrievedFiles.audioFile;

			return disc;
		})
	);

	const nbsDiscs = await Promise.all(
		parsedNbsDiscs.map(async (element) => {
			const namespace = element['disc-namespace'];

			const retrievedFiles = {
				discTexture: await zip
					.file(`assets/minecraft/textures/item/music_disc_${namespace}.png`)!
					.async('blob'),
				fragmentTexture: await zip
					.file(`assets/minecraft/textures/item/fragment_${namespace}.png`)!
					.async('blob'),
				nbsFile: await zip.file(`assets/jext/${namespace}.nbs`)!.async('blob')
			};

			const arrBuff = await retrievedFiles.nbsFile.arrayBuffer();

			const disc = new NbsDisc(new File([retrievedFiles.nbsFile], ''), arrBuff, false);

			disc.title = element.title;
			disc.author = element.author;
			disc.namespace = namespace;
			disc.modelData = element['model-data'];
			disc.creeperDroppable = element['creeper-drop'];
			disc.tooltip = element.lores.join('\n');

			disc.setDiscTexture(retrievedFiles.discTexture);
			disc.setFragmentTexture(retrievedFiles.fragmentTexture);

			disc.discLootTables = element['loot-tables'];
			disc.fragmentLootTables = element['fragment-loot-tables'];

			return disc;
		})
	);

	if(get(ResourcePackData).packs.length == 0) {
		ResourcePackData.update(data => {
			data.description = pack.description;
			data.version = pack.pack_format;
			data.icon = icon;

			return data;
		})
	}

	return [...musicDiscs, ...nbsDiscs];
};
