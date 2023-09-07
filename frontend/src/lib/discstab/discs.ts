import { writable } from 'svelte/store';

interface resourcePack {
	name: string;
	version: string;
	description: string;
	icon: Blob;
	songs: song[];
}

interface song {
	oggFile: Blob;
	texture: Blob;
	fragmentTexture: Blob;
	title: string;
	author: string;
	duration: number;
	'disc-namespace': string;
	'model-data': number;
	'creeper-drop': boolean;
	lores: string[];
	'loot-tables': {[key: string]: number};
	'fragment-loot-tables': {[key: string]: number};
}

export const resourcePackStore = writable<resourcePack>({
	name: 'your resource pack',
	version: '15',
	description: 'Adds custom music discs',
	icon: null,
	songs: [],
});