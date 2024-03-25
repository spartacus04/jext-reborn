import { localFFmpegStore } from '$lib/ffmpeg';
import type { LayoutLoad } from './$types';
import { invoke } from '@tauri-apps/api/tauri';

export const ssr = false;
export const prerender = true;

export const load: LayoutLoad = async ({ url }) => {
	if (window.__TAURI__) {
		const downloaded = await invoke<boolean>('download_ffmpeg');

		localFFmpegStore.set(downloaded);
	}

	// fetch url parameters
	const params = new URLSearchParams(url.search);

	const connect = !!params.get('c');
	const ip = params.get('ip');
	const port = params.get('port');

	return {
		server: {
			connect,
			ip,
			port: port ? (isNaN(+port) ? undefined : +port) : undefined
		}
	};
};
