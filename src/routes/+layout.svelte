<script lang="ts">
	import { SidebarContainer } from '$lib/components/base';
	import { swipe, type SwipeCustomEvent } from 'svelte-gestures';
	import '../app.postcss';
	import '../global.d.ts';
	import { baseElement } from '$lib/state';
	import type { LayoutData } from './$types';
	import { addDisc } from '$lib/discs/discManager';
	import { JextReader } from '$lib/exporter/importer';

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

	export let data: LayoutData;

	if (data.props?.rp) {
		JextReader(data.props.rp).then((results) => {
			if (results) {
				results.forEach((result) => addDisc(result));
			}
		});
	}
</script>

<div class="w-full h-full" bind:this={$baseElement} use:swipe on:swipe={swipeHandler}>
	<SidebarContainer bind:isOpen>
		<slot />
	</SidebarContainer>
</div>
