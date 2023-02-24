export const inputFile = (e: HTMLElement, params : { accept: string, cb: (files ?: File[]) => void, multiple? : boolean } = { accept: '', cb: null, multiple: false }) => {
	const { accept, cb, multiple } = params;

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


	return {
		destroy() {
			e.removeEventListener('click', click);
		},
	};
};