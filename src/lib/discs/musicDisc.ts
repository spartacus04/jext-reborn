import { BaseDisc } from "./baseDisc";

export class MusicDisc extends BaseDisc {
    private audioFile: Blob = new Blob();
    private cachedFinalAudioFile: Blob|null = null;

    public monoChannel: boolean = true;
    public normalizeVolume: boolean = true;
    public qualityPreset: 'none' | 'low' | 'medium' | 'high' = 'none';

    constructor(file: File) {
        super();

        this.audioFile = file;

        const splitName = file.name.replace(/\.[^.]+$/g, '').split(/ ?- ?/g);

		this.title = file.name;

        if (splitName.length >= 1) this.title = splitName.shift()!;
        if (splitName.length >= 1) this.author = splitName.join(' - ');

        this.autoSetNamespace();
    } 
}