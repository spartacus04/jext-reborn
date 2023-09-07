<script lang="ts">
	import { button, button_highlight } from '@assets';

	export let selected : string;
	export let options : string[];

	const onMouseEnter = (e: MouseEvent) => {
		(e.target as HTMLElement).style.borderImageSource = `url(${button_highlight})`;
	};

	const onMouseLeave = (e: MouseEvent) => {
		(e.target as HTMLElement).style.borderImageSource = `url(${button})`;
	};
</script>

<select class="clickable" style:border-image-source="url({button})" on:mouseenter={onMouseEnter} on:mouseleave={onMouseLeave} bind:value={selected}>
	{#each options as option}
		<option value={option}>{option}</option>
	{/each}
</select>

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
		text-align: center;
		font-size: 16px;
		height: 100%;
		appearance: none;
		width: 100%;

		&:hover {
			border-image-source: url({@/assets/button_highlight.png});
		}
	}

	option {
		background-color: #202020;
		color: white;
	}
</style>
