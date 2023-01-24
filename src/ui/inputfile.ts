export const inputFile = (e: HTMLElement, params : { accept: string, cb: (files ?: FileList) => void, multiple? : boolean } = { accept: '', cb: null, multiple: false }) => {
	const { accept, cb, multiple } = params;

	e.addEventListener('click', () => {
		const input = document.createElement('input');
		input.type = 'file';
		input.accept = accept;
		if (multiple) input.setAttribute('multiple', '');

		input.addEventListener('change', () => {
			cb(input.files);
		});

		input.click();
	});
};