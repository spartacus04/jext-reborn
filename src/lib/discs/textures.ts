import { disc_template, fragment_template } from '$lib/assets';

export const randomColors = () => {
	const pickRandomHex = () =>
		'#' +
		Array.from({ length: 6 }, () => (~~(Math.random() * 16)).toString(16)).join('');
	return {
		colorOuter: pickRandomHex(),
		colorInner: pickRandomHex(),
		colorFill: pickRandomHex()
	};
};

export const generateTexturesFromColors = async (
	colorOuter: string,
	colorInner: string,
	colorFill: string
): Promise<{ discTexture: Blob; fragmentTexture: Blob }> => {
	const parseColor = (hex: string) => {
		const r = parseInt(hex.slice(1, 3), 16);
		const g = parseInt(hex.slice(3, 5), 16);
		const b = parseInt(hex.slice(5, 7), 16);
		return { r, g, b };
	};

	const outer = parseColor(colorOuter);
	const inner = parseColor(colorInner);
	const fill = parseColor(colorFill);

	const processTemplate = async (templateSrc: string): Promise<Blob> => {
		return new Promise((resolve) => {
			const canvas = document.createElement('canvas');
			canvas.width = 16;
			canvas.height = 16;
			const ctx = canvas.getContext('2d')!;

			const img = new Image();
			img.src = templateSrc;
			img.onload = () => {
				ctx.drawImage(img, 0, 0, 16, 16);
				const imgData = ctx.getImageData(0, 0, 16, 16);
				const data = imgData.data;

				for (let i = 0; i < data.length; i += 4) {
					if (data[i] === 0 && data[i + 1] === 0 && data[i + 2] === 0 && data[i + 3] === 0)
						continue;
					if (data[i] === 53 && data[i + 1] === 53 && data[i + 2] === 53) {
						data[i] = outer.r;
						data[i + 1] = outer.g;
						data[i + 2] = outer.b;
					}
					if (data[i] === 94 && data[i + 1] === 94 && data[i + 2] === 94) {
						data[i] = inner.r;
						data[i + 1] = inner.g;
						data[i + 2] = inner.b;
					}
					if (data[i] === 0 && data[i + 1] === 255 && data[i + 2] === 0) {
						data[i] = fill.r;
						data[i + 1] = fill.g;
						data[i + 2] = fill.b;
					}
				}

				ctx.putImageData(imgData, 0, 0);
				canvas.toBlob((blob) => resolve(blob!));
			};
		});
	};

	return {
		discTexture: await processTemplate(disc_template),
		fragmentTexture: await processTemplate(fragment_template)
	};
};

export const randomTextures = async (): Promise<{
	discTexture: Blob;
	fragmentTexture: Blob;
	colorOuter: string;
	colorInner: string;
	colorFill: string;
}> => {
	const colors = randomColors();
	const textures = await generateTexturesFromColors(colors.colorOuter, colors.colorInner, colors.colorFill);
	return {
		...textures,
		...colors
	};
};

export const adaptImageToDisc = async (
	image: Blob
): Promise<{ discTexture: Blob; fragmentTexture: Blob }> => {
	// scale image to 16x16
	const getImageData = async (image: Blob): Promise<Uint8ClampedArray> => {
		return await new Promise<Uint8ClampedArray>((resolve) => {
			const canvas = document.createElement('canvas');
			canvas.width = 16;
			canvas.height = 16;

			const ctx = canvas.getContext('2d')!;

			const img = new Image();
			img.src = URL.createObjectURL(image);

			img.onload = () => {
				ctx.drawImage(img, 0, 0, 16, 16);

				const imgData = ctx.getImageData(0, 0, 16, 16);

				resolve(imgData.data);
			};
		});
	};

	const thumbData = await getImageData(image);

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

					if (data[i] === 0 && data[i + 1] === 255 && data[i + 2] === 0) {
						data[i] = thumbData[i];
						data[i + 1] = thumbData[i + 1];
						data[i + 2] = thumbData[i + 2];
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

					if (data[i] === 0 && data[i + 1] === 255 && data[i + 2] === 0) {
						data[i] = thumbData[i];
						data[i + 1] = thumbData[i + 1];
						data[i + 2] = thumbData[i + 2];
					}
				}

				ctx.putImageData(imgData, 0, 0);
				canvas.toBlob((blob) => resolve(blob!));
			};
		})
	};
};

export const generateTexturesFromTemplate = async (
	template: any,
	inputs: { [inputId: string]: string }
): Promise<{ discTexture: Blob; fragmentTexture: Blob }> => {
	const parseColor = (hex: string) => {
		const clean = hex.startsWith('#') ? hex.slice(1) : hex;
		const r = parseInt(clean.slice(0, 2), 16);
		const g = parseInt(clean.slice(2, 4), 16);
		const b = parseInt(clean.slice(4, 6), 16);
		return { r, g, b };
	};

	const parsedInputs: Record<string, { r: number; g: number; b: number }> = {};
	for (const key of Object.keys(inputs)) {
		parsedInputs[key] = parseColor(inputs[key]);
	}

	const resolveReplacementColor = (ruleAfter: any) => {
		if (typeof ruleAfter === 'string') {
			return parsedInputs[ruleAfter] || { r: 0, g: 0, b: 0 };
		} else if (Array.isArray(ruleAfter)) {
			let rSum = 0, gSum = 0, bSum = 0, weightSum = 0;
			for (const item of ruleAfter) {
				const weight = item.contribution || 0;
				let color = { r: 0, g: 0, b: 0 };
				if (item.input) {
					color = parsedInputs[item.input] || { r: 0, g: 0, b: 0 };
				} else if (item.color) {
					color = parseColor(item.color);
				}
				rSum += color.r * weight;
				gSum += color.g * weight;
				bSum += color.b * weight;
				weightSum += weight;
			}
			if (weightSum > 0) {
				return {
					r: Math.round(rSum / weightSum),
					g: Math.round(gSum / weightSum),
					b: Math.round(bSum / weightSum)
				};
			}
		}
		return { r: 0, g: 0, b: 0 };
	};

	const rulesMap = template.replace.map((rule: any) => {
		const beforeColor = parseColor(rule.before);
		const afterColorResolver = () => resolveReplacementColor(rule.after);
		return { beforeColor, afterColorResolver };
	});

	const processImage = async (imgSrc: string): Promise<Blob> => {
		return new Promise((resolve) => {
			const canvas = document.createElement('canvas');
			canvas.width = 16;
			canvas.height = 16;
			const ctx = canvas.getContext('2d')!;
			const img = new Image();
			img.src = imgSrc;
			img.onload = () => {
				ctx.drawImage(img, 0, 0, 16, 16);
				const imgData = ctx.getImageData(0, 0, 16, 16);
				const data = imgData.data;

				for (let i = 0; i < data.length; i += 4) {
					if (data[i + 3] === 0) continue;

					for (const rule of rulesMap) {
						if (
							data[i] === rule.beforeColor.r &&
							data[i + 1] === rule.beforeColor.g &&
							data[i + 2] === rule.beforeColor.b
						) {
							const replaced = rule.afterColorResolver();
							data[i] = replaced.r;
							data[i + 1] = replaced.g;
							data[i + 2] = replaced.b;
							break;
						}
					}
				}

				ctx.putImageData(imgData, 0, 0);
				canvas.toBlob((blob) => resolve(blob!));
			};
		});
	};

	const discTexture = await processImage(template.base);

	let fragmentTexture: Blob;
	if (template.fragmentBase) {
		fragmentTexture = await processImage(template.fragmentBase);
	} else {
		const inputIds = Object.keys(inputs);
		const outerHex = inputs[inputIds[0]] || '#353535';
		const innerHex = inputs[inputIds[1]] || '#5e5e5e';
		const fillHex = inputs[inputIds[2]] || '#00ff00';
		const defaultFragment = await generateTexturesFromColors(outerHex, innerHex, fillHex);
		fragmentTexture = defaultFragment.fragmentTexture;
	}

	return { discTexture, fragmentTexture };
};
