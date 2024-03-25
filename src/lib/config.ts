import { writable } from 'svelte/store';
import type { Disc, ResourcePackData } from './types';
import default_icon from './assets/default_icon.png';

export const versions = new Map([
	[4, '1.14 - 1.14.4'],
	[5, '1.15 - 1.16.1'],
	[6, '1.16.2 - 1.16.5'],
	[7, '1.17 - 1.17.1'],
	[8, '1.18 - 1.18.2'],
	[9, '1.19 - 1.19.2'],
	[12, '1.19.3'],
	[13, '1.19.4'],
	[15, '1.20 - 1.20.1'],
	[18, '1.20.2'],
	[22, '1.20.3 - 1.20.4']
]);

export const discsStore = writable<Disc[]>([]);

export const resourcePackStore = writable<ResourcePackData>({
	icon: new Blob(),
	version: Math.max(...Array.from(versions.keys())),
	description: 'Adds custom music discs!',
	packs: []
});

fetch(default_icon).then(async (res) => {
	const blob = await res.blob();

	resourcePackStore.update((store) => {
		store.icon = blob;
		return store;
	});
});
