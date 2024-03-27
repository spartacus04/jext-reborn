<script lang="ts">
	import 'highlight.js/styles/github-dark-dimmed.min.css';
	import '../app.postcss';

	import { default_disc, cog, knowledge_book, github, githubsponsors } from '$lib/assets';

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
	import hljs from 'highlight.js';

	import { UserAttentionType, appWindow } from '@tauri-apps/api/window';
	import { confirm } from '@tauri-apps/api/dialog';
	import { listen } from '@tauri-apps/api/event';
	import { open } from '@tauri-apps/api/shell';

	import { base } from '$app/paths';

	storePopup.set({ computePosition, autoUpdate, offset, shift, flip, arrow });
	initializeStores();

	storeHighlightJs.set(hljs);

	if (window.__TAURI__) {
		listen('scheme-request-received', (event) => {
			window.location.href = (event.payload as string).replace('jext://open', '');
			appWindow.requestUserAttention(UserAttentionType.Critical);
			appWindow.setFocus();
		});

		try {
			(async () => {
				const currentVersion = await fetch(`${base}/_app/version.json`).then((res) => res.json());
				const latestVersion = await fetch(
					'https://spartacus04.github.io/jext-reborn/_app/version.json'
				).then((res) => res.json());

				if (currentVersion.version !== latestVersion.version) {
					const update = await confirm(
						'A new version of the Jext Companion App is available. Do you want to update?'
					);

					if (update) {
						const response = await fetch(
							'https://api.github.com/repos/spartacus04/jext-reborn/actions/workflows/build-tauri.yml/runs?status=success&per_page=1'
						);

						const json = await response.json();

						if (json.total_count == 0) return;

						open(json.workflow_runs[0].html_url);
					}
				}
			})();
		} catch (_) { }
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
			<AppRailAnchor href="{base}/">
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
			<AppRailAnchor href="{base}/config">
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
			<AppRailAnchor href="{base}/documentation">
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
						src={githubsponsors}
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
