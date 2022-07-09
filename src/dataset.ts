import { convertToOgg } from './utils';

export const data : Disc[] = [];

export class Disc {
	public convertedFile: Blob|null = null;
	public name: string;
	public author: string;
	public lores: string[];
	public texture: Blob|null = null;
	public id = 0;
	public namespace: string;

	reference: HTMLDivElement|null = null;

	constructor(musicFile: File) {
		this.name = musicFile.name.replace(/(\.mp3)|(\.ogg)|(\.wav)/g, '');
		this.author = 'Disc author';
		this.lores = [
			'This is the lore of the disc',
			'',
			'You can have multiple lines.',
			'',
			'If you dont want any lore you can leave this empty.',
		];

		this.namespace = this.createNamespace();

		(async () => {
			this.convertedFile = await convertToOgg(musicFile);
			this.texture = await (await fetch('./default_disk.png')).blob();
			this.generateUI();

			data.push(this);
			this.id = data.length;
		})();
	}

	public generateUI = () => {
		const element = document.createElement('div');
		element.classList.add('song');
		element.innerHTML = this.regenHtml();

		this.reference = document.querySelector('#songscontainer')!.appendChild(element)!;

		this.reference.querySelector('img')!.addEventListener('click', () => {
			// input image
			const input = document.createElement('input');

			input.type = 'file';

			input.accept = 'image/*';

			input.addEventListener('change', () => {
				const file = input.files![0];
				if (file) {
					this.texture = file;
					this.reference!.querySelector('img')!.src = URL.createObjectURL(file);
				}
			});

			input.click();
		});

		this.reference.querySelector('#song_name_input')!.addEventListener('input', () => {
			this.name = (<HTMLInputElement>this.reference!.querySelector('#song_name_input')!).value;
			if(this.name.trim().length === 0) {
				this.name = 'Untitled';
			}
			this.reference!.querySelector('#disknamespace')!.innerHTML = this.createNamespace();
		});

		this.reference.querySelector('#song_author_input')!.addEventListener('input', () => {
			this.author = (<HTMLInputElement>this.reference!.querySelector('#song_author_input')!).value;
			this.reference!.querySelector('#disknamespace')!.innerHTML = this.createNamespace();
		});

		this.reference.querySelector('#song_lore_input')!.addEventListener('input', () => {
			this.lores = (<HTMLInputElement>this.reference!.querySelector('#song_lore_input')!).value.split('\n');
		});

		this.reference.querySelector('#song_delete')!.addEventListener('click', () => {
			this.delete();
		});
	};

	public regenHtml = () : string => {
		return `
			<div style="display: flex; align-items: center;">
				<img src="${URL.createObjectURL(this.texture as Blob)}" height="64" width="64">
				<button id="song_delete"></button>
			</div>
			<div class="flex" id="name">
				<input type="text" name="song_name" id="song_name_input" value="${this.name}">
				<input type="text" name="song_author" id="song_author_input" value="${this.author}">
				<p id="disknamespace">${this.createNamespace()}</p>
			</div>
			<textarea name="song_lore" id="song_lore_input" cols="30" rows="6">${this.lores.join('&#10')}</textarea>
		`;
	};

	public createNamespace = () : string => {
		let namespace = `${this.name}${this.author}`
			.replace(/[^a-zA-Z0-9]/g, '')
			.replace('1', 'one')
			.replace('2', 'two')
			.replace('3', 'three')
			.replace('4', 'four')
			.replace('5', 'five')
			.replace('6', 'six')
			.replace('7', 'seven')
			.replace('8', 'eight')
			.replace('9', 'nine')
			.replace('0', 'zero')
			.toLowerCase();

		while(data.filter(e => e.id != this.id).find(d => d.namespace === namespace)) {
			namespace += 'clone';
		}

		this.namespace = namespace;

		return namespace;
	};

	public delete = () => {
		data.splice(this.id, 1);
		this.reference!.remove();
	};
}