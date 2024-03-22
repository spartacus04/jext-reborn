export interface ConfigNode<T> {
	name: string,
	id: string,
	description: string,
	value: T,
	defaultValue: T,
	enumValues?: string[] | string
}

export interface Disc {
	title: string,
	author: string,
	duration: number,
	"disc-namespace": string,
	"model-data": number,
	"creeper-drop": boolean,
	lores: string[],
	"loot-tables": { [key: string]: number },
	"fragment-loot-tables": { [key: string]: number },
	packData: FinalData|null,
	uploadData: UploadData|null
}

export interface FinalData {
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

export interface FFmpegData {
	mono: boolean,
	normalize: boolean,
	quality: 'none' | 'low' | 'medium' | 'high',
}

export interface ResourcePackData {
	icon: Blob,
	version: number,
	description: string,
	packs: {
		icon: Blob,
		name: string,
		value: Blob
	}[]
}