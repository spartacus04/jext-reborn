import Worker from '@/worker?worker';

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
				worker.terminate();
			};

			worker.postMessage({
				audio: arrayBuffer,
				args: ['-vn', '-acodec', 'libvorbis'],
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
				worker.terminate();
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
				worker.terminate();
			};

			worker.postMessage({ audio: arrayBuffer, args: ['-ac', '1'] });
		};
	});
};
