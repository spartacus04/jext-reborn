import { BaseDisc } from './baseDisc';

export class MusicDisc extends BaseDisc {
	public audioFile: Blob = new Blob();
	public cachedFinalAudioFile: Blob | null = null;

	public monoChannel: boolean = true;
	public normalizeVolume: boolean = true;
	public disableTranscoding: boolean = false;
	public qualityPreset: 'none' | 'low' | 'medium' | 'high' = 'none';

	constructor(file: File, isNew: boolean = true) {
		super();

		this.audioFile = file;
		this.isNew = isNew;

		const splitName = file.name.replace(/\.[^.]+$/g, '').split(/ ?- ?/g);

		this.title = file.name;

		if (splitName.length >= 1) {
			const candidateTitle = splitName.shift()!;

			if(candidateTitle.includes('\\')) {
				this.title = candidateTitle.split('\\').pop()!;
			} else {
				this.title = candidateTitle;
			}
		}
		if (splitName.length >= 1) this.author = splitName.join(' - ');

		this.autoSetNamespace();
	}
}
