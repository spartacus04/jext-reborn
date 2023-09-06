<script lang="ts">
	import { checkbox_off, checkbox_on } from '@/assets';

	export let value: boolean;

	const toggle = () => {
		value = !value;
	};
</script>


{#if value}
	<img src={checkbox_on} alt="true" on:click={toggle}>
{:else}
	<img src={checkbox_off} alt="false" on:click={toggle}>
{/if}


<style lang="scss">
	@import '../../styles/crisp.scss';

	img {
		@extend %crisp;

		aspect-ratio: 1/1;
		height: calc(100% - 6px);
		border: 3px solid black;
		cursor: pointer;

		&:hover {
			border: 3px solid white;
		}
	}
</style>