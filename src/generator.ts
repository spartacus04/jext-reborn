import JSZip from 'jszip';

import { resizeImageBlob, stereoToMono, normalize } from '@/ffmpeg';
import { saveAs } from '@/utils';
import type { Disc, SongData } from '@/config';
import { versionStore } from '@/store';
import { isMinecraftRP } from '@/importer';

import { readme } from '@assets';


let version : number;


versionStore.subscribe(v => version = v);

export const generatePack = async (data: SongData[], icon : string, name : string, merge : boolean) => {
	return new Promise<void>(async (res) => {
		const rp = new JSZip();

		// Pack meta
		const packmcmeta = `
			{"pack": {"pack_format": ${version},"description": "Adds custom musics discs"}}
		`;

		rp.file('pack.mcmeta', packmcmeta);

		const packIcon = icon;
		const packpng = await (await (await fetch(packIcon)).blob()).arrayBuffer();

		rp.file('pack.png', packpng);

		const minecraft = rp.folder('assets')!.folder('minecraft')!;

		// sounds.json
		const soundsjson : {
			[key: string]: {
				sounds: {
					name: string,
					stream: boolean
				}[]
			}
		} = {};

		data.forEach(disc => {
			soundsjson[`music_disc.${disc.namespace}`] = {
				sounds: [
					{
						name: `records/${disc.namespace}`,
						stream: true,
					},
				],
			};
		});

		minecraft.file('sounds.json', JSON.stringify(soundsjson, null, 2));

		const models = minecraft.folder('models')!.folder('item')!;
		const sounds = minecraft.folder('sounds')!.folder('records')!;
		const textures = minecraft.folder('textures')!.folder('item')!;

		// disc 11 overrides
		const m11 = {
			parent: 'item/generated',
			textures: {
				layer0: 'item/music_disc_11',
			},
			overrides: data.map((disc, i) => {
				return {
					predicate: {
						custom_model_data: i + 1,
					},
					model: `item/music_disc_${disc.namespace}`,
				};
			}),
		};

		// fragment

		const mbone = {
			parent: 'item/generated',
			textures: {
				layer0: 'item/bone',
			},
			overrides: data.map((disc, i) => {
				return {
					predicate: {
						custom_model_data: i + 1,
					},
					model: `item/fragment_${disc.namespace}`,
				};
			}),
		};


		models.file('music_disc_11.json', JSON.stringify(m11, null, 2));
		models.file('bone.json', JSON.stringify(mbone, null, 2));

		// converts stereo to mono
		await data.forEachParallel(async (disc) => {
			if(disc.isMono && !disc.monoFile) {
				disc.monoFile = await stereoToMono(disc.oggFile);
			}
		});

		// disc 11 texture, audio and model
		for(let i = 0; i < data.length; i++) {
			const disc = data[i];

			const resizedTexture = await (await resizeImageBlob(disc.texture, 16, 16)).arrayBuffer();
			textures.file(`music_disc_${disc.namespace}.png`, resizedTexture);

			const resizedFragment = await (await resizeImageBlob(disc.fragmentTexture, 16, 16)).arrayBuffer();
			textures.file(`fragment_${disc.namespace}.png`, resizedFragment);

			const sound = disc.isMono ? disc.monoFile : disc.oggFile;

			const normalized = disc.normalize ? await normalize(sound) : sound;

			const soundbuffer = await normalized.arrayBuffer();

			sounds.file(`${disc.namespace}.ogg`, soundbuffer);

			models.file(`music_disc_${disc.namespace}.json`, JSON.stringify({
				parent: 'item/generated',
				textures: {
					layer0: `item/music_disc_${disc.namespace}`,
				},
			}, null, 2));

			// fragment

			models.file(`fragment_${disc.namespace}.json`, JSON.stringify({
				parent: 'item/generated',
				textures: {
					layer0: `item/fragment_${disc.namespace}`,
				},
			}, null, 2));
		}

		if(merge) {
			const input = document.createElement('input');
			input.type = 'file';
			input.accept = '.zip';
			input.click();

			await new Promise<void>((resolve, reject) => {
				input.onchange = async () => {
					if(!input.files || input.files.length == 0) reject('No file was selected');

					if(!(await isMinecraftRP(input.files[0]))) reject('The selected file is not a valid resource pack');

					const ufile = input.files[0];
					const zip = await JSZip.loadAsync(ufile);

					await zip.forEach(async (path, nfile) => {
						if(path == 'pack.mcmeta') return;
						if(path == 'pack.png') return;

						if(nfile.dir) {
							if(!rp.folder(path)) rp.folder(path);
						}
						else if(!rp.file(path)) {
							rp.file(path, await nfile.async('arraybuffer'));
						}
						else {
							reject('Cannot merge resource pack manually as there are conflicting files');
						}
					});

					resolve();
				};
			}).catch(e => {
				alert(e);
				res();
			});
		}

		rp.generateAsync({ type: 'blob' }).then(async content => {
			const zip = new JSZip();


			const discjson = await Promise.all(data.map(async (disc, i) => {
				return {
					title: disc.name,
					author: disc.author,
					duration: await getDuration(disc.isMono ? disc.monoFile : disc.oggFile),
					'disc-namespace': `music_disc.${disc.namespace}`,
					'model-data': i + 1,
					'creeper-drop': disc.creeperDrop,
					lores: disc.lores.split('\n'),
					'loot-tables': disc.lootTables,
					'fragment-loot-tables' : disc.fragmentLootTables,
				} satisfies Disc;
			}));

			zip.file('README.md', await (await fetch(readme)).arrayBuffer());
			zip.file(`${name}.zip`, await content.arrayBuffer());
			zip.file('discs.json', JSON.stringify(discjson, null, 2));

			zip.generateAsync({ type: 'blob' }).then(async resources => {
				saveAs(resources, 'open me.zip');

				res();
			});
		});
	});
};

const getDuration = async (blob: Blob) : Promise<number> => {
	return new Promise(resolve => {
		const audio = document.createElement('audio');
		audio.src = URL.createObjectURL(blob);
		audio.onloadedmetadata = () => {
			URL.revokeObjectURL(audio.src);
			resolve(Math.ceil(audio.duration));
		};
	});
};