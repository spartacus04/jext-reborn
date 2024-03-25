import type { Disc } from '$lib/types';
import Ajv from 'ajv';
import JSZip from 'jszip';

export const RPChecker = async (blob: Blob): Promise<'JextRP' | 'RP' | 'NotValid'> => {
	const zip = await JSZip.loadAsync(blob);

	if (zip.file('pack.mcmeta') !== null) {
		if (zip.file('jext.json') !== null) {
			return 'JextRP';
		}

		return 'RP';
	}

	return 'NotValid';
};

const newSchema = {
	type: 'array',
	items: {
		type: 'object',
		properties: {
			title: {
				type: 'string'
			},
			author: {
				type: 'string'
			},
			duration: {
				type: 'integer'
			},
			'disc-namespace': {
				type: 'string'
			},
			'model-data': {
				type: 'integer'
			},
			'creeper-drop': {
				type: 'boolean'
			},
			lores: {
				type: 'array',
				items: {
					type: 'string'
				}
			},
			'loot-tables': {
				type: 'object',
				additionalProperties: {
					type: 'integer'
				}
			},
			'fragment-loot-tables': {
				type: 'object',
				additionalProperties: {
					type: 'integer'
				}
			}
		},
		required: ['title', 'author', 'disc-namespace', 'model-data', 'creeper-drop', 'lores']
	}
};

const oldSchema = {
	type: 'array',
	items: {
		type: 'object',
		properties: {
			title: {
				type: 'string'
			},
			author: {
				type: 'string'
			},
			duration: {
				type: 'integer'
			},
			'disc-namespace': {
				type: 'string'
			},
			'model-data': {
				type: 'integer'
			},
			'creeper-drop': {
				type: 'boolean'
			},
			lores: {
				type: 'array',
				items: {
					type: 'string'
				}
			},
			'loot-tables': {
				type: 'array',
				items: {
					type: 'string'
				}
			},
			'fragment-loot-tables': {
				type: 'array',
				items: {
					type: 'string'
				}
			}
		},
		required: ['title', 'author', 'disc-namespace', 'model-data', 'creeper-drop', 'lores']
	}
};

export const JextFileChecker = async (blob: Blob): Promise<'Valid' | 'Old' | 'NotValid'> => {
	const text = await blob.text();

	const validate = new Ajv().compile(newSchema);

	if (validate(JSON.parse(text))) {
		return 'Valid';
	}

	const oldValidate = new Ajv().compile(oldSchema);

	if (oldValidate(JSON.parse(text))) {
		return 'Old';
	}

	return 'NotValid';
};

export const JextReader = async (blob: Blob): Promise<Disc[]> => {
	const zip = await JSZip.loadAsync(blob);

	const text = await zip.file('jext.json')!.async('text');

	const oldValidate = new Ajv().compile(oldSchema);
	const obj = JSON.parse(text) as Disc[];

	if (oldValidate(obj)) {
		obj.forEach((element) => {
			element['fragment-loot-tables'] = {};
			element['loot-tables'] = {};
		});
	}

	return obj;
};

export const processImage = async (blob: Blob) => {
	return await new Promise<Blob>((resolve) => {
		const img = new Image();
		img.src = URL.createObjectURL(blob);

		const canvas = document.createElement('canvas');
		const ctx = canvas.getContext('2d')!;

		img.onload = () => {
			const size = Math.min(img.width, img.height);

			canvas.width = size;
			canvas.height = size;

			ctx.drawImage(img, 0, 0, size, size, 0, 0, size, size);

			canvas.toBlob((blob) => {
				resolve(blob!);
			}, 'image/png');
		};
	});
};

export const getDuration = async (blob: Blob): Promise<number> => {
	return new Promise((resolve) => {
		const audio = document.createElement('audio');

		audio.src = URL.createObjectURL(blob);

		audio.onloadedmetadata = () => {
			URL.revokeObjectURL(audio.src);
			resolve(Math.ceil(audio.duration));
		};
	});
};
