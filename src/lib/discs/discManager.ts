import { get, writable } from 'svelte/store';
import type { BaseDisc } from './baseDisc';
import { MusicDisc } from './musicDisc';
import EditDiscsModal from '$lib/components/modals/EditDiscsModal.svelte';
import { baseElement } from '$lib/state';
import { NbsDisc } from './nbsDisc';
import EditDungeonModal from '$lib/components/modals/EditDungeonModal.svelte';
import { areJextFilesValid, JextReader, RPChecker } from '$lib/exporter/importer';
import { base64ToArrayBuffer, cAlert, cConfirm } from '$lib/utils';
import { addRP } from './resourcePackManager';
import { adaptImageToDisc } from './textures';
import { isTauri } from '$lib/state';
import { invoke } from '@tauri-apps/api/core';
import { appCacheDir } from '@tauri-apps/api/path';
import { remove, writeFile } from '@tauri-apps/plugin-fs';

export const discsStore = writable<BaseDisc[]>([]);
export const selectedDiscs = writable<BaseDisc[]>([]);

type FfprobeTags = Record<string, string | undefined>;

const pickTag = (tags: FfprobeTags | undefined, keys: string[]): string | undefined => {
	if (!tags) return undefined;
	for (const key of keys) {
		const value = tags[key];
		if (value && value.trim() !== '') return value.trim();
	}
	return undefined;
};

const decodeBase64 = (data: string): Uint8Array => {
	const binary = atob(data.replace(/\s+/g, ''));
	const bytes = new Uint8Array(binary.length);
	for (let i = 0; i < binary.length; i++) bytes[i] = binary.charCodeAt(i);
	return bytes;
};

const parseFlacPictureBlock = (data: Uint8Array) => {
	let offset = 0;
	const readU32 = () => {
		const value =
			(data[offset] << 24) |
			(data[offset + 1] << 16) |
			(data[offset + 2] << 8) |
			data[offset + 3];
		offset += 4;
		return value >>> 0;
	};

	readU32();
	const mimeLen = readU32();
	const mime = new TextDecoder().decode(data.slice(offset, offset + mimeLen));
	offset += mimeLen;
	const descLen = readU32();
	offset += descLen;
	readU32();
	readU32();
	readU32();
	readU32();
	const dataLen = readU32();
	const imageData = data.slice(offset, offset + dataLen);

	return { mime, imageData };
};

const getEmbeddedCover = (tags: FfprobeTags | undefined) => {
	if (!tags) return undefined;

	const coverArt = tags.COVERART || tags.coverart;
	const coverMime = tags.COVERARTMIME || tags.coverartmime || 'image/jpeg';
	if (coverArt) {
		return { mime: coverMime, data: decodeBase64(coverArt) };
	}

	const pictureBlock = tags.METADATA_BLOCK_PICTURE || tags.metadata_block_picture;
	if (pictureBlock) {
		try {
			const decoded = decodeBase64(pictureBlock);
			return parseFlacPictureBlock(decoded);
		} catch {
			return undefined;
		}
	}

	return undefined;
};

const applyTauriAudioMetadata = async (disc: MusicDisc, file: File) => {
	let filePath =
		(file as { tauriPath?: string; path?: string }).tauriPath ??
		(file as { path?: string }).path;
	let tempPath: string | null = null;


	if (!filePath) {
		const extension = file.name.split('.').pop() ?? 'mp3';
		const cacheDir = await appCacheDir();
		tempPath = `${cacheDir}/ffprobe-${Date.now()}-${Math.random().toString(16).slice(2)}.${extension}`;
		await writeFile(tempPath, new Uint8Array(await file.arrayBuffer()));
		filePath = tempPath;
	}


	try {
		let result: string | null = null;
		try {
			result = (await invoke('run_ffprobe', {
				args: ['-v', 'quiet', '-print_format', 'json', '-show_format', '-show_streams', filePath]
			})) as string;
		} catch (error) {
			console.error(error);
		}


		if (!result) return false;

		let data:
			| {
					format?: { tags?: FfprobeTags };
					streams?: Array<{ tags?: FfprobeTags; disposition?: { attached_pic?: number } }>;
			  }
			| null = null;
		try {
			data = JSON.parse(result);
		} catch {
			return false;
		}

		const formatTags = data?.format?.tags;
		const streamTags = data?.streams?.find((s) => s.tags)?.tags;
		const mergedTags = { ...streamTags, ...formatTags };

		const title = pickTag(mergedTags, ['title', 'TITLE']);
		const artist = pickTag(mergedTags, ['artist', 'ARTIST', 'album_artist', 'ALBUM_ARTIST']);

		if (title) disc.title = title;
		if (artist) disc.author = artist;


		const cover = getEmbeddedCover(mergedTags);
		if (cover) {
			const textures = await adaptImageToDisc(new Blob([cover.data], { type: cover.mime }));
			disc.setDiscTexture(textures.discTexture);
			disc.setFragmentTexture(textures.fragmentTexture);
			return true;
		}

		const hasAttachedPic = data?.streams?.some((s) => s.disposition?.attached_pic === 1);
		if (hasAttachedPic) {
			const coverResult = (await invoke('extract_ffmpeg_cover', {
				path: filePath
			}).catch(() => null)) as { mime: string; data: string } | null;


			if (coverResult?.data) {
				const textures = await adaptImageToDisc(
					new Blob([base64ToArrayBuffer(coverResult.data)], { type: coverResult.mime })
				);
				disc.setDiscTexture(textures.discTexture);
				disc.setFragmentTexture(textures.fragmentTexture);
				return true;
			}
		}

		return false;
	} finally {
		if (tempPath) {
			await remove(tempPath).catch(() => undefined);
		}

	}
};

export function addDisc(disc: BaseDisc) {
	discsStore.update((discs) => [...discs, disc]);
}

export function createNewDisc(files: File[] | undefined) {
	if (!files) return;

	const audioFiles = ['.mp3', '.wav', '.ogg', '.aac', '.flac'];

	files.forEach(async (file) => {
		if (audioFiles.some((ext) => file.name.endsWith(ext))) {
			const audioDisc = new MusicDisc(file);

			let appliedCover = false;
			if (isTauri) {
				appliedCover = await applyTauriAudioMetadata(audioDisc, file);
			}

			if (!appliedCover) {
				await audioDisc.RerollTextures();
			}

			audioDisc.autoSetNamespace();
			addDisc(audioDisc);
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
