export const inputFile = (e: HTMLElement, params : { accept: string, cb: (files ?: FileList) => void }) => {
	const { accept, cb } = params;

	e.addEventListener('click', () => {
		const input = document.createElement('input');
		input.type = 'file';
		input.accept = accept;

		input.addEventListener('change', () => {
			cb(input.files);
		});

		input.click();
	});
};