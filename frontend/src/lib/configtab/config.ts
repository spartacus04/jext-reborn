import { ENDPOINT } from '@/constants';
import { writable, get } from 'svelte/store';

export interface ConfigNode<T> {
	name: string,
	id: string,
	description: string,
	value: T,
	defaultValue: T,
	enumValues?: string[]
}

export interface ConfigNodeApply<T> {
	id: string,
	value: T
}

export const configStore = writable<ConfigNode<boolean|number|string|{[key : string] : boolean}>[]>([]);

export const fetchConfigData = async (force: boolean = false) => {
	await new Promise<void>(async resolve => {
		try {
			if(!force) {
				const data = get(configStore);
				if(data.length > 0) {
					return resolve();
				}
			}

			const response = await fetch(`${ENDPOINT}/api/config`);
			const data = await response.json();

			configStore.set(data);
			resolve();
		}
		catch(err) {
			setTimeout(async () => {
				await fetchConfigData(force);
				resolve();
			}, 5000);
		}
	});
};

export const saveConfigData = async () => {
	const config = get(configStore);

	const data : ConfigNodeApply<boolean|number|string|{[key : string] : boolean}>[] = config.map(node => {
		return {
			id: node.id,
			value: node.value,
		};
	});

	const response = await fetch(`${ENDPOINT}/api/config`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify(data),
	});

	const json = await response.json();

	if(json.message !== 'ok') {
		throw new Error('Failed to save config data');
	}
};