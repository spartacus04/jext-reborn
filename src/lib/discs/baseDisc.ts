import { get } from 'svelte/store';
import { randomTextures } from './textures';
import { discsStore } from './discManager';

export abstract class BaseDisc {
	public isNew: boolean = true;

	public title: string = '';
	public author: string = 'Unknown artist';

	public namespace: string = '';

	public discTexture: Blob = new Blob();
	public fragmentTexture: Blob = new Blob();

	public discTextureURL: string = '';
	public fragmentTextureURL: string = '';

	public colorOuter: string = '';
	public colorInner: string = '';
	public colorFill: string = '';

	public customTemplate: any = null;
	public templateInputs: Record<string, string> = {};

	public tooltip: string = '';

	public modelData: number = -1;

	public creeperDroppable: boolean = true;
	public discLootTables: { [key: string]: number } = {};
	public fragmentLootTables: { [key: string]: number } = {};

	public randomizeDiscData(): void {
		this.creeperDroppable = Math.random() < 0.5;
	}

	async RerollTextures() {
		const result = await randomTextures();
		this.colorOuter = result.colorOuter;
		this.colorInner = result.colorInner;
		this.colorFill = result.colorFill;

		this.setDiscTexture(result.discTexture);
		this.setFragmentTexture(result.fragmentTexture);
	}

	setDiscTexture(blob: Blob) {
		URL.revokeObjectURL(this.discTextureURL);
		this.discTexture = blob;
		this.discTextureURL = URL.createObjectURL(blob);
	}

	setFragmentTexture(blob: Blob) {
		URL.revokeObjectURL(this.fragmentTextureURL);
		this.fragmentTexture = blob;
		this.fragmentTextureURL = URL.createObjectURL(blob);
	}

	refreshTextures() {
		this.setDiscTexture(this.discTexture);
		this.setFragmentTexture(this.fragmentTexture);
	}

	autoSetNamespace() {
		if (!this.isNew) return;

		this.namespace = `${this.title}${this.author}`
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

		const count = get(discsStore).filter((disc) => disc.namespace.includes(this.namespace)).length;

		if (count > 0) {
			this.namespace = `${this.namespace}${count}`
				.replaceAll('1', 'one')
				.replaceAll('2', 'two')
				.replaceAll('3', 'three')
				.replaceAll('4', 'four')
				.replaceAll('5', 'five')
				.replaceAll('6', 'six')
				.replaceAll('7', 'seven')
				.replaceAll('8', 'eight')
				.replaceAll('9', 'nine')
				.replaceAll('0', 'zero');
		}

		while (get(discsStore).filter((disc) => disc.namespace == this.namespace).length > 1) {
			this.namespace = `${this.namespace}zero`;
		}
	}
}
