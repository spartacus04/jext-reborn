import { get, writable } from 'svelte/store';
import type { BaseDisc } from './baseDisc';
import { MusicDisc } from './musicDisc';
import EditDiscsModal from '$lib/components/modals/EditDiscsModal.svelte';
import { baseElement } from '$lib/state';
import { NbsDisc } from './nbsDisc';
import EditDungeonModal from '$lib/components/modals/EditDungeonModal.svelte';
import { areJextFilesValid, JextReader, RPChecker } from '$lib/exporter/importer';
import { cAlert, cConfirm } from '$lib/utils';
import { addRP } from './resourcePackManager';

export const discsStore = writable<BaseDisc[]>([]);
export const selectedDiscs = writable<BaseDisc[]>([]);

export function addDisc(disc: BaseDisc) {
	discsStore.update((discs) => [...discs, disc]);
}

export function createNewDisc(files: File[] | undefined) {
	if (!files) return;

	const audioFiles = ['.mp3', '.wav', '.ogg', '.aac', '.flac'];

	files.forEach(async (file) => {
		if (audioFiles.some((ext) => file.name.endsWith(ext))) {
			const audioDisc = new MusicDisc(file);

			audioDisc.RerollTextures().then(() => {
				addDisc(audioDisc);
			});
		} else if (file.name.endsWith('.zip')) {
			const type = await RPChecker(file);

			switch (type) {
				case 'JextRP':
					if (!areJextFilesValid(file)) {
						cAlert('Invalid Jext Files files');
						return;
					}

					(await JextReader(file)).forEach((disc) => {
						addDisc(disc);
					});

					addRP([file]);

					break;
				case 'RP':
					if (
						(await cConfirm({
							text: 'This is a normal Resource Pack. Do you want to add it to the Resource Pack Manager?',
							confirmText: 'Yes',
							cancelText: undefined,
							discardText: 'No'
						})) == 'confirm'
					) {
						addRP([file]);
					}
					break;
				case 'NotValid':
					cAlert('Invalid Resource Pack');
					break;
			}
		} else if (file.name.endsWith('.nbs')) {
			const nbsDisc = new NbsDisc(file, await file.arrayBuffer());

			nbsDisc.RerollTextures().then(() => {
				addDisc(nbsDisc);
			});
		}
	});
}

export function removeDisc(disc: BaseDisc) {
	discsStore.update((discs) => discs.filter((d) => d !== disc));
}

export async function editDiscs(...discs: BaseDisc[]) {
	const changes = await new Promise<{ [key: string]: unknown }>((resolve) => {
		const editDiscsModal = new EditDiscsModal({
			target: get(baseElement)!,
			props: {
				onFinish: (changes) => {
					editDiscsModal.$destroy();
					resolve(changes);
				},
				discs
			}
		});

		editDiscsModal.openModal();
	});

	for (const change of Object.keys(changes)) {
		for (let i = 0; i < discs.length; i++) {
			if (
				['monoChannel', 'normalizeVolume', 'qualityPreset', 'disableTranscoding'].includes(change) &&
				!isMusicDisc(discs[i])
			)
				continue;

			// @ts-expect-error this is fine
			discs[i][change] = changes[change];
			if (discs[i].isNew) {
				discs[i].autoSetNamespace();
			}
			discs[i].refreshTextures();
		}
	}

	discsStore.update((d) => d);
}

export async function editLootTables(data: { [key: string]: number }) {
	const dataCopy = { ...data };

	return await new Promise<{ [key: string]: number } | null>((resolve) => {
		const editDungeonModal = new EditDungeonModal({
			target: get(baseElement)!,
			props: {
				onFinish: (changes) => {
					editDungeonModal.$destroy();
					resolve(changes);
				},
				dungeons: dataCopy
			}
		});

		editDungeonModal.openModal();
	});
}

export function isMusicDisc(disc: BaseDisc): disc is MusicDisc {
	// @ts-expect-error this is fine
	return disc instanceof MusicDisc || disc['audioFile'] !== undefined;
}
