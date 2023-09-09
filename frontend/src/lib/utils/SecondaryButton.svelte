<script lang="ts">
	import { button, button_highlight, button_disabled } from '@assets';

	export let enabled = true;

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
	<div class="clickable" style:border-image-source="url({button})" on:click on:mouseenter={onMouseEnter} on:mouseleave={onMouseLeave}>
		<slot />
	</div>
{:else}
	<div class="clickable" style:border-image-source="url({button_disabled})" class:disabled={!enabled}>
		<slot />
	</div>
{/if}

<style lang="scss">
	@import '../../styles/crisp.scss';

	.clickable {
		@extend %crisp;

		cursor: pointer;
		border: 8px solid transparent;
		border-bottom: 12px solid transparent;
		border-image-slice: 2 2 3 2 fill;
		padding: calc(0.8em - 8px);
		padding-bottom: calc(0.8em - 12px);
		font-family: 'minecraft_regular';
		color: white;
		height: min-content;
		text-shadow: 1px 1px 5px black;
		user-select: none;

		&:hover {
			border-image-source: url({@assets/button_highlight.png});
		}

		&.disabled {
			color: #aaa;
			cursor: not-allowed;
		}
	}
</style>
