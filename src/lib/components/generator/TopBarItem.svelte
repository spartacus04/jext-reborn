<script lang="ts">
	import { scale } from 'svelte/transition';

	export let active: boolean;
	export let page: string;
</script>

{#if active}
	<div class="flex flex-col items-center px-2 first:pl-0">
		<p class="text-white font-bold text-center text-nowrap select-none">
			{page}
		</p>

		<div class="w-5 h-1 bg-primary-variant bg-[#008542]" in:scale={{ opacity: 1, duration: 200 }} />
	</div>
{:else}
	<button
		class="text-[#aeaeae] hover:text-[#eaeaea] text-center mb-1 text-nowrap px-2 first:pl-0"
		title={page}
		on:click
	>
		{page}
	</button>
{/if}

<style>
	/* Keeps the width of the button consistent */
	button::after {
		display: block;
		content: attr(title);
		font-weight: bold;
		height: 1px;
		color: transparent;
		overflow: hidden;
		visibility: hidden;
	}
</style>
