import { ResourcePackData } from '$lib/discs/resourcePackManager';
import { mergeDeep } from '$lib/utils';
import JSZip from 'jszip';
import { get } from 'svelte/store';

export const mergeResourcePacks = async (base: Blob) => {
	const packs = [base, ...get(ResourcePackData).packs.map((pack) => pack.contents)];

	const rp = new JSZip();

	const mergedJsonFiles: { [key: string]: unknown } = {};

	for (const pack of packs.toReversed()) {
		const packZip = await JSZip.loadAsync(pack);

		for (const path in packZip.files) {
			const file = packZip.files[path];

			if (!file.dir) {
				if (
					(file.name.endsWith('.json') &&
						!file.name.endsWith('jext.json') &&
						!file.name.endsWith('jext.nbs.json')) &&
						!file.name.endsWith('items/music_disc_11.json') &&
						!file.name.endsWith('items/disc_fragment_5.json') ||
					file.name.endsWith('.mcmeta')
				) {

					if (!mergedJsonFiles[file.name])
						mergedJsonFiles[file.name] = JSON.parse(await file.async('text'));
					else  {
						mergedJsonFiles[file.name] = mergeDeep(
							mergedJsonFiles[file.name],
							JSON.parse(await file.async('text'))
						);
					}
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
