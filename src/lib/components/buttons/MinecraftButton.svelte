<script lang="ts">
	import { button, button_highlight, button_disabled } from '$lib/assets';

	export let enabled = true;
	export let flex = false;
	export let square = false;

	const onMouseEnter = (e: MouseEvent) => {
		if (enabled) {
			(e.target as HTMLElement).style.borderImageSource = `url(${button_highlight})`;
		}
	};

	const onMouseLeave = (e: MouseEvent) => {
		if (enabled) {
			(e.target as HTMLElement).style.borderImageSource = `url(${button})`;
		}
	};
</script>

{#if enabled}
	<button
		class:flex-1={flex}
		disabled={!enabled}
		id="clickable"
		class="cursor-pointer border-8 border-transparent border-b-[12px] font-minecraft text-white h-min select-none {square
			? 'aspect-square'
			: ''}"
		style:border-image-source="url({button})"
		on:click
		on:mouseenter={onMouseEnter}
		on:mouseleave={onMouseLeave}
		class:disabled={!enabled}
	>
		<slot />
	</button>
{:else}
	<button
		class:flex-1={flex}
		disabled={!enabled}
		id="clickable"
		class="cursor-pointer border-8 border-transparent border-b-[12px] font-minecraft text-white h-min select-none {square
			? 'aspect-square'
			: ''}"
		style:border-image-source="url({button_disabled})"
		on:click
		on:mouseenter={onMouseEnter}
		on:mouseleave={onMouseLeave}
		class:disabled={!enabled}
	>
		<slot />
	</button>
{/if}

<style>
	.disabled {
		color: #aaa;
		cursor: not-allowed;
	}

	#clickable:hover {
		border-image-source: url({@assets/button_highlight.webp});
	}

	#clickable {
		border-image-slice: 2 2 3 2 fill;
		padding: calc(0.8em - 8px);
		padding-bottom: calc(0.8em - 12px);
		text-shadow: 1px 1px 5px black;
	}
</style>
