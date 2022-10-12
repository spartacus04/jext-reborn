import { Disc } from './dataset';
import { generatePack } from './packgenerator';

const hidepopup = () => {
	(<HTMLElement>document.querySelector('#messagepopup')).style.display = 'none';
	(<HTMLElement>document.querySelector('.popupbackground')).style.opacity = '0%';
	setTimeout(() => {
		(<HTMLElement>document.querySelector('.popupbackground')).style.display = 'none';
	}, 200);
};

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
	document.querySelector('.popupbackground')!.addEventListener('click', hidepopup);

	disableGenButton();
};

const alert = (message: string) => {
	document.querySelector('#messagepopupcontainer')!.innerHTML = message;
	(<HTMLElement>document.querySelector('#messagepopup')).style.display = 'flex';
	(<HTMLElement>document.querySelector('.popupbackground')).style.display = 'block';
	setTimeout(() => {
		(<HTMLElement>document.querySelector('.popupbackground')).style.opacity = '100%';
	}, 1);
};

const generatediscalert = () => alert('Add at least a disc');

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
