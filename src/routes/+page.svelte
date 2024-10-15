<script lang="ts">
	import { BottomBar } from '$lib/components/generator';
	import DiscsTab from '$lib/components/generator/DiscsTab.svelte';
	import TopBar from '$lib/components/generator/TopBar.svelte';

	let currentPage: string;
</script>

<div class="flex flex-col h-full w-full">
	<TopBar bind:currentPage />

	<div class="flex-1 flex flex-col">
		{#if currentPage == "Discs"}
			<DiscsTab />
		{/if}
	</div>

</div>
