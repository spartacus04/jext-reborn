<script lang="ts">
	import generate_btn from '$lib/assets/generate_btn.png';

	export let enabled = true;
	export let highlight = false;
</script>

<button
	id="clickable"
	class="cursor-pointer border-[10px] border-transparent p-2 select-none"
	style:border-image-source="url({generate_btn})"
	class:grayscale={!enabled}
	class:highlight
	disabled={!enabled}
	on:click
>
	<slot />
</button>

<style>
	#clickable {
		border-image-slice: 2 fill;
		text-shadow: 0 0 10px black;

		&.highlight:hover:not(.grayscale) {
			filter: drop-shadow(1px 1px 5px green) contrast(130%);
		}
	}
</style>
