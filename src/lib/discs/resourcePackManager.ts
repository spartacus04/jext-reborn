import { default_icon } from '$lib/assets';
import { versions } from '$lib/constants';
import JSZip from 'jszip';
import { writable } from 'svelte/store';
import { randomTextures } from './textures';

interface PackData {
	icon: Blob;
	name: string;
	contents: Blob;
}

export interface ExportPackData {
	icon: Blob;
	name: string;
	description: string;
	version: number;
	packs: PackData[];
}

export const ResourcePackData = writable<ExportPackData>({
	icon: new Blob(),
	name: 'Jext Resources',
	description: 'Adds custom music discs to Minecraft!',
	version: Math.max(...Array.from(versions.keys())),
	packs: []
});

// hack to load the default icon
(async () => {
	const response = await fetch(default_icon);
	const icon = await response.blob();

	ResourcePackData.update((data) => {
		data.icon = icon;
		return data;
	});
})();

export const addRP = async (files?: File[]) => {
	if (files && files.length > 0 && files[0]) {
		const zip = await new JSZip().loadAsync(files[0]);

		const icon =
			(await zip.file('pack.png')?.async('blob')) ?? (await randomTextures()).discTexture;

		ResourcePackData.update((ResourcePackData) => {
			ResourcePackData.packs = [
				...ResourcePackData.packs,
				{
					name: files[0].name,
					icon: icon,
					contents: files[0]
				}
			];
			return ResourcePackData;
		});
	}
};
