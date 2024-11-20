import { get } from "svelte/store";
import { AlertModal, ConfirmModal } from "./components/modals";
import { baseElement, isTauri } from "./state";
import { save } from "@tauri-apps/plugin-dialog";
import { writeFile } from "@tauri-apps/plugin-fs";

export const cConfirm = async (options: {
    text: string,
    confirmText: string,
    cancelText: string|undefined,
    discardText: string,
}) => {
    return await new Promise<'discard' | 'confirm' | 'cancel'>((resolve) => {
        const confirmModal = new ConfirmModal({
            target: get(baseElement)!,
            props: {
                ...options,
                onFinish: (result) => {
                    confirmModal.$destroy();
                    resolve(result);
                }
            }
        });

        confirmModal.openModal();
    });
}

export const cAlert = async (text: string, confirmText: string = 'Ok') => {
	return await new Promise<void>((resolve) => {
		const alertModal = new AlertModal({
			target: get(baseElement)!,
			props: {
				text,
				confirmText,
				onFinish: () => {
					alertModal.$destroy();
					resolve();
				},
			},
		});

		alertModal.openModal();
	});
}

export const blobToArraBuffer = (blob: Blob): Promise<ArrayBuffer> => {
	return new Promise((resolve, reject) => {
		const reader = new FileReader();
		reader.onload = () => resolve(reader.result as ArrayBuffer);
		reader.onerror = reject;
		reader.readAsArrayBuffer(blob);
	});
};

export const arrayBufferToBase64 = (buffer: ArrayBuffer) => {
	let binary = '';
	const bytes = new Uint8Array(buffer);
	bytes.forEach((byte) => (binary += String.fromCharCode(byte)));
	return window.btoa(binary);
};

export const base64ToArrayBuffer = (base64: string) => {
	const binary_string = atob(base64);
	const len = binary_string.length;
	const bytes = new Uint8Array(len);
	for (let i = 0; i < len; i++) {
		bytes[i] = binary_string.charCodeAt(i);
	}
	return bytes.buffer;
};

export const saveAs = (blob: Blob | undefined, filename: string) => {
	if(isTauri) {
		save({
			canCreateDirectories: true,
			title: 'Save File',
			filters: [{ name: 'All Files', extensions: [filename.split('.').pop()!] }],
		}).then(async (result) => {
			if(result == null) return;

			const uint8Array = new Uint8Array(await blob!.arrayBuffer());

			writeFile(result, uint8Array);
		});
	} else {
		const link = document.createElement('a');
		link.href = URL.createObjectURL(blob!);
		link.download = filename;
		link.click();
	}
};

export const getVersionFromTime = () => {
	const now = new Date();
	const oneDay = 1000 * 60 * 60 * 24;

	// day of year
	const startDay = new Date(now.getFullYear(), 0, 0);
	const diffDay =
		now.getTime() -
		startDay.getTime() +
		(startDay.getTimezoneOffset() - now.getTimezoneOffset()) * 60 * 1000;
	const day = Math.floor(diffDay / oneDay);

	// seconds of day
	const startSeconds = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0);
	const diffSeconds = now.getTime() - startSeconds.getTime();
	const seconds = Math.floor(diffSeconds / 1000);

	return [now.getFullYear(), day, seconds];
};

export const downloadWithProgress = async (
	url: string | URL,
	cb?: (event: {
		url: string | URL;
		total: number;
		received: number;
		delta: number;
		done: boolean;
	}) => unknown
): Promise<ArrayBuffer> => {
	const resp = await fetch(url);
	let buf;

	try {
		// Set total to -1 to indicate that there is not Content-Type Header.
		const total = parseInt(resp.headers.get('Content-Length') || '-1');

		const reader = resp.body?.getReader();
		if (!reader) throw new Error('failed to get response body reader');

		const chunks = [];
		let received = 0;
		for (;;) {
			const { done, value } = await reader.read();
			const delta = value ? value.length : 0;

			if (done) {
				if (total != -1 && total !== received) throw new Error('failed to complete download');
                if(cb) cb({ url, total, received, delta, done });
				break;
			}

			chunks.push(value);
			received += delta;
            if(cb) cb({ url, total, received, delta, done });
		}

		const data = new Uint8Array(received);
		let position = 0;
		for (const chunk of chunks) {
			data.set(chunk, position);
			position += chunk.length;
		}

		buf = data.buffer;
	} catch (e) {
		console.log('failed to send download progress event: ', e);
		buf = await resp.arrayBuffer();
		if(cb) cb({
            url,
            total: buf.byteLength,
            received: buf.byteLength,
            delta: 0,
            done: true
        });
	}

	return buf;
};

export const getDuration = async (blob: Blob): Promise<number> => {
	return new Promise((resolve) => {
		const audio = document.createElement('audio');

		console.debug('blob', blob);

		audio.src = URL.createObjectURL(blob);

		audio.onloadedmetadata = () => {
			URL.revokeObjectURL(audio.src);
			resolve(Math.ceil(audio.duration));
		};
	});
};

export const mergeDeep = (...objects: any[]) => {
	const isObject = (obj: unknown) => obj && typeof obj == 'object';

	if (Array.isArray(objects[0]) || Array.isArray(objects[1])) {
		return objects.reduce((prev, obj) => {
			if (obj === undefined) return prev;
			return prev.concat(...obj);
		}, []);
	}

	return objects.reduce((prev, obj) => {
		if (obj === undefined) return prev;
		Object.keys(obj).forEach((key) => {
			const pVal = prev[key];
			const oVal = obj[key];

			if (Array.isArray(pVal) && Array.isArray(oVal)) {
				prev[key] = pVal.concat(...oVal);
			} else if (isObject(pVal) && isObject(oVal)) {
				prev[key] = mergeDeep(pVal, oVal);
			} else {
				prev[key] = oVal;
			}
		});

		return prev;
	}, {});
};

export const wait = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms));