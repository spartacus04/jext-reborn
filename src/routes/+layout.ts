import { PluginConnector, pluginConnectorStore } from '$lib/pluginAccess/pluginConnector';
import { get } from 'svelte/store';
import type { LayoutLoad } from './$types';

export const ssr = false;
export const prerender = true;
export const trailingSlash = 'always';

export const load: LayoutLoad = async () => {
	const address = window.sessionStorage.getItem('serverAddress');
	const token = window.sessionStorage.getItem('bearerToken');

	if (address && token && !get(pluginConnectorStore)) {
		const connector = PluginConnector.fromBearerToken(address, token);

		try {
			if (await connector.healthCheck()) {
				pluginConnectorStore.set(connector);

				const results = await connector.getDiscs();

				return {
					props: {
						rp: results
					}
				};
			}
		} catch {
			console.error('Could not connect to the JEXT server');
		}
	}
};
