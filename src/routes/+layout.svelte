<script lang="ts">
	import '../app.postcss';
	import default_disc from '$lib/assets/default_disc.png';
	import cog from '$lib/assets/cog.webp';
	import knowledge_book from '$lib/assets/knowledge_book.png';
	import github from '$lib/assets/github.svg';
	import github_sponsors from '$lib/assets/githubsponsors.svg';

	// Highlight JS
	import { AppShell, AppRail, AppRailAnchor, popup, storePopup, initializeStores, Modal } from '@skeletonlabs/skeleton';
	import { computePosition, autoUpdate, offset, shift, flip, arrow } from '@floating-ui/dom';
	storePopup.set({ computePosition, autoUpdate, offset, shift, flip, arrow });
	initializeStores();
</script>

<Modal />

<AppShell>
	<svelte:fragment slot="sidebarLeft">
		<AppRail hover="bg-tertiary-hover-token">
			<AppRailAnchor href="/" >
				<div class="flex" use:popup={{
					event: 'hover',
					placement: 'right',
					target: 'generator',
				}}>
					<img src={default_disc} alt="Custom disc generator" class="h-full w-full p-2 pointer-events-none">
				</div>
			</AppRailAnchor>
			<AppRailAnchor href="/config" >
				<div class="flex" use:popup={{
					event: 'hover',
					placement: 'right',
					target: 'configurator',
				}}>
					<img src={cog} alt="Plugin configurator" class="h-full w-full p-2 pointer-events-none">
				</div>
			</AppRailAnchor>
			<AppRailAnchor href="/documentation" >
				<div class="flex" use:popup={{
					event: 'hover',
					placement: 'right',
					target: 'documentation',
				}}>
					<img src={knowledge_book} alt="Documentation" class="h-full w-full p-2 pointer-events-none">
				</div>
			</AppRailAnchor>

			<svelte:fragment slot="trail">
				<AppRailAnchor href="https://github.com/spartacus04/jext-reborn" target="_blank">
					<img src={github} alt="Github" class="h-full w-full p-5">
				</AppRailAnchor>
				<AppRailAnchor href="https://github.com/sponsors/spartacus04" target="_blank">
					<img src={github_sponsors} alt="Github sponsors" class="h-full w-full p-5 pointer-events-none">
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