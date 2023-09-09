import { dropFile } from './dropfile';

export const inputFile = (e: HTMLElement, params : { accept: string, cb: (files ?: File[]) => void, multiple? : boolean, drag: boolean } = { accept: '', cb: null, multiple: false, drag: false }) => {
	const { accept, cb, multiple, drag } = params;

	const click = () => {
		const input = document.createElement('input');
		input.type = 'file';
		input.accept = accept;
		if (multiple) input.setAttribute('multiple', '');

		input.addEventListener('change', () => {
			cb(Array.from(input.files));
		});

		input.click();
	};

	e.addEventListener('click', click);

	if(drag) {
		const { destroy } = dropFile(e, {
			accept,
			cb,
			multiple,
		});

		return {
			destroy() {
				e.removeEventListener('click', click);
				destroy();
			},
		};
	}
	else {
		return {
			destroy() {
				e.removeEventListener('click', click);
			},
		};
	}
};