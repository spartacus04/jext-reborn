export const dataURLToBlob = (dataURL: string): Blob | PromiseLike<Blob> => {
	return new Promise((resolve) => {
		const arrayBuffer = dataURLToArrayBuffer(dataURL);
		resolve(new Blob([arrayBuffer], { type: 'image/png' }));
	});
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

export const saveAs = (blob: Blob, filename: string) => {
	const link = document.createElement('a');
	link.href = URL.createObjectURL(blob);
	link.download = filename;
	link.click();
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
