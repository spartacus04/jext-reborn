export const dropFile = (e: HTMLElement, params : { accept: string, cb: (files ?: File[]) => void, multiple? : boolean } = { accept: '', cb: null, multiple: false }) => {
	const { accept, cb, multiple } = params;

	const backgroundColor = e.style.backgroundColor;

	const mouseEnter = () => {
		e.dispatchEvent(new CustomEvent('mouseenter'));

		if(e.tagName == 'IMG') {
			e.style.backgroundColor = '#fff';
		}
		else {
			e.style.backgroundColor = '#404040';
		}
	};

	const mouseLeave = () => {
		e.dispatchEvent(new CustomEvent('mouseleave'));

		e.style.backgroundColor = backgroundColor;
	};

	const dragover = (ev : DragEvent) => {
		ev.preventDefault();
	};

	const drop = (ev : DragEvent) => {
		const list : File[] = [];

		ev.preventDefault();

		if(ev.dataTransfer.items && ev.dataTransfer.items.length > 0) {
			[...ev.dataTransfer.items].forEach((item) => {
				if (item.kind === 'file') {
					const file = item.getAsFile();

					if(accept.startsWith('.')) {
						if(file.name.endsWith(accept)) {
							list.push(file);
						}
					}
					else if (file.type.match(accept)) {
						list.push(file);
					}
				}
			});
		}
		else {
			[...ev.dataTransfer.files].forEach((file) => {
				if(accept.startsWith('.')) {
					if(file.name.endsWith(accept)) {
						list.push(file);
					}
				}
				else if (file.type.match(accept)) {
					list.push(file);
				}
			});
		}

		if (multiple) {
			cb(list);
		}
		else {
			cb([list[0]]);
		}

		e.style.backgroundColor = backgroundColor;
	};

	e.addEventListener('dragenter', mouseEnter);
	e.addEventListener('dragleave', mouseLeave);
	e.addEventListener('dragover', dragover);
	e.addEventListener('drop', drop);


	return {
		destroy() {
			e.removeEventListener('dragenter', mouseEnter);
			e.removeEventListener('dragleave', mouseLeave);
			e.removeEventListener('dragover', dragover);
			e.removeEventListener('drop', drop);
		},
	};
};