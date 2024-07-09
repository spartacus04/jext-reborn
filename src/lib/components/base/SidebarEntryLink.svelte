<script lang="ts">
	import { afterNavigate } from '$app/navigation';
	import { external_link } from '$lib/assets';
	import { scale } from 'svelte/transition';

	export let icon: string;
	export let title: string;
	export let href: string;
	export let external: boolean = false;

	let active: boolean;

	afterNavigate(() => {
		active = !href.startsWith('http') && location.pathname.split('/')[1] === href.split('/')[1];
	});
</script>

{#if active}
	<div class="flex items-center">
		<div class="w-1 h-6 bg-primary-variant bg-white -mr-1" in:scale={{ opacity: 1 }} />
		<div class="p-2 pl-4 flex items-center">
			<img class="min-w-12 w-12 aspect-square" src={icon} alt={title} />

			<h3 class="font-minecraft text-white text-lg ml-3">{title}</h3>
		</div>
	</div>
{:else}
	<a {href} target={external ? '_blank' : ''}>
		<div class="flex p-2 pl-4 items-center hover:bg-surface-background-variant">
			<img class="min-w-12 w-12 aspect-square" src={icon} alt={title} />

			<h3 class="font-minecraft text-white text-lg ml-3">{title}</h3>

			{#if external}
				<img class="w-6 h-6 ml-auto stroke-gray-50" src={external_link} alt="External link" />
			{/if}
		</div>
	</a>
{/if}
