import { default_disc } from '$lib/assets';
import { addDisc } from '$lib/discs/discManager';
import { MusicDisc } from '$lib/discs/musicDisc';
import { adaptImageToDisc, randomTextures } from '$lib/discs/textures';
import { base64ToArrayBuffer } from '$lib/utils';
import { invoke } from '@tauri-apps/api/core';
import { listen } from '@tauri-apps/api/event';
import { fetch as tfetch } from '@tauri-apps/plugin-http';
import { get, writable } from 'svelte/store';

export interface DownloadQueueElement {
	displayName?: string;
	url: string;
	status: string;
	iconUrl: string;
	overrides?: Overrides;
}

interface Overrides {
	title?: string;
	author?: string;
	description?: string;
	icon?: string;
}

export interface CommunityPack {
	name: string;
	description: string;
	descriptionAsTooltip: boolean;
	icon: string;
	authors: string[];
	downloads: {
		url: string;
		overrideName?: string;
		overrideAuthor?: string;
		overrideDescription?: string;
		baseIcon?: string;
	}[];
}

export const downloadQueue = writable<DownloadQueueElement[]>([]);

let isDownloading = false;

export const addDownloadQueueElement = async (url: string, overrides?: Overrides) => {
	const defaultDiscBlob = await fetch(default_disc).then((res) => res.blob());

	downloadQueue.update((queue) => [
		...queue,
		{ url, status: 'Download pending...', iconUrl: URL.createObjectURL(defaultDiscBlob), overrides }
	]);
};

export const addDownloadQueuePack = async (pack: CommunityPack) => {
	const { discTexture } = await adaptImageToDisc(await tfetch(pack.icon).then((res) => res.blob()));

	downloadQueue.update((queue) => [
		...queue,
		...pack.downloads.map((download) => {
			return {
				displayName: `${download.overrideName ?? pack.name} - ${download.overrideAuthor ?? pack.authors.join(', ')}`,
				url: download.url,
				status: 'Download pending...',
				iconUrl: URL.createObjectURL(discTexture),
				overrides: {
					title: download.overrideName,
					author: download.overrideAuthor,
					icon: download.baseIcon,
					description: download.overrideDescription ?? (pack.descriptionAsTooltip ? pack.description : undefined)
				}
			}
		})
	]);
}

export const setFirstDownloadQueueElementStatus = (status: string) => {
	downloadQueue.update((queue) => {
		queue[0].status = status;
		return queue;
	});
};

export const downloaderLine = async (url: string, overrides?: Overrides) => {
	if(url.trim() === '') return;

	const urlData = (await invoke('yt_dlp_get_playlist_info', { url })) as any;

	if (urlData['_type'] === 'playlist') {
		console.debug('playlist');
		for (let i = 0; i < urlData.entries.length; i++) {
			const entry = urlData.entries[i];

			if (entry['_type'] === 'url') {
				const entryUrl = entry.url;
				console.log(entryUrl);

				await addDownloadQueueElement(entryUrl, overrides);
			}
		}
	} else {
		await addDownloadQueueElement(url, overrides);
	}

	startDownloadQueue();
};

export const startDownloadQueue = async () => {
	const queue = get(downloadQueue);

	if (queue.length == 0 || isDownloading) return;

	isDownloading = true;

	setFirstDownloadQueueElementStatus('Starting download...');

	const firstElement = queue[0];

	const details = (await invoke('yt_dlp_get_info', { url: firstElement.url }).catch((e) => {
		console.error(e);

		downloadQueue.update((queue) => {
			queue.shift();
			return queue;
		});

		isDownloading = false;

		startDownloadQueue();
	})) as any;

	if (!details) return;

	const author = firstElement.overrides?.author ?? details.uploader ?? '';
	const title = firstElement.overrides?.title ?? details.title ?? 'Unknown';
	const thumbnail = firstElement.overrides?.icon ?? details.thumbnail ?? details.artwork_url ?? null;

	downloadQueue.update((queue) => {
		if (author != '') {
			queue[0].displayName = `${title} - ${author}`;
		} else {
			queue[0].displayName = title;
		}
		return queue;
	});

	const textures = thumbnail
		? await adaptImageToDisc(await tfetch(thumbnail).then((res) => res.blob()))
		: await randomTextures();

	URL.revokeObjectURL(firstElement.iconUrl);
	downloadQueue.update((queue) => {
		queue[0].iconUrl = URL.createObjectURL(textures.discTexture);
		return queue;
	});

	const unlisten = await listen('yt-dlp-download-progress', (event) => {
		setFirstDownloadQueueElementStatus(event.payload as string);
	});

	const b64eAudioFile: string = await invoke('yt_dlp_download', { url: firstElement.url });

	unlisten();

	const audioBlob = new Blob([await base64ToArrayBuffer(b64eAudioFile)], { type: 'audio/mp3' });
	const audioFile = new File([audioBlob], `${title} - ${author}.mp3`, { type: 'audio/mp3' });

	const disc = new MusicDisc(audioFile);

	if(firstElement.overrides?.description) {
		disc.tooltip = firstElement.overrides.description;
	}

	disc.setDiscTexture(textures.discTexture);
	disc.setFragmentTexture(textures.fragmentTexture);

	addDisc(disc);

	downloadQueue.update((queue) => {
		queue.shift();
		return queue;
	});

	isDownloading = false;

	startDownloadQueue();
};
