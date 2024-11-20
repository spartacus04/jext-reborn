<script lang="ts">
	import DiscsTab from '$lib/components/generator/DiscsTab.svelte';
	import ResourcePackManagerTab from '$lib/components/generator/ResourcePackManagerTab.svelte';
	import TopBar from '$lib/components/generator/TopBar.svelte';

	let currentPage: string;
</script>

<svelte:head>
	<title>JEXT | Companion App</title>
	<meta
		name="description"
		content="Web GUI for the Jext Reborn Minecraft plugin. Here you can manage the resourcepack and music discs with ease!"
	/>
</svelte:head>

<div class="flex flex-col h-full w-full">
	<TopBar bind:currentPage />

	<div class="flex-1 flex flex-col h-[calc(100%-65px)]">
		{#if currentPage == "Discs"}
			<DiscsTab />
		{:else if currentPage == "Resource pack manager"}
			<ResourcePackManagerTab />
		{/if}
	</div>
</div>
