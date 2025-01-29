import JSZip from 'jszip';
import { BaseExporter } from './baseExporter';
import { get } from 'svelte/store';
import { discsStore, isMusicDisc } from '$lib/discs/discManager';
import type { NbsDisc } from '$lib/discs/nbsDisc';
import { convertToPng, getDuration, getVersionFromTime } from '$lib/utils';
import { ResourcePackData } from '$lib/discs/resourcePackManager';
import { MusicDisc } from '$lib/discs/musicDisc';

export class PluginExporter extends BaseExporter {
	public name = 'JEXT Reborn';
	public icon: string = '';
	public subtitle: string = '';

	public async export() {
		return {
			javaRP: await this.buildJavaRP(),
			bedrockRP: await this.buildBedrockRP()
		};
	}

	private async buildJavaRP(): Promise<Blob> {
		const packData = get(ResourcePackData);

		const rp = new JSZip();

		const discs = get(discsStore);

		rp.file(
			'pack.mcmeta',
			JSON.stringify(
				{
					pack: {
						pack_format: packData.version,
						description: packData.description
					}
				},
				null,
				2
			)
		);

		rp.file('pack.png', await convertToPng(packData.icon));

		const sounds: { [key: string]: unknown } = {};

		for (const disc of discs) {
			if (isMusicDisc(disc)) {
				sounds[disc.namespace] = {
					sounds: [
						{
							name: `records/${disc.namespace}`,
							stream: true
						}
					]
				};
			}
		}

		rp.file('assets/minecraft/sounds.json', JSON.stringify(sounds, null, 2));

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
								custom_model_data: disc.modelData
							},
							model: `item/music_disc_${disc.namespace}`
						};
					})
				},
				null,
				2
			)
		);

		rp.file(
			'assets/minecraft/models/item/music_disc_5.json',
			JSON.stringify(
				{
					parent: 'item/generated',
					textures: {
						layer0: 'item/music_disc_5'
					},
					overrides: discs.map((disc) => {
						return {
							predicate: {
								custom_model_data: disc.modelData
							},
							model: `item/fragment_${disc.namespace}`
						};
					})
				},
				null,
				2
			)
		);

		rp.file(
			`assets/minecraft/items/music_disc_11.json`,
			JSON.stringify(
				{
					model: {
						type: 'range_dispatch',
						property: 'custom_model_data',
						fallback: {
							type: 'model',
							model: 'item/music_disc_11'
						},
						entries: discs.map((disc) => {
							return {
								threshold: disc.modelData,
								model: {
									type: 'model',
									model: `item/music_disc_${disc.namespace}`
								}
							}
						})
					}
				},
				null,
				2
			)
		);

		rp.file(
			`assets/minecraft/items/fragment_5.json`,
			JSON.stringify(
				{
					model: {
						type: 'range_dispatch',
						property: 'custom_model_data',
						fallback: {
							type: 'model',
							model: 'item/music_disc_5'
						},
						entries: discs.map((disc) => {
							return {
								when: disc.modelData,
								model: {
									type: 'model',
									model: `item/fragment_${disc.namespace}`
								}
							}
						})
					}
				},
				null,
				2
			)
		);

		for (const disc of discs) {
			rp.file(
				`assets/minecraft/models/item/music_disc_${disc.namespace}.json`,
				JSON.stringify(
					{
						parent: 'item/generated',
						textures: {
							layer0: `item/music_disc_${disc.namespace}`
						}
					},
					null,
					2
				)
			);

			// fragment model
			rp.file(
				`assets/minecraft/models/item/fragment_${disc.namespace}.json`,
				JSON.stringify(
					{
						parent: 'item/generated',
						textures: {
							layer0: `item/fragment_${disc.namespace}`
						}
					},
					null,
					2
				)
			);

			// disc texture
			rp.file(`assets/minecraft/textures/item/music_disc_${disc.namespace}.png`, await convertToPng(disc.discTexture));

			// fragment texture
			rp.file(
				`assets/minecraft/textures/item/fragment_${disc.namespace}.png`,
				await convertToPng(disc.fragmentTexture)
			);

			// track
			if (isMusicDisc(disc)) {
				rp.file(
					`assets/minecraft/sounds/records/${disc.namespace}.ogg`,
					disc.cachedFinalAudioFile!
				);
			} else {
				rp.file(`assets/jext/${disc.namespace}.nbs`, (disc as NbsDisc).nbsFile);
			}
		}

		rp.file(
			'jext.json',
			JSON.stringify(
				await Promise.all(
					discs.filter(isMusicDisc).map(async (disc) => {
						return {
							title: disc.title,
							author: disc.author,
							duration: await getDuration(disc.cachedFinalAudioFile!),
							'disc-namespace': disc.namespace,
							'model-data': disc.modelData,
							'creeper-drop': disc.creeperDroppable,
							lores: (() => {
								const lores = disc.tooltip.split('\n');

								if (lores.length == 1 && lores[0] == '') {
									return [];
								}

								return lores;
							})(),
							'loot-tables': disc.discLootTables,
							'fragment-loot-tables': disc.fragmentLootTables
						};
					})
				),
				null,
				2
			)
		);

		rp.file(
			'jext.nbs.json',
			JSON.stringify(
				discs
					.filter((d) => !isMusicDisc(d))
					.map((disc) => {
						return {
							title: disc.title,
							author: disc.author,
							'disc-namespace': disc.namespace,
							'model-data': disc.modelData,
							'creeper-drop': disc.creeperDroppable,
							lores: (() => {
								const lores = disc.tooltip.split('\n');

								if (lores.length == 1 && lores[0] == '') {
									return [];
								}

								return lores;
							})(),
							'loot-tables': disc.discLootTables,
							'fragment-loot-tables': disc.fragmentLootTables
						};
					}),
				null,
				2
			)
		);

		return await rp.generateAsync({ type: 'blob' });
	}

	private async buildBedrockRP(): Promise<Blob | undefined> {
		const rp = new JSZip();

		const discs = get(discsStore);
		const resourcePackData = get(ResourcePackData);

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

		rp.file(
			'mappings.json',
			JSON.stringify(
				{
					format_version: 2,
					items: {
						'minecraft:music_disc_11': discs.map((disc) => ({
							name: `music_disc_${disc.namespace}`,
							custom_model_data: disc.modelData,
							display_name: 'Music Disc'
						}))
					}
				},
				null,
				2
			)
		);

		// pack_icon.png

		rp.file('pack_icon.png', await convertToPng(resourcePackData.icon));

		// item_texture.json

		const itemTextures: {
			[key: string]: {
				textures: string[];
			};
		} = {};

		for (const disc of discs) {
			itemTextures[`music_disc_${disc.namespace}`] = {
				textures: [`textures/items/music_disc_${disc.namespace}`]
			};

			itemTextures[`fragment_${disc.namespace}`] = {
				textures: [`textures/items/fragment_${disc.namespace}`]
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
			soundDefinitions[disc.namespace] = {
				__use_legacy_max_distance: true,
				category: 'record',
				max_distance: 64.0,
				sounds: [
					{
						name: `sounds/jext/${disc.namespace}`,
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
			rp.file(`textures/items/music_disc_${disc.namespace}.png`, await convertToPng(disc.discTexture));
			rp.file(`textures/items/fragment_${disc.namespace}.png`, await convertToPng(disc.fragmentTexture));

			if (isMusicDisc(disc)) {
				rp.file(`sounds/jext/${disc.namespace}.ogg`, (disc as MusicDisc).cachedFinalAudioFile!);
			}
		}

		return await rp.generateAsync({ type: 'blob' });
	}
}
