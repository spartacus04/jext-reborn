<script lang="ts">
	import { generate_btn } from '@assets';

	export let enabled = true;
	export let highlight = false;
</script>

<div class="clickable" style:border-image-source="url({generate_btn})" class:grayscale={!enabled} class:highlight on:click class:tooltip={true} >
	<slot />
</div>

<style lang="scss">
	@import "../../styles/crisp.scss";

	.clickable {
		@extend %crisp;

		cursor: pointer;
		border: 10px solid transparent;
		border-image-slice: 2 fill;
		padding: 0.5em;
		text-shadow: 0 0 10px black;
		user-select: none;

		&.highlight:hover:not(.grayscale) {
			filter: drop-shadow(1px 1px 5px green) contrast(130%);
		}
	}
</style>
