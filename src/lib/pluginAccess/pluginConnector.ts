import { fetch } from '@tauri-apps/plugin-http';
import { writable } from 'svelte/store';

export const pluginConnectorStore = writable<PluginConnector | undefined>(undefined);

export interface ConfigNode {
	name: string;
	id: string;
	description: string;
	value: any;
	default: any;
	enumValues?: string[] | string;
}

export class PluginConnector {
	private bearerToken: string | undefined;
	private serverAddress: string;

	private constructor(serverAddress: string, bearerToken?: string) {
		this.serverAddress = serverAddress;
		this.bearerToken = bearerToken;
	}

	public async loginWithPassword(password: string) {
		const response = await fetch(`${this.serverAddress}/connect`, {
			method: 'POST',
			body: password
		}).catch(() => {
			throw new Error('Could not connect to the JEXT server');
		});

		if (response.status === 200) {
			this.bearerToken = await response.text();

			window.sessionStorage.setItem('bearerToken', this.bearerToken);
			window.sessionStorage.setItem('serverAddress', this.serverAddress);
		} else {
			throw new Error('The password is incorrect');
		}
	}

	public async disconnect() {
		if (!this.bearerToken) {
			throw new Error('Cannot disconnect without a bearer token');
		}

		const response = await fetch(`${this.serverAddress}/disconnect`, {
			method: 'POST',
			headers: {
				Authorization: `Bearer ${this.bearerToken}`
			}
		}).catch(() => {
			throw new Error('Could not connect to the JEXT server');
		});

		if (response.status === 200 || response.status === 204) {
			this.bearerToken = undefined;
		} else {
			pluginConnectorStore.set(undefined);
			window.sessionStorage.removeItem('bearerToken');
			window.sessionStorage.removeItem('serverAddress');
			throw new Error('The bearer token is invalid');
		}
	}

	public async healthCheck(): Promise<boolean> {
		const response1 = await fetch(`${this.serverAddress}/health`, {
			method: 'GET'
		}).catch(() => {
			throw new Error('Could not connect to the JEXT server');
		});

		if (response1.status != 200 && response1.status != 204) {
			pluginConnectorStore.set(undefined);
			window.sessionStorage.removeItem('bearerToken');
			window.sessionStorage.removeItem('serverAddress');
			throw new Error('Health check failed');
		}

		if (!this.bearerToken) {
			return false;
		}

		const response2 = await fetch(`${this.serverAddress}/health`, {
			method: 'GET',
			headers: {
				Authorization: `Bearer ${this.bearerToken}`
			}
		}).catch(() => {
			throw new Error('Could not connect to the JEXT server');
		});

		if (response2.status == 200 || response2.status == 204) {
			return true;
		}

		pluginConnectorStore.set(undefined);
		window.sessionStorage.removeItem('bearerToken');
		window.sessionStorage.removeItem('serverAddress');
		throw new Error('The bearer token is invalid');
	}

	public async getConfig(): Promise<ConfigNode[]> {
		if (!this.bearerToken) {
			throw new Error('Cannot get config without a bearer token');
		}

		const response = await fetch(`${this.serverAddress}/config/read`, {
			method: 'GET',
			headers: {
				Authorization: `Bearer ${this.bearerToken}`
			}
		}).catch(() => {
			throw new Error('Could not connect to the JEXT server');
		});

		if (response.status != 200) {
			pluginConnectorStore.set(undefined);
			window.sessionStorage.removeItem('bearerToken');
			window.sessionStorage.removeItem('serverAddress');
			throw new Error('The bearer token is invalid');
		}

		return await response.json();
	}

	public async applyConfig(config: any) {
		if (!this.bearerToken) {
			throw new Error('Cannot set config without a bearer token');
		}

		const response = await fetch(`${this.serverAddress}/config/apply`, {
			method: 'POST',
			headers: {
				Authorization: `Bearer ${this.bearerToken}`
			},
			body: JSON.stringify(config)
		}).catch(() => {
			throw new Error('Could not connect to the JEXT server');
		});

		if (response.status == 401) {
			pluginConnectorStore.set(undefined);
			window.sessionStorage.removeItem('bearerToken');
			window.sessionStorage.removeItem('serverAddress');
			throw new Error('The bearer token is invalid');
		}

		if (response.status == 400) {
			throw new Error('The config is invalid');
		}
	}

	public async getDiscs(): Promise<Blob | null> {
		if (!this.bearerToken) {
			throw new Error('Cannot get discs without a bearer token');
		}

		const response = await fetch(`${this.serverAddress}/discs/read`, {
			method: 'GET',
			headers: {
				Authorization: `Bearer ${this.bearerToken}`
			}
		}).catch(() => {
			throw new Error('Could not connect to the JEXT server');
		});

		if (response.status != 200) {
			return null;
		}

		return await response.blob();
	}

	public async applyDiscs(discs: Blob) {
		if (!this.bearerToken) {
			throw new Error('Cannot set discs without a bearer token');
		}

		const response = await fetch(`${this.serverAddress}/discs/apply`, {
			method: 'POST',
			headers: {
				Authorization: `Bearer ${this.bearerToken}`
			},
			body: discs
		}).catch(() => {
			throw new Error('Could not connect to the JEXT server');
		});

		if (response.status == 401) {
			pluginConnectorStore.set(undefined);
			window.sessionStorage.removeItem('bearerToken');
			window.sessionStorage.removeItem('serverAddress');
			throw new Error('The bearer token is invalid');
		}
	}

	public async applyDiscsGeyser(discs: Blob) {
		if (!this.bearerToken) {
			throw new Error('Cannot set discs without a bearer token');
		}

		const response = await fetch(`${this.serverAddress}/discs/applygeyser`, {
			method: 'POST',
			headers: {
				Authorization: `Bearer ${this.bearerToken}`
			},
			body: discs
		}).catch(() => {
			throw new Error('Could not connect to the JEXT server');
		});

		if (response.status == 401) {
			pluginConnectorStore.set(undefined);
			window.sessionStorage.removeItem('bearerToken');
			window.sessionStorage.removeItem('serverAddress');
			throw new Error('The bearer token is invalid');
		}
	}

	static fromBearerToken(serverAddress: string, bearerToken: string) {
		return new PluginConnector(serverAddress, bearerToken);
	}

	static async fromPassword(serverAddress: string, password: string) {
		const connector = new PluginConnector(serverAddress);

		await connector.loginWithPassword(password);
		return connector;
	}
}
