import { disc_template, fragment_template } from '$lib/assets';

import type { Disc } from '../';


export const namespace = (disc: Disc) =>
	`${disc.title}${disc.author}${disc['model-data']}`
		.replace(/[^a-zA-Z0-9]/g, '')
		.replaceAll('1', 'one')
		.replaceAll('2', 'two')
		.replaceAll('3', 'three')
		.replaceAll('4', 'four')
		.replaceAll('5', 'five')
		.replaceAll('6', 'six')
		.replaceAll('7', 'seven')
		.replaceAll('8', 'eight')
		.replaceAll('9', 'nine')
		.replaceAll('0', 'zero')
		.toLowerCase();

export const randomDiscTexture = async (): Promise<Blob | null> => {
	return await new Promise(async (resolve) => {
		const canvas = document.createElement('canvas');
		canvas.width = 16;
		canvas.height = 16;

		const ctx = canvas.getContext('2d')!;

		const replaceInner = '#000000'.replace(/0/g, () => (~~(Math.random() * 16)).toString(16));
		const replaceOuter = '#000000'.replace(/0/g, () => (~~(Math.random() * 16)).toString(16));
		const replaceFill = '#000000'.replace(/0/g, () => (~~(Math.random() * 16)).toString(16));

		const img = new Image();
		img.src = disc_template;

		img.onload = () => {
			ctx.drawImage(img, 0, 0, 16, 16);

			const imgData = ctx.getImageData(0, 0, 16, 16);
			const data = imgData.data;

			for (let i = 0; i < data.length; i += 4) {
				if (data[i] === 0 && data[i + 1] === 0 && data[i + 2] === 0 && data[i + 3] === 0) continue;
				if (data[i] === 53 && data[i + 1] === 53 && data[i + 2] === 53) {
					data[i] = parseInt(replaceOuter.slice(1, 3), 16);
					data[i + 1] = parseInt(replaceOuter.slice(3, 5), 16);
					data[i + 2] = parseInt(replaceOuter.slice(5, 7), 16);
				}
				if (data[i] === 94 && data[i + 1] === 94 && data[i + 2] === 94) {
					data[i] = parseInt(replaceInner.slice(1, 3), 16);
					data[i + 1] = parseInt(replaceInner.slice(3, 5), 16);
					data[i + 2] = parseInt(replaceInner.slice(5, 7), 16);
				}
				if (data[i] === 0 && data[i + 1] === 255 && data[i + 2] === 0) {
					data[i] = parseInt(replaceFill.slice(1, 3), 16);
					data[i + 1] = parseInt(replaceFill.slice(3, 5), 16);
					data[i + 2] = parseInt(replaceFill.slice(5, 7), 16);
				}
			}

			ctx.putImageData(imgData, 0, 0);
			canvas.toBlob((blob) => resolve(blob));
		};
	});
};

export const randomFragmentTexture = async (): Promise<Blob | null> => {
	return await new Promise(async (resolve) => {
		const canvas = document.createElement('canvas');
		canvas.width = 16;
		canvas.height = 16;

		const ctx = canvas.getContext('2d')!;

		const replaceInner = '#000000'.replace(/0/g, () => (~~(Math.random() * 16)).toString(16));
		const replaceOuter = '#000000'.replace(/0/g, () => (~~(Math.random() * 16)).toString(16));
		const replaceFill = '#000000'.replace(/0/g, () => (~~(Math.random() * 16)).toString(16));

		const img = new Image();
		img.src = fragment_template;

		img.onload = () => {
			ctx.drawImage(img, 0, 0, 16, 16);

			const imgData = ctx.getImageData(0, 0, 16, 16);
			const data = imgData.data;

			for (let i = 0; i < data.length; i += 4) {
				if (data[i] === 0 && data[i + 1] === 0 && data[i + 2] === 0 && data[i + 3] === 0) continue;
				if (data[i] === 53 && data[i + 1] === 53 && data[i + 2] === 53) {
					data[i] = parseInt(replaceOuter.slice(1, 3), 16);
					data[i + 1] = parseInt(replaceOuter.slice(3, 5), 16);
					data[i + 2] = parseInt(replaceOuter.slice(5, 7), 16);
				}
				if (data[i] === 94 && data[i + 1] === 94 && data[i + 2] === 94) {
					data[i] = parseInt(replaceInner.slice(1, 3), 16);
					data[i + 1] = parseInt(replaceInner.slice(3, 5), 16);
					data[i + 2] = parseInt(replaceInner.slice(5, 7), 16);
				}
				if (data[i] === 0 && data[i + 1] === 255 && data[i + 2] === 0) {
					data[i] = parseInt(replaceFill.slice(1, 3), 16);
					data[i + 1] = parseInt(replaceFill.slice(3, 5), 16);
					data[i + 2] = parseInt(replaceFill.slice(5, 7), 16);
				}
			}

			ctx.putImageData(imgData, 0, 0);
			canvas.toBlob((blob) => resolve(blob));
		};
	});
};

export const randomTextures = async (): Promise<{ discTexture: Blob; fragmentTexture: Blob }> => {
	const replaceInner = '#000000'.replace(/0/g, () => (~~(Math.random() * 16)).toString(16));
	const replaceOuter = '#000000'.replace(/0/g, () => (~~(Math.random() * 16)).toString(16));
	const replaceFill = '#000000'.replace(/0/g, () => (~~(Math.random() * 16)).toString(16));

	return {
		fragmentTexture: await new Promise(async (resolve) => {
			const canvas = document.createElement('canvas');
			canvas.width = 16;
			canvas.height = 16;

			const ctx = canvas.getContext('2d')!;

			const img = new Image();
			img.src = fragment_template;

			img.onload = () => {
				ctx.drawImage(img, 0, 0, 16, 16);

				const imgData = ctx.getImageData(0, 0, 16, 16);
				const data = imgData.data;

				for (let i = 0; i < data.length; i += 4) {
					if (data[i] === 0 && data[i + 1] === 0 && data[i + 2] === 0 && data[i + 3] === 0)
						continue;
					if (data[i] === 53 && data[i + 1] === 53 && data[i + 2] === 53) {
						data[i] = parseInt(replaceOuter.slice(1, 3), 16);
						data[i + 1] = parseInt(replaceOuter.slice(3, 5), 16);
						data[i + 2] = parseInt(replaceOuter.slice(5, 7), 16);
					}
					if (data[i] === 94 && data[i + 1] === 94 && data[i + 2] === 94) {
						data[i] = parseInt(replaceInner.slice(1, 3), 16);
						data[i + 1] = parseInt(replaceInner.slice(3, 5), 16);
						data[i + 2] = parseInt(replaceInner.slice(5, 7), 16);
					}
					if (data[i] === 0 && data[i + 1] === 255 && data[i + 2] === 0) {
						data[i] = parseInt(replaceFill.slice(1, 3), 16);
						data[i + 1] = parseInt(replaceFill.slice(3, 5), 16);
						data[i + 2] = parseInt(replaceFill.slice(5, 7), 16);
					}
				}

				ctx.putImageData(imgData, 0, 0);
				canvas.toBlob((blob) => resolve(blob!));
			};
		}),
		discTexture: await new Promise(async (resolve) => {
			const canvas = document.createElement('canvas');
			canvas.width = 16;
			canvas.height = 16;

			const ctx = canvas.getContext('2d')!;

			const img = new Image();
			img.src = disc_template;

			img.onload = () => {
				ctx.drawImage(img, 0, 0, 16, 16);

				const imgData = ctx.getImageData(0, 0, 16, 16);
				const data = imgData.data;

				for (let i = 0; i < data.length; i += 4) {
					if (data[i] === 0 && data[i + 1] === 0 && data[i + 2] === 0 && data[i + 3] === 0)
						continue;
					if (data[i] === 53 && data[i + 1] === 53 && data[i + 2] === 53) {
						data[i] = parseInt(replaceOuter.slice(1, 3), 16);
						data[i + 1] = parseInt(replaceOuter.slice(3, 5), 16);
						data[i + 2] = parseInt(replaceOuter.slice(5, 7), 16);
					}
					if (data[i] === 94 && data[i + 1] === 94 && data[i + 2] === 94) {
						data[i] = parseInt(replaceInner.slice(1, 3), 16);
						data[i + 1] = parseInt(replaceInner.slice(3, 5), 16);
						data[i + 2] = parseInt(replaceInner.slice(5, 7), 16);
					}
					if (data[i] === 0 && data[i + 1] === 255 && data[i + 2] === 0) {
						data[i] = parseInt(replaceFill.slice(1, 3), 16);
						data[i + 1] = parseInt(replaceFill.slice(3, 5), 16);
						data[i + 2] = parseInt(replaceFill.slice(5, 7), 16);
					}
				}

				ctx.putImageData(imgData, 0, 0);
				canvas.toBlob((blob) => resolve(blob!));
			};
		})
	};
};
