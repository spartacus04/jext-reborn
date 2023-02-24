export const dataURLToBlob = (dataURL: string): Blob | PromiseLike<Blob> => {
	return new Promise((resolve) => {
		const arrayBuffer = dataURLToArrayBuffer(dataURL);
		resolve(new Blob([arrayBuffer], { type: 'image/png' }));
	});
};

export const saveAs = (blob: Blob, filename: string) => {
	const link = document.createElement('a');
	link.href = URL.createObjectURL(blob);
	link.download = filename;
	link.click();
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

export const getDuration = async (blob: Blob) : Promise<number> => {
	return new Promise(resolve => {
		const audio = document.createElement('audio');

		audio.src = URL.createObjectURL(blob);

		audio.onloadedmetadata = () => {
			URL.revokeObjectURL(audio.src);
			resolve(Math.ceil(audio.duration));
		};
	});
};

export const mergeJson = (base: any, toMerge: any) : any => {
	if(toMerge.overrides) {
		base.overrides.push(...toMerge.overrides);

		return base;
	}

	return Object.assign(base, toMerge);
};


const dataURLToArrayBuffer = (dataURL: string) : ArrayBuffer => {
	const base64 = dataURL.split(',')[1];
	const binary = window.atob(base64);
	const arrayBuffer = new ArrayBuffer(binary.length);
	const uint8Array = new Uint8Array(arrayBuffer);

	for (let i = 0; i < binary.length; i++) {
		uint8Array[i] = binary.charCodeAt(i);
	}

	return arrayBuffer;
};


declare global {
	interface Array<T> {
		mapAsync<U>(callback: (value: T, index: number, array: T[]) => Promise<U>): Promise<U[]>;
		forEachParallel(callback: (value: T, index: number, array: T[]) => Promise<void>): Promise<void>;
	}
}

Array.prototype.mapAsync = async function <T, U>(callback: (value: T, index: number, array: T[]) => Promise<U>): Promise<U[]> {
	const promises = this.map(callback);
	return await Promise.all(promises);
};

Array.prototype.forEachParallel = async function <T>(callback: (value: T, index: number, array: T[]) => Promise<void>): Promise<void> {
	await Promise.all(this.map(callback));
};
