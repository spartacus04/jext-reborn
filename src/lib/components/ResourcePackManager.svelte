<script lang="ts">
    import { resourcePackStore, versions } from "$lib/config";
	import { inputFile, dropFile } from "$lib/directives";
    import MinecraftComboBox from "./MinecraftComboBox.svelte";
    import MinecraftTextbox from "./MinecraftTextbox.svelte";

    const setTexture = async (files?: File[]) => {
        if(files && files.length > 0 && files[0]) {
            $resourcePackStore.icon = files[0];
        }
    }
</script>

<!-- TODO: continue working on this -->

<main class="flex p-5">
    <img class="bg-[#202020] p-3 cursor-pointer h-28 sm:h-32 h-max-32 aspect-square border border-transparent hover:border-white" src={URL.createObjectURL($resourcePackStore.icon)} alt="disc icon" use:inputFile={{
        accept: 'image/*',
        cb: setTexture
    }} use:dropFile={{
        accept: 'image/*',
        cb: setTexture
    }} />

    <MinecraftComboBox bind:value={$resourcePackStore.version} values={Array.from(versions.keys())} labels={Array.from(versions.values())} />

    <MinecraftTextbox placeholder="The resource pack description" bind:value={$resourcePackStore.description} />

    <div class="flex">
        {#each $resourcePackStore.packs as pack}
        {/each}
    </div>
</main>