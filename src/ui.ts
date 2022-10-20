import { Disc } from './dataset';
import { generatePack } from './packgenerator';

export const ui = () => {
	// pack icon
	document.querySelector('#pack_icon')?.addEventListener('click', () => {
		(<HTMLInputElement>document.querySelector('#pack_icon_input')).click();

		document.querySelector('#pack_icon_input')?.addEventListener('change', () => {
			const files = (<HTMLInputElement>document.querySelector('#pack_icon_input')).files;

			if(!files || files.length === 0) return;

			const file = files[0];

			const reader = new FileReader();

			reader.onload = () => {
				const image = new Image();

				image.onload = () => {
					const canvas = document.createElement('canvas');

					canvas.width = 64;
					canvas.height = 64;

					const ctx = canvas.getContext('2d');

                    ctx!.drawImage(image, 0, 0, 64, 64);

                    const dataURL = canvas.toDataURL('image/png');

                    (<HTMLImageElement>document.querySelector('#pack_icon')).src = dataURL;
				};

				image.src = reader.result as string;
			};

			reader.readAsDataURL(file);
		});
	});

	// pack name
	document.querySelector('#pack_name_input')?.addEventListener('input', () => {
		const inputvalue = (<HTMLInputElement>document.querySelector('#pack_name_input')).value;

		(<HTMLInputElement>document.querySelector('#pack_name_input')).value = inputvalue
			.replace(' ', '_')
			.replace(/[^a-zA-Z0-9_]/g, '')
			.toLowerCase();
	});

	// add button
	document.querySelector('#addsongsbtn')?.addEventListener('click', () => {
		// open select file dialog

		const input = document.createElement('input');
		input.type = 'file';
		input.multiple = true;
		input.accept = import.meta.env.PROD ? 'audio/*' : '.ogg';
		input.click();

		input.addEventListener('change', async () => {
			if(!input.files || input.files.length === 0) return;


			for(let i = 0; i < input.files.length; i++) {
				new Disc(input.files[i]);
			}
		});
	});

	document.querySelector('#messagepopupconfirm')!.addEventListener('click', hidepopup);

	(<HTMLSelectElement>document.querySelector('#version_input'))!.addEventListener('change', () => {
		const value = parseInt((<HTMLSelectElement>document.querySelector('#version_input'))!.value);

		const ancientElement = document.querySelector('#ancientelement')!;
		const ruinedElement = document.querySelector('#ruinedelement')!;
		const bastionElement = document.querySelector('#bastionelement')!;


		if(value >= 5 && value <= 8) {
			ruinedElement.classList.remove('none');
			bastionElement.classList.remove('none');
			ancientElement.classList.add('none');
		}
		else if(value >= 9) {
			ruinedElement.classList.remove('none');
			bastionElement.classList.remove('none');
			ancientElement.classList.remove('none');
		}
		else {
			ruinedElement.classList.add('none');
			bastionElement.classList.add('none');
			ancientElement.classList.add('none');
		}
	});

	disableGenButton();
};

// Popup message
const hidepopup = () => {
	(<HTMLElement>document.querySelector('#messagepopup')).style.display = 'none';
	(<HTMLElement>document.querySelector('.popupbackground')).style.opacity = '0%';
	setTimeout(() => {
		(<HTMLElement>document.querySelector('.popupbackground')).style.display = 'none';
	}, 200);

	document.querySelector('.popupbackground')!.removeEventListener('click', hidepopup);
};

const alert = (message: string) => {
	document.querySelector('#messagepopupcontainer')!.innerHTML = message;
	(<HTMLElement>document.querySelector('#messagepopup')).style.display = 'flex';
	(<HTMLElement>document.querySelector('.popupbackground')).style.display = 'block';
	setTimeout(() => {
		(<HTMLElement>document.querySelector('.popupbackground')).style.opacity = '100%';
	}, 1);

	document.querySelector('.popupbackground')!.addEventListener('click', hidepopup);
};

const generatediscalert = () => alert('Add at least a disc');

// Generate button

export const activateGenButton = () => {
	(<HTMLElement>document.querySelector('#generate_button')).style.filter = 'grayscale(0%)';
	document.querySelector('#generate_button')!.addEventListener('click', generatePack);
	document.querySelector('#generate_button')!.removeEventListener('click', generatediscalert);
};

export const disableGenButton = () => {
	(<HTMLElement>document.querySelector('#generate_button')).style.filter = 'grayscale(100%)';
	document.querySelector('#generate_button')!.removeEventListener('click', generatePack);
	document.querySelector('#generate_button')!.addEventListener('click', generatediscalert);
};

// Dungeon popups
const togglevalue = (e: Element) => {
	const attrib = e.attributes.getNamedItem('value')!;
	attrib.value = `${!JSON.parse(attrib.value.toLowerCase())}`;

	e.attributes.setNamedItem(attrib);
};

const dungeonelements = document.querySelectorAll('.dungeonelement');

dungeonelements.forEach(e => e.addEventListener('click', () => togglevalue(e)));

export const openDungeonSelector = async (selected: string[]) : Promise<string[]> => {
	dungeonelements.forEach(e => {
		if(selected.includes(e.attributes.getNamedItem('source')!.value!.split(',')[0])) {
			const attrib = e.attributes.getNamedItem('value')!;
			attrib.value = 'true';

			e.attributes.setNamedItem(attrib);
		}
	});

	(<HTMLElement>document.querySelector('#dungeonpopup')).style.display = 'flex';
	(<HTMLElement>document.querySelector('.popupbackground')).style.display = 'block';
	setTimeout(() => {
		(<HTMLElement>document.querySelector('.popupbackground')).style.opacity = '100%';
	}, 1);

	return await new Promise<string[]>(resolve => {
		const handleclosedungeon = () => {
			(<HTMLElement>document.querySelector('#dungeonpopup')).style.display = 'none';
			(<HTMLElement>document.querySelector('.popupbackground')).style.opacity = '0%';

			setTimeout(() => {
				(<HTMLElement>document.querySelector('.popupbackground')).style.display = 'none';
			}, 200);

			document.querySelector('#dungeonpopupconfirm')!.removeEventListener('click', handleclosedungeon);
			document.querySelector('.popupbackground')!.removeEventListener('click', handleclosedungeon);

			const values : string[] = [];

			dungeonelements.forEach(e => {
				const attrib = e.attributes.getNamedItem('value')!;

				if((<boolean>JSON.parse(attrib.value.toLowerCase()))) {
					e.attributes.getNamedItem('source')!.value.split(',').forEach(el => values.push(el));
				}
			});

			resolve(values);

		};

		document.querySelector('#dungeonpopupconfirm')!.addEventListener('click', handleclosedungeon);
		document.querySelector('.popupbackground')!.addEventListener('click', handleclosedungeon);
	});
};