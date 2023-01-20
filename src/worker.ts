import { createFFmpeg } from '@ffmpeg/ffmpeg';

// FIXME
const ffmpeg = createFFmpeg({
	corePath: `../ffmpeg-core/ffmpeg-core.js`,
});

onmessage = async (e) => {
	const { audio, args } : { audio: ArrayBuffer, args: string[] } = e.data;

	await ffmpeg.load();
	ffmpeg.FS('writeFile', 'audio', new Uint8Array(audio));
	await ffmpeg.run('-i', 'audio', ...args, '/output.ogg');

	const output = ffmpeg.FS('readFile', '/output.ogg');

	ffmpeg.exit();

	postMessage(output);
};