import type { LayoutLoad } from './$types';

import { default_icon } from '$lib/assets';

import { invoke } from '@tauri-apps/api/tauri';

import { resourcePackStore, localFFmpegStore } from '$lib';

export const ssr = false;
export const prerender = true;
export const trailingSlash = 'always';


export const load: LayoutLoad = async ({ url, fetch }) => {
	if (window.__TAURI__) {
		const downloaded = await invoke<boolean>('download_ffmpeg');

		localFFmpegStore.set(downloaded);
	}

	// fetch url parameters
	const params = new URLSearchParams(url.search);

	const connect = !!params.get('c');
	const ip = params.get('ip');
	const port = params.get('port');

	(() =>
		fetch(default_icon).then(async (res) => {
			const blob = await res.blob();

			resourcePackStore.update((store) => {
				store.icon = blob;
				return store;
			});
		}))();

	return {
		server: {
			connect,
			ip,
			port: port ? (isNaN(+port) ? undefined : +port) : undefined
		}
	};
};
