import { createFFmpeg } from '@ffmpeg/ffmpeg';

import { dataURLToBlob } from '@/utils';


const ffmpeg = createFFmpeg({
	corePath: '../ffmpeg-core/ffmpeg-core.js',
	mainName: 'main',
});


export const convertToOgg = async (file: File) : Promise<Blob> => {
	if(file.type == 'audio/ogg' || import.meta.env.DEV) {
		return new Blob([file], { type: 'audio/ogg' });
	}

	return await new Promise((resolve) => {
		const reader = new FileReader();
		const blob = new Blob([file]);

		reader.readAsArrayBuffer(blob);

		reader.onload = async () => {
			const arrayBuffer = <ArrayBuffer>reader.result;
			await ffmpeg.load();
			ffmpeg.FS('writeFile', 'audio', new Uint8Array(arrayBuffer));
			await ffmpeg.run('-i', 'audio', '-acodec', 'libvorbis', '/output.ogg');

			const output = ffmpeg.FS('readFile', '/output.ogg');
			ffmpeg.exit();

			resolve(new Blob([output], { type: 'audio/ogg' }));
		};
	});
};

export const normalize = async (blob: Blob) : Promise<Blob> => {
	if(import.meta.env.DEV) return blob;

	return await new Promise((resolve) => {
		const reader = new FileReader();

		reader.readAsArrayBuffer(blob);

		reader.onload = async () => {
			const arrayBuffer = <ArrayBuffer>reader.result;
			await ffmpeg.load();
			ffmpeg.FS('writeFile', 'audio', new Uint8Array(arrayBuffer));
			await ffmpeg.run('-i', 'audio', '-af', 'loudnorm', '/output.ogg');

			const output = ffmpeg.FS('readFile', '/output.ogg');
			ffmpeg.exit();

			resolve(new Blob([output], { type: 'audio/ogg' }));
		};
	});
};

export const stereoToMono = async (blob: Blob) : Promise<Blob> => {
	if(import.meta.env.DEV) return blob;

	return await new Promise((resolve) => {
		const reader = new FileReader();

		reader.readAsArrayBuffer(blob);

		reader.onload = async () => {
			const arrayBuffer = <ArrayBuffer>reader.result;
			await ffmpeg.load();
			ffmpeg.FS('writeFile', 'audio', new Uint8Array(arrayBuffer));
			await ffmpeg.run('-i', 'audio', '-ac', '1', '/output.ogg');

			const output = ffmpeg.FS('readFile', '/output.ogg');
			ffmpeg.exit();

			resolve(new Blob([output], { type: 'audio/ogg' }));
		};
	});
};

export const resizeImageBlob = async (blob: Blob, width: number, height: number) : Promise<Blob> => {
	return await new Promise((resolve) => {
		const image = new Image();
		image.src = URL.createObjectURL(blob);

		image.onload = () => {
			const canvas = document.createElement('canvas');
			canvas.width = width;
			canvas.height = height;

			const ctx = canvas.getContext('2d');

			ctx!.drawImage(image, 0, 0, width, height);

			const dataURL = canvas.toDataURL('image/png');

			resolve(dataURLToBlob(dataURL));
		};
	});
};