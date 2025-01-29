import { goto } from '$app/navigation';
import { pluginConnectorStore } from '$lib/pluginAccess/pluginConnector';
import type { Load } from '@sveltejs/kit';
import { get } from 'svelte/store';

export const load: Load = async () => {
	if (!get(pluginConnectorStore)) {
		goto('../');
	}
};
