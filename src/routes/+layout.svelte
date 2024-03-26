<script lang="ts">
	import '../app.postcss';
	import default_disc from '$lib/assets/default_disc.png';
	import cog from '$lib/assets/cog.webp';
	import knowledge_book from '$lib/assets/knowledge_book.png';
	import github from '$lib/assets/github.svg';
	import github_sponsors from '$lib/assets/githubsponsors.svg';
	import hljs from 'highlight.js';
	import json from 'highlight.js/lib/languages/json';
	import plaintext from 'highlight.js/lib/languages/plaintext';
	import kotlin from 'highlight.js/lib/languages/kotlin';
	import java from 'highlight.js/lib/languages/java';
	import gradle from 'highlight.js/lib/languages/gradle';
	import http from 'highlight.js/lib/languages/http';
	import xml from 'highlight.js/lib/languages/xml';
	import 'highlight.js/styles/github-dark-dimmed.min.css';
	import { listen } from '@tauri-apps/api/event';
	import {
		AppShell,
		AppRail,
		AppRailAnchor,
		popup,
		storePopup,
		initializeStores,
		Modal,
		storeHighlightJs
	} from '@skeletonlabs/skeleton';
	import { computePosition, autoUpdate, offset, shift, flip, arrow } from '@floating-ui/dom';
	import { onMount } from 'svelte';
	import { UserAttentionType, appWindow } from '@tauri-apps/api/window';
	storePopup.set({ computePosition, autoUpdate, offset, shift, flip, arrow });
	hljs.registerLanguage('json', json);
	hljs.registerLanguage('plaintext', plaintext);
	hljs.registerLanguage('kotlin', kotlin);
	hljs.registerLanguage('java', java);
	hljs.registerLanguage('gradle', gradle);
	hljs.registerLanguage('xml', xml);
	hljs.registerLanguage('http', http);
	initializeStores();

	storeHighlightJs.set(hljs);

	if (window.__TAURI__) {
		listen('scheme-request-received', (event) => {
			window.location.href = (event.payload as string).replace('jext://open', '');
			appWindow.requestUserAttention(UserAttentionType.Critical);
			appWindow.setFocus();
		});
	}

	onMount(async () => {
		if (!navigator.userAgent.toLowerCase().includes('linux')) {
			if (!window.location.href.includes('?')) return (window.location.href = 'jext://open');
			window.location.href = `jext://open?${window.location.href.split('?')[1]}`;
		}
	});
</script>

<Modal />

<AppShell>
	<svelte:fragment slot="sidebarLeft">
		<AppRail hover="bg-tertiary-hover-token">
			<AppRailAnchor href="/">
				<div
					class="flex"
					use:popup={{
						event: 'hover',
						placement: 'right',
						target: 'generator'
					}}
				>
					<img
						src={default_disc}
						alt="Custom disc generator"
						class="h-full w-full p-2 pointer-events-none"
					/>
				</div>
			</AppRailAnchor>
			<AppRailAnchor href="/config">
				<div
					class="flex"
					use:popup={{
						event: 'hover',
						placement: 'right',
						target: 'configurator'
					}}
				>
					<img src={cog} alt="Plugin configurator" class="h-full w-full p-2 pointer-events-none" />
				</div>
			</AppRailAnchor>
			<AppRailAnchor href="/documentation">
				<div
					class="flex"
					use:popup={{
						event: 'hover',
						placement: 'right',
						target: 'documentation'
					}}
				>
					<img
						src={knowledge_book}
						alt="Documentation"
						class="h-full w-full p-2 pointer-events-none"
					/>
				</div>
			</AppRailAnchor>

			<svelte:fragment slot="trail">
				<AppRailAnchor href="https://github.com/spartacus04/jext-reborn" target="_blank">
					<img src={github} alt="Github" class="h-full w-full p-5" />
				</AppRailAnchor>
				<AppRailAnchor href="https://github.com/sponsors/spartacus04" target="_blank">
					<img
						src={github_sponsors}
						alt="Github sponsors"
						class="h-full w-full p-5 pointer-events-none"
					/>
				</AppRailAnchor>
			</svelte:fragment>
		</AppRail>
	</svelte:fragment>
	<slot />
</AppShell>

<!-- Popups -->

<div class="card p-1 variant-filled-primary" data-popup="generator">
	<p>Custom disc generator</p>
	<div class="arrow variant-filled-primary" />
</div>

<div class="card p-1 variant-filled-primary" data-popup="configurator">
	<p>Plugin configurator</p>
	<div class="arrow variant-filled-primary" />
</div>

<div class="card p-1 variant-filled-primary" data-popup="documentation">
	<p>Documentation</p>
	<div class="arrow variant-filled-primary" />
</div>
