export const hashStr = (str: string) => {
	let hash = 0;

	for (let i = 0; i < str.length; i++) {
		hash = ((hash << 5) - hash) + str.charCodeAt(i);
		hash |= 0;
	}

	return hash;
};

export const convertToOgg = async (file: File) : Promise<Blob> => {
	// TODO: this doesn't convert to ogg, should use ffmpeg.js and MEMFS
	switch(file.type) {
	case 'audio/ogg':
		return new Blob([file], { type: 'audio/ogg' });
	case 'audio/mp3':
	{
		return await new Promise((resolve) => {
			const reader = new FileReader();
			const blob = new Blob([file], { type: 'audio/mp3' });
			reader.readAsArrayBuffer(blob);

			reader.onload = () => {
				const arrayBuffer = reader.result as ArrayBuffer;
				resolve(new Blob([arrayBuffer], { type: 'audio/ogg' }));
			};
		});
	}
	case 'audio/wav':
	{
		return await new Promise((resolve) => {
			const reader = new FileReader();
			const blob = new Blob([file], { type: 'audio/wav' });
			reader.readAsArrayBuffer(blob);

			reader.onload = () => {
				const arrayBuffer = reader.result as ArrayBuffer;
				resolve(new Blob([arrayBuffer], { type: 'audio/ogg' }));
			};
		});
	}
	default:
		return new Blob([file], { type: 'audio/ogg' });
	}
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

function dataURLToBlob(dataURL: string): Blob | PromiseLike<Blob> {
	return new Promise((resolve) => {
		const arrayBuffer = dataURLToArrayBuffer(dataURL);
		resolve(new Blob([arrayBuffer], { type: 'image/png' }));
	});
}

function dataURLToArrayBuffer(dataURL: string) {
	const base64 = dataURL.split(',')[1];
	const binary = atob(base64);
	const arrayBuffer = new ArrayBuffer(binary.length);
	const uint8Array = new Uint8Array(arrayBuffer);
	for (let i = 0; i < binary.length; i++) {
		uint8Array[i] = binary.charCodeAt(i);
	}
	return arrayBuffer;
}

export const saveAs = (blob: Blob, filename: string) => {
	const link = document.createElement('a');
	link.href = URL.createObjectURL(blob);
	link.download = filename;
	link.click();
};