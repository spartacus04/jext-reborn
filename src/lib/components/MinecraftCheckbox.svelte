<script lang="ts">
    import checkbox_on from '$lib/assets/checkbox_on.png';
    import checkbox_off from '$lib/assets/checkbox_off.png';

	export let value: unknown;

	const toggle = () => {
		value = !value;
	};
</script>


{#if value}
	<button class="border-[3px] aspect-square cursor-pointer border-black hover:border-white flex" on:click={toggle}>
		<img src={checkbox_on} alt="true" class="h-full">
	</button>
{:else}
	<button class="border-[3px] aspect-square cursor-pointer border-black hover:border-white flex" on:click={toggle}>
		<img src={checkbox_off} alt="false" class="h-full">
	</button>
{/if}

<style>
	button {
		height: calc(24px + 1.6em);
		width: calc(24px + 1.6em);
	}
</style>