import { open } from '@tauri-apps/plugin-dialog';
import { isTauri } from './state';
import { exists, readFile } from '@tauri-apps/plugin-fs';

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
			// @ts-expect-error This is fine
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
			// @ts-expect-error This is fine
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

		if (cb) {
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

	const click = async () => {
		if (isTauri) {
			let filePaths = (await open({
				multiple: multiple,
				directory: false,
				title: 'Select a file',
				filters: [{ name: 'Files', extensions: accept == 'image/*' ? ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'tiff'] : accept.replace(/\./g, '').split(',') }]
			})) as string | string[] | null;

			if (filePaths == null) return;
			else if (typeof filePaths === 'string') filePaths = [filePaths];

			const files = await Promise.all(
				filePaths.map(async (path) => {
					if (!(await exists(path))) return null;
					const contents = await readFile(path);

					const file = new File([contents], path.split('/').pop()!);
					// @ts-expect-error attach tauri path for ffprobe
					file.tauriPath = path;

					return file;
				})
			);

			if (cb) {
				cb(files.filter((file) => file !== null));
			}
		} else {
			const input = document.createElement('input');
			input.type = 'file';
			input.accept = accept;
			if (multiple) input.setAttribute('multiple', '');

			input.addEventListener('change', () => {
				if (cb) {
					cb(Array.from(input.files!));
				}
			});

			input.click();
		}
	};

	e.addEventListener('click', click);

	return {
		destroy() {
			e.removeEventListener('click', click);
		}
	};
};
