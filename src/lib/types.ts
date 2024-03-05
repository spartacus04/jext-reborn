export interface ConfigNode<T> {
	name: string,
	id: string,
	description: string,
	value: T,
	defaultValue: T,
	enumValues?: string[] | string
}

export interface Disc {
	name: string,
	author: string,
	duration: number,
	"disc-namespace": string,
	"model-data": number,
	"creeper-drop": boolean,
	lores: string[],
	"loot-tables": { [key: string]: number },
	"fragment-loot-tables": { [key: string]: number },
	packData: ResourcePackData|null,
	uploadData: UploadData|null
}

export interface ResourcePackData {
	track: Blob,
	texture: Blob,
	fragmentTexture: Blob,
}

export interface UploadData {
	uploadedTrack: Blob,
	uploadedTexture: Blob,
	uploadedFragmentTexture: Blob,
	mono: boolean,
	normalize: boolean,
	quality: 'none' | 'low' | 'medium' | 'high',
}