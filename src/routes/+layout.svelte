<script lang="ts">
	import { SidebarContainer } from '$lib/components/base';
	import { swipe, type SwipeCustomEvent } from 'svelte-gestures';
	import '../app.postcss';
	import '../global.d.ts';
	import { baseElement, isTauri } from '$lib/state';
	import { addDisc } from '$lib/discs/discManager';
	import { JextReader } from '$lib/exporter/importer';
	import { onMount } from 'svelte';
	import { get } from 'svelte/store';
	import { PluginConnector, pluginConnectorStore } from '$lib/pluginAccess/pluginConnector';

	const media = matchMedia('(max-width: 768px)');

	let isOpen = !media.matches;
	let isMobile = media.matches;

	media.addEventListener('change', (event) => {
		isMobile = event.matches;

		if (!isMobile) {
			isOpen = true;
		}
	});

	const swipeHandler = (event: SwipeCustomEvent) => {
		if (!isMobile) return;

		if (event.detail.direction === 'left') {
			isOpen = false;
		} else if (event.detail.direction === 'right') {
			isOpen = true;
		}
	};

	onMount(() => {
		const restoreConnection = async () => {
			if (!isTauri) return;

			const address = window.sessionStorage.getItem('serverAddress');
			const token = window.sessionStorage.getItem('bearerToken');

			if (!address || !token || get(pluginConnectorStore)) return;

			isConnectingToApi = true;

			const connector = PluginConnector.fromBearerToken(address, token);

			try {
				if (await connector.healthCheck()) {
					pluginConnectorStore.set(connector);

					const results = await connector.getDiscs();

					if (results) {
						const discs = await JextReader(results);

						discs.forEach((disc) => addDisc(disc));
					}
				}
			} catch {
				console.error('Could not connect to the JEXT server');
			} finally {
				isConnectingToApi = false;
			}
		};

		if (localStorage.getItem('disable-animations')) {
			document.documentElement.style.setProperty('--prefers-reduced-motion', 'reduce');
		} else {
			document.documentElement.style.removeProperty('--prefers-reduced-motion');
		}

		if (localStorage.getItem('accessibility-text-size')) {
			document.documentElement.setAttribute('data-font-size', 'large');
		}

		// data-font-type='dyslexic'
		if (localStorage.getItem('accessibility-dyslexic-font')) {
			document.documentElement.setAttribute('data-font-type', 'dyslexic');
		}

		restoreConnection();
	});

	let isConnectingToApi = false;
</script>

<div class="w-full h-full" bind:this={$baseElement} use:swipe on:swipe={swipeHandler}>
	{#if isConnectingToApi}
		<div
			class="fixed inset-0 z-[9999] bg-surface-background/90 flex flex-col items-center justify-center gap-4 text-white"
		>
			<div class="w-10 h-10 border-4 border-surface-separator border-t-white rounded-full animate-spin" />
			<p class="font-minecraft text-sm">Connecting to JEXT API...</p>
		</div>
	{/if}

	<SidebarContainer bind:isOpen>
		<slot />
	</SidebarContainer>
</div>
