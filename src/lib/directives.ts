export const dropFile = (
	e: HTMLElement,
	params: { accept: string; cb: ((files?: File[]) => void) | null; multiple?: boolean } = {
		accept: '',
		cb: null,
		multiple: false
	}
) => {
	const { accept, cb, multiple } = params;

	const backgroundColor = e.style.backgroundColor;

	const mouseEnter = () => {
		e.dispatchEvent(new CustomEvent('mouseenter'));

		e.style.backgroundColor = '#404040';
	};

	const mouseLeave = () => {
		e.dispatchEvent(new CustomEvent('mouseleave'));

		e.style.backgroundColor = backgroundColor;
	};

	const dragover = (ev: DragEvent) => {
		ev.preventDefault();
	};

	const drag = (ev: DragEvent) => {
		const list: File[] = [];

		ev.preventDefault();

		if (ev.dataTransfer!.items && ev.dataTransfer!.items.length > 0) {
			[...ev.dataTransfer!.items].forEach((item) => {
				if (item.kind === 'file') {
					const file = item.getAsFile()!;

					if (accept.startsWith('.')) {
						if (file.name.endsWith(accept)) {
							list.push(file);
						}
					} else if (file.type.match(accept)) {
						list.push(file);
					}
				}
			});
		} else {
			[...ev.dataTransfer!.files].forEach((file) => {
				if (accept.startsWith('.')) {
					if (file.name.endsWith(accept)) {
						list.push(file);
					}
				} else if (file.type.match(accept)) {
					list.push(file);
				}
			});
		}

        if(cb) {
            if (multiple) {
                cb(list);
            } else {
                cb([list[0]]);
            }
        }
	};

	e.addEventListener('dragenter', mouseEnter);
	e.addEventListener('dragleave', mouseLeave);
	e.addEventListener('dragover', dragover);
	e.addEventListener('drop', drag);

	return {
		destroy() {
			e.removeEventListener('mouseenter', mouseEnter);
			e.removeEventListener('mouseleave', mouseLeave);
			e.removeEventListener('dragover', dragover);
			e.removeEventListener('drop', drag);
		}
	};
};

export const inputFile = (
	e: HTMLElement,
	params: { accept: string; cb: ((files?: File[]) => void) | null; multiple?: boolean } = {
		accept: '',
		cb: null,
		multiple: false
	}
) => {
	const { accept, cb, multiple } = params;

	const click = () => {
		const input = document.createElement('input');
		input.type = 'file';
		input.accept = accept;
		if (multiple) input.setAttribute('multiple', '');

		input.addEventListener('change', () => {
            if(cb) {
                cb(Array.from(input.files!))
            }
		});

		input.click();
	};

	e.addEventListener('click', click);

	return {
		destroy() {
			e.removeEventListener('click', click);
		}
	};
};