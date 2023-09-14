export const resizeImage = (image: Blob, height: number, width: number): Promise<Blob> => new Promise((resolve, reject) => {
	const canvas = document.createElement('canvas');
	const ctx = canvas.getContext('2d')!;
	const img = new Image();

	img.onload = () => {
		canvas.height = height;
		canvas.width = width;
		ctx.drawImage(img, 0, 0, width, height);
		img.remove();
		canvas.toBlob(resolve);
	};

	img.onerror = reject;
	img.src = URL.createObjectURL(image);
});