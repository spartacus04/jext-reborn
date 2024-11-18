import { get, writable } from 'svelte/store';
import { FFmpeg } from '@ffmpeg/ffmpeg';

import { listen } from '@tauri-apps/api/event';
import { invoke } from '@tauri-apps/api/tauri';
import { arrayBufferToBase64, base64ToArrayBuffer, blobToArraBuffer, downloadWithProgress, getDuration } from '../utils';
import { removeStep, updateSteps } from './exporterLine';
import type { MusicDisc } from '$lib/discs/musicDisc';

const baseURL = 'https://unpkg.com/@ffmpeg/core@0.12.6/dist/esm';
const baseMTURL = 'https://unpkg.com/@ffmpeg/core-mt@0.12.6/dist/esm';

const qualityArgs = {
	none: [],
	low: ['-ar', '22050', '-qscale:a', '9'],
	medium: ['-ar', '44100', '-qscale:a', '1'],
	high: ['-ar', '48000', '-qscale:a', '7']
};

export const localFFmpegStore = writable(false);

export const convertAudio = async (
	disc: MusicDisc,
	ffmpeg: FFmpeg | null,
): Promise<Blob | null> => {
	const args = ['-vn', '-acodec', 'libvorbis'];

	if (disc.monoChannel) args.push('-ac', '1');

	if (disc.normalizeVolume) args.push('-af', 'loudnorm');

	args.push(...qualityArgs[disc.qualityPreset]);

	if (window.__TAURI__ && get(localFFmpegStore)) {
		const duration = await getDuration(disc.audioFile);

		const unregister = await listen<string>('ffmpeg-progress', async (event) => {
			const [hours, minutes, seconds] = event.payload.split(':').map(Number);

			updateSteps(2, 'Converting audio file', Math.ceil(hours * 3600 + minutes * 60 + seconds), Math.ceil(duration));
		});

		const result = <string>await invoke('ffmpeg', {
			input: arrayBufferToBase64(await disc.audioFile.arrayBuffer()),
			args
		});

		unregister();
		removeStep(2);

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

		await ffmpeg.writeFile('input.ogg', new Uint8Array(await blobToArraBuffer(disc.audioFile)));

		ffmpeg.on('progress', (event) =>
			updateSteps(2, 'Converting audio file', Math.ceil(event.progress * 100), 100)
		);

		await ffmpeg.exec(['-i', 'input.ogg', ...args, 'output.ogg']);

		const result = await ffmpeg.readFile('output.ogg');

		ffmpeg.deleteFile('input.ogg');
		ffmpeg.deleteFile('output.ogg');

		removeStep(2);

		return new Blob([result], { type: 'audio/ogg' });
	}
};

export const loadFFmpeg = async (): Promise<FFmpeg | null> => {
	const ffmpeg = new FFmpeg();

	if (window.__TAURI__ || ffmpeg.loaded) return null;

	updateSteps(0, 'Loading FFmpeg', 0, 3);

	updateSteps(1, 'Downloading ffmpeg-core.js', 0, 3);
	const coreURL = await fetchData(crossOriginIsolated ? `${baseMTURL}/ffmpeg-core.js` : `${baseURL}/ffmpeg-core.js`);
	
	updateSteps(1, 'Downloading ffmpeg-core.wasm', 1, 3);
	const wasmURL = await fetchData(crossOriginIsolated ? `${baseMTURL}/ffmpeg-core.wasm` : `${baseURL}/ffmpeg-core.wasm`);
	
	updateSteps(1, 'Loading FFmpeg', 2, 3);
	await ffmpeg.load({
		coreURL,
		wasmURL
	});

	updateSteps(0, 'FFmpeg loaded', 3, 3);
	removeStep(1);

	return ffmpeg;
};

const fetchData = async (url: string) => {
	const arraybuffer = await downloadWithProgress(url, () => {});

	return URL.createObjectURL(
		new Blob([arraybuffer], {
			type: url.endsWith('.wasm') ? 'application/wasm' : 'application/javascript'
		})
	);
};