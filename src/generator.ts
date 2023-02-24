import JSZip from 'jszip';
import { writable } from 'svelte/store';

import { stereoToMono, normalize } from '@/ffmpeg';
import { saveAs, resizeImageBlob, getDuration, mergeJson } from '@/utils';
import type { Disc, SongData } from '@/config';
import { versionStore, iconStore, nameStore, discStore } from '@/store';
import { isMinecraftRP } from '@/importer';

import { pack_icon, readme } from '@assets';


export const totalCount = writable(0);
export const currentCount = writable(0);


let discs : SongData[] = [];
let version = 12;
let icon = pack_icon;
let name = 'your_pack_name';

versionStore.subscribe(v => version = v);
iconStore.subscribe(i => icon = i);
nameStore.subscribe(n => name = n);
discStore.subscribe(d => discs = d);


const generateDiscsJson = async () : Promise<string> => {
	const discjson = await Promise.all(discs.map(async (disc, i) => {
		return {
			title: disc.name,
			author: disc.author,
			duration: await getDuration(disc.isMono ? disc.monoFile : disc.oggFile),
			'disc-namespace': `music_disc.${disc.namespace}`,
			'model-data': i + 1,
			'creeper-drop': disc.creeperDrop,
			lores: disc.lores.split('\n').length == 1 && disc.lores.split('\n')[0] == '' ? [] : disc.lores.split('\n'),
			'loot-tables': disc.lootTables.join(',').split(',').filter(e => e != ''),
			'fragment-loot-tables' : disc.fragmentLootTables.join(',').split(',').filter(e => e != ''),
		} satisfies Disc;
	}));

	return JSON.stringify(discjson, null, 2);
};

const generateResourcePack = async () : Promise<JSZip> => {
	const rp = new JSZip();

	// Pack meta
	const packmcmeta = `
			{"pack": {"pack_format": ${version},"description": "Adds custom musics discs"}}
		`;

	rp.file('pack.mcmeta', packmcmeta);

	const packpng = await (await (await fetch(icon)).blob()).arrayBuffer();

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

	discs.forEach(disc => {
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
		overrides: discs.map((disc, i) => {
			return {
				predicate: {
					custom_model_data: i + 1,
				},
				model: `item/music_disc_${disc.namespace}`,
			};
		}),
	};

	// fragment

	const mfragment = {
		parent: 'item/generated',
		textures: {
			layer0: 'item/disc_fragment_5',
		},
		overrides: discs.map((disc, i) => {
			return {
				predicate: {
					custom_model_data: i + 1,
				},
				model: `item/fragment_${disc.namespace}`,
			};
		}),
	};


	models.file('music_disc_11.json', JSON.stringify(m11, null, 2));
	models.file('disc_fragment_5.json', JSON.stringify(mfragment, null, 2));

	// converts stereo to mono

	// run foreach into chunks of 4 to avoid too many ffmpeg processes

	for(let i = 0; i < discs.length; i += 4) {
		const chunk = discs.slice(i, i + 4);

		await chunk.forEachParallel(async (disc) => {
			if(disc.isMono && !disc.monoFile) {
				disc.monoFile = await stereoToMono(disc.oggFile);
				currentCount.update(n => n + 1);
			}
		});

		discs.splice(i, 4, ...chunk);
	}

	// disc 11 texture, audio and model
	for(let i = 0; i < discs.length; i++) {
		const disc = discs[i];

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

	return rp;
};

// TODO: handle resourcepack merging
const mergeResourcePack = async (rp : JSZip) : Promise<JSZip|string> => {
	return await new Promise<JSZip>((resolve, reject) => {
		const input = document.createElement('input');
		input.type = 'file';
		input.accept = '.zip';
		input.click();

		input.onchange = async () => {
			if(!input.files || input.files.length == 0) reject('No file was selected');

			if(!(await isMinecraftRP(input.files[0]))) reject('The selected file is not a valid resource pack');

			const toMerge = await JSZip.loadAsync(input.files[0]);

			await toMerge.forEach(async (path, file) => {
				if(path == 'pack.mcmeta') return;
				if(path == 'pack.png') return;

				if(file.dir) {
					if(!rp.folder(path)) rp.folder(path);
				}
				else if(!rp.file(path)) {
					rp.file(path, await file.async('arraybuffer'));
				}
				else if(path.endsWith('.json')) {
					// merge json file
					const base = JSON.parse(await rp.file(path)!.async('text'));
					const newRp = JSON.parse(await file.async('text'));

					const merged = mergeJson(base, newRp);

					rp.file(path, JSON.stringify(merged, null, 2));
				}
				else {
					reject('Cannot merge resource pack manually as there are conflicting files that cannot be merged');
				}
			});

			resolve(rp);
		};
	}).catch(err => {
		return err;
	});
};

export const generatePack = async (merge : boolean) => {
	currentCount.set(0);
	totalCount.set(discs.length + 1 + (merge ? 1 : 0));

	let rp = await generateResourcePack();

	if(merge) {
		const output = await mergeResourcePack(rp);

		if(typeof output == 'string') {
			alert(output);
			currentCount.set(0);
			totalCount.set(0);
			return;
		}

		rp = output;
		currentCount.update(n => n + 1);
	}

	const discsJson = await generateDiscsJson();
	currentCount.update(n => n + 1);

	rp.generateAsync({ type: 'blob' }).then(async content => {
		const zip = new JSZip();

		zip.file('README.md', await (await fetch(readme)).arrayBuffer());
		zip.file(`${name}.zip`, await content.arrayBuffer());
		zip.file('discs.json', discsJson);

		zip.generateAsync({ type: 'blob' }).then(async resources => {
			saveAs(resources, 'open me.zip');

			currentCount.set(0);
			totalCount.set(0);
		});
	});
};