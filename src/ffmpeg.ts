import Worker from '@/worker?worker';
import { dataURLToBlob } from '@/utils';

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

			const worker = new Worker();

			worker.onmessage = (e) => {
				resolve(new Blob([e.data], { type: 'audio/ogg' }));
			};

			worker.postMessage({
				audio: arrayBuffer,
				args: ['-acodec', 'libvorbis'],
			});
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

			const worker = new Worker();

			worker.onmessage = (e) => {
				resolve(new Blob([e.data], { type: 'audio/ogg' }));
			};

			worker.postMessage({ audio: arrayBuffer, args: ['-af', 'loudnorm'] });
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

			const worker = new Worker();

			worker.onmessage = (e) => {
				resolve(new Blob([e.data], { type: 'audio/ogg' }));
			};

			worker.postMessage({ audio: arrayBuffer, args: ['-ac', '1'] });
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