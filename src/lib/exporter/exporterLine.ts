import { get, writable } from 'svelte/store';
import { loadFFmpeg } from './ffmpeg';
import { preProcessDiscs } from './preprocessor';
import type { BaseExporter } from './baseExporter';
import { ResourcePackData } from '$lib/discs/resourcePackManager';
import { mergeResourcePacks } from './rpmerger';
import { discsStore } from '$lib/discs/discManager';
import { isTauri } from '$lib/state';
import { exists, readDir, writeFile, mkdir, remove, stat } from '@tauri-apps/plugin-fs'
import { appCacheDir } from '@tauri-apps/api/path';

export const exporterSteps = writable<
	(
		| {
				status: string;
				current: number;
				total: number;
		  }
		| undefined
	)[]
>([]);

export const removeStep = (index: number) => {
	exporterSteps.update((steps) => {
		steps[index] = undefined;
		return steps;
	});
};

export const updateSteps = (index: number, status: string, current: number, total: number) => {
	exporterSteps.update((steps) => {
		steps[index] = { status, current, total };
		return steps;
	});
};

export const exportResourcePack = async (exporter: BaseExporter) => {
	const pack = get(ResourcePackData);

	exporterSteps.set([
		{ status: 'Loading FFmpeg', current: 0, total: pack.packs.length > 0 ? 4 : 3 },
		undefined,
		undefined
	]);
	const ffmpeg = await loadFFmpeg();

	updateSteps(0, 'Building music objects', 1, pack.packs.length > 0 ? 4 : 3);
	await preProcessDiscs(ffmpeg);

	updateSteps(0, 'Building resource pack', 2, pack.packs.length > 0 ? 4 : 3);
	const output = await exporter.export();

	discsStore.update((discs) => {
		discs.forEach((disc) => {
			disc.isNew = false;
		});

		return discs;
	});

	if (pack.packs.length > 0) {
		updateSteps(0, 'Merging resource pack', 3, 4);

		const merged = await mergeResourcePacks(output.javaRP);

		updateSteps(0, 'Finishing up', 4, 4);

		await saveRecentExport(merged);

		return {
			javaRP: merged,
			bedrockRP: output.bedrockRP
		};
	}

	updateSteps(0, 'Finishing up', 3, 3);

	await saveRecentExport(output.javaRP);

	return output;
};

const saveRecentExport = async (rp: Blob) => {
	if(!isTauri) return;

	const baseDir = `${await appCacheDir()}/recentExports`;

	if(!(await exists(baseDir))) {
		await mkdir(baseDir, {
			recursive: true
		});
	}

	const files = await readDir(baseDir);

	if(files.length >= 15) {
		let oldest = files[0];
		let oldestStat = await stat(`${baseDir}/${oldest}`);

		for(let i = 1; i < files.length; i++) {
			const file = files[i];
			const newStat = await stat(`${baseDir}/${file}`);

			if(newStat.birthtime! < oldestStat.birthtime!) {
				oldest = file;
				oldestStat = newStat;
			}
		}

		await remove(`${baseDir}/${oldest}`);
	}

	const name = `export-${new Date().toISOString().replaceAll(/:/g, '-').replaceAll('-', '+')}-${get(ResourcePackData).name}`;

	await writeFile(`${baseDir}/${name}`, new Uint8Array(await rp.arrayBuffer()));
}