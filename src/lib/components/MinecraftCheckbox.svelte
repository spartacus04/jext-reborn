<script lang="ts">
	import checkbox_on from '$lib/assets/checkbox_on.png';
	import checkbox_off from '$lib/assets/checkbox_off.png';

	export let value: unknown;
	export let size: string = '24px';
	export let onClick: () => unknown = () => {};

	const toggle = () => {
		value = !value;
		onClick();
	};
</script>

{#if value}
	<button
		class="border-[3px] aspect-square cursor-pointer border-black hover:border-white flex"
		style="height: calc({size} + 1.6em); width: calc({size} + 1.6em);"
		on:click={toggle}
	>
		<img src={checkbox_on} alt="true" class="h-full" />
	</button>
{:else}
	<button
		class="border-[3px] aspect-square cursor-pointer border-black hover:border-white flex"
		style="height: calc({size} + 1.6em); width: calc({size} + 1.6em);"
		on:click={toggle}
	>
		<img src={checkbox_off} alt="false" class="h-full" />
	</button>
{/if}

<style>
</style>
