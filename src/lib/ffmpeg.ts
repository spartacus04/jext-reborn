import type { FFmpegData } from './types';
import { FFmpeg } from '@ffmpeg/ffmpeg';
import {
	arrayBufferToBase64,
	base64ToArrayBuffer,
	blobToArraBuffer,
	downloadWithProgress
} from './utils';
import { invoke } from '@tauri-apps/api/tauri';
import { get, writable } from 'svelte/store';
import { listen } from '@tauri-apps/api/event';
import { getDuration } from './resourcepack/utils';

const baseURL = 'https://unpkg.com/@ffmpeg/core@0.12.6/dist/esm';
const baseMTURL = 'https://unpkg.com/@ffmpeg/core-mt@0.12.6/dist/esm';

const qualityArgs = {
	none: [],
	low: ['-ar', '22050', '-qscale:a', '9'],
	medium: ['-ar', '44100', '-qscale:a', '1'],
	high: ['-ar', '48000', '-qscale:a', '7']
};

export const localFFmpegStore = writable(false);

export const prepareAudio = async (
	blob: Blob,
	data: FFmpegData,
	onProgress: (
		total: number | undefined,
		done: number | undefined,
		status: string | undefined,
		index: number
	) => unknown,
	ffmpeg: FFmpeg | null
): Promise<Blob | null> => {
	const args = ['-vn', '-acodec', 'libvorbis'];

	if (data.mono) args.push('-ac', '1');

	if (data.normalize) args.push('-af', 'loudnorm');

	args.push(...qualityArgs[data.quality]);

	if (window.__TAURI__ && get(localFFmpegStore)) {
		const duration = await getDuration(blob);

		const unregister = await listen<string>('ffmpeg-progress', async (event) => {
			const [hours, minutes, seconds] = event.payload.split(':').map(Number);

			onProgress(
				duration,
				Math.ceil(hours * 3600 + minutes * 60 + seconds),
				'Converting audio file',
				2
			);
		});

		const result = <string>await invoke('ffmpeg', {
			input: arrayBufferToBase64(await blob.arrayBuffer()),
			args
		});

		unregister();

		onProgress(undefined, undefined, undefined, 2);

		if (result === '') return null;

		return new Blob([base64ToArrayBuffer(result)], { type: 'audio/ogg' });
	} else {
		if (ffmpeg === null) return null;

		if (!ffmpeg.loaded)
			await ffmpeg.load(
				crossOriginIsolated
					? {
							coreURL: `${baseMTURL}/ffmpeg-core.js`,
							wasmURL: `${baseMTURL}/ffmpeg-core.wasm`
						}
					: {
							coreURL: `${baseURL}/ffmpeg-core.js`,
							wasmURL: `${baseURL}/ffmpeg-core.wasm`
						}
			);

		await ffmpeg.writeFile('input.ogg', new Uint8Array(await blobToArraBuffer(blob)));

		ffmpeg.on('progress', (event) =>
			onProgress(100, Math.ceil(event.progress * 100), 'Converting audio file', 2)
		);

		await ffmpeg.exec(['-i', `input.ogg`, ...args, `output.ogg`]);

		const result = await ffmpeg.readFile(`output.ogg`);

		ffmpeg.deleteFile(`input.ogg`);
		ffmpeg.deleteFile(`output.ogg`);

		onProgress(undefined, undefined, undefined, 2);

		return new Blob([result], { type: 'audio/ogg' });
	}
};

export const loadFFmpeg = async (
	onProgress: (
		total: number | undefined,
		done: number | undefined,
		status: string | undefined,
		index: number
	) => unknown
): Promise<FFmpeg | null> => {
	const ffmpeg = new FFmpeg();

	if (window.__TAURI__ || ffmpeg.loaded) return null;

	onProgress(2, 0, 'Downloading ffmpeg-core.js', 1);

	await ffmpeg.load(
		crossOriginIsolated
			? {
					coreURL: await fetchData(`${baseMTURL}/ffmpeg-core.js`, (done) => {
						if (done) onProgress(2, 1, 'Downloading ffmpeg-core.wasm', 1);
					}),
					wasmURL: await fetchData(`${baseMTURL}/ffmpeg-core.wasm`, (done) => {
						if (done) onProgress(2, 2, 'Finished downloading', 1);
					})
				}
			: {
					coreURL: await fetchData(`${baseURL}/ffmpeg-core.js`, (done) => {
						if (done) onProgress(2, 1, 'Downloading ffmpeg-core.wasm', 1);
					}),
					wasmURL: await fetchData(`${baseURL}/ffmpeg-core.wasm`, (done) => {
						if (done) onProgress(2, 2, 'Finished downloading', 1);
					})
				}
	);

	onProgress(undefined, undefined, undefined, 1);

	return ffmpeg;
};

const fetchData = async (url: string, progress: (done: boolean) => unknown) => {
	const arraybuffer = await downloadWithProgress(url, (e) => {
		progress(e.done);
	});

	return URL.createObjectURL(
		new Blob([arraybuffer], {
			type: url.endsWith('.wasm') ? 'application/wasm' : 'application/javascript'
		})
	);
};
