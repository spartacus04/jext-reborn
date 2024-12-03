<script lang="ts">
	import { SidebarContainer } from '$lib/components/base';
	import { swipe, type SwipeCustomEvent } from 'svelte-gestures';
	import '../app.postcss';
	import '../global.d.ts';
	import { baseElement, isTauri } from '$lib/state';
	import { invoke } from '@tauri-apps/api/core';
	import { base64ToArrayBuffer, saveAs } from '$lib/utils';

	const media = matchMedia('(max-width: 768px)');

	let isOpen = !media.matches;
	let isMobile = media.matches;

	media.addEventListener('change', (event) => {
		isMobile = event.matches;

		if (!isMobile) {
			isOpen = true;
		}
	});

	if(isTauri) {
		invoke('yt_download', {
			url: 'O6JLkzn3X1s'
		}).then((res) => {
			const arbuf = base64ToArrayBuffer(res as string);
			const blob = new Blob([arbuf], { type: 'audio/mpeg' });

			saveAs(blob, 'test.mp3');
		}).catch((err) => {
			console.debug(err);
		});
	}

	const swipeHandler = (event: SwipeCustomEvent) => {
		if (!isMobile) return;

		if (event.detail.direction === 'left') {
			isOpen = false;
		} else if (event.detail.direction === 'right') {
			isOpen = true;
		}
	};
</script>

<div class="w-full h-full" bind:this={$baseElement} use:swipe on:swipe={swipeHandler}>
	<SidebarContainer bind:isOpen>
		<slot />
	</SidebarContainer>
</div>
