<script lang="ts">
    import TopBar from "$lib/components/generator/TopBar.svelte";
	import GeneralSettingsPage from "$lib/components/settings/GeneralSettingsPage.svelte";

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
	<TopBar bind:currentPage title="SETTINGS" pages={{General: false, About: false, Debug: false}} />

	<div class="flex-1 flex flex-col h-[calc(100%-65px)] bg-[#333333]">
        {#if currentPage === "General"}
            <GeneralSettingsPage />
        {:else if currentPage === "About"}
            <div>About</div>
        {:else if currentPage === "Debug"}
            <div>Debug</div>
        {:else}
            <div>General</div>
        {/if}
	</div>
</div>
