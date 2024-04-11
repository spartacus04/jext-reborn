import {
	getModalStore,
	localStorageStore,
	type ModalComponent,
	type ModalStore,
	type ToastStore
} from '@skeletonlabs/skeleton';
import { get } from 'svelte/store';

export const LoginStore = localStorageStore<{ ip: string; token: string } | null>('login', null);

export const login = async (
	modalStore: ModalStore,
	toastStore: ToastStore,
	ErrorPopup: any,
	data: { ip: string | undefined | null; port: number | undefined | null }
): Promise<boolean> => {
	const ip =
		data.ip ??
		(await new Promise<string>((resolve) => {
			modalStore.trigger({
				type: 'prompt',
				title: 'Connect to jext server',
				body: 'Please enter the IP address of the jext server you want to connect to.',
				value: '127.0.0.1',
				valueAttr: { type: 'text', required: true },
				response: (value) => resolve(value)
			});
		}));

	if (!ip) return false;

	const port =
		data.port ??
		(await new Promise<number>((resolve) => {
			modalStore.trigger({
				type: 'prompt',
				title: 'Connect to jext server',
				body: 'Please enter the port of the jext server you want to connect to.',
				value: 9871,
				valueAttr: { type: 'number', required: true },
				response: (value) => resolve(value)
			});
		}));

	if (!port) return false;

	const password = await new Promise<string>((resolve) => {
		modalStore.trigger({
			type: 'prompt',
			title: 'Connect to jext server',
			body: 'Please enter the password of the jext server you want to connect to.',
			value: '',
			valueAttr: { type: 'password', required: false },
			response: (value) => resolve(value)
		});
	});

	if (password == null) return false;

	const response = await fetch(`http://${ip}:${port}/connect`, {
		method: 'POST',
		headers: { 'Content-Type': 'text/plain' },
		body: password
	}).catch(async (e: any) => {
		console.error(e);
		toastStore.trigger({
			message: 'Error connecting to jext server',
			timeout: 5000,
			background: 'bg-red-500',
			action: {
				label: 'See details',
				response: async () => {
					const modalComponent: ModalComponent = {
						ref: ErrorPopup,
						props: { error: e }
					};

					modalStore.trigger({
						type: 'component',
						title: 'Error connecting to jext server',
						component: modalComponent
					});
				}
			}
		});
		return null;
	});

	if (response == null) return false;

	const result = await response.text();

	switch (response.status) {
		case 200:
			LoginStore.set({ ip: `${ip}:${port}`, token: result });
			return true;
		default:
			modalStore.trigger({
				type: 'alert',

				title: 'Error connecting to jext server',
				body: result ?? 'Unknown error'
			});
			return false;
	}
};

export const isLoggedIn = (): boolean => {
	const login = get(LoginStore);
	return login !== null;
};

export const fetchAuthed = async (url: string, options: RequestInit = {}): Promise<Response> => {
	const login = get(LoginStore);

	if (login == null) throw new Error('Not logged in');

	const response = await fetch(`http://${login.ip}/${url}`, {
		...options,
		headers: {
			...options.headers,
			Authorization: `Bearer ${login.token}`
		}
	});

	if (response.status === 401) {
		LoginStore.set(null);
		window.location.reload();
		throw new Error('Not logged in');
	}

	return response;
};

export const healthCheck = async (): Promise<boolean> => {
	try {
		const response = await fetchAuthed('health');
		return response.status === 200;
	} catch (e) {
		return false;
	}
};

export const logout = async (force: boolean): Promise<void> => {
	if (force) {
		fetchAuthed('disconnect', {
			method: 'POST'
		}).catch(null);

		LoginStore.set(null);

		return window.location.reload();
	}

	const login = get(LoginStore);

	if (login == null) return;

	await fetchAuthed('disconnect', {
		method: 'POST'
	}).catch(async (e: any) => {
		console.error(e);
		await new Promise<void>(async (resolve) => {
			getModalStore().trigger({
				type: 'alert',
				title: 'Error disconnecting from jext server',
				body: e.message ?? 'Unknown error',
				response: () => resolve()
			});
		});
	});

	LoginStore.set(null);

	await new Promise<void>(async (resolve) => {
		getModalStore().trigger({
			type: 'alert',
			title: 'Disconnected from jext server',
			body: 'You have been disconnected from the jext server.',
			response: () => resolve()
		});
	});

	window.location.reload();
};
