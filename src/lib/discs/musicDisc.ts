import { BaseDisc } from './baseDisc';

export class MusicDisc extends BaseDisc {
	public audioFile: Blob = new Blob();
	public cachedFinalAudioFile: Blob | null = null;

	public monoChannel: boolean = true;
	public normalizeVolume: boolean = true;
	public qualityPreset: 'none' | 'low' | 'medium' | 'high' = 'none';

	constructor(file: File, isNew: boolean = true) {
		super();

		this.audioFile = file;
		this.isNew = isNew;

		const splitName = file.name.replace(/\.[^.]+$/g, '').split(/ ?- ?/g);

		this.title = file.name;

		if (splitName.length >= 1) this.title = splitName.shift()!;
		if (splitName.length >= 1) this.author = splitName.join(' - ');

		this.autoSetNamespace();
	}
}
