<script lang="ts">
	import { outputEverything } from "$lib/resourcepack/exporter";
    import { ProgressBar, ProgressRadial } from '@skeletonlabs/skeleton';
	import { writable } from "svelte/store";
    import loading from '$lib/assets/loading.webp';
    import { resourcePackStore } from "$lib/config";
    import MinecraftButton from "./MinecraftButton.svelte";
	import { saveAs } from "$lib/utils";
    import minecraft_logo from '$lib/assets/minecraft_logo.webp';
    import geyser_logo from '$lib/assets/geyser_logo.png';

    const progressStore = writable<{
        total?: number,
        current?: number
    }>({
        total: undefined,
        current: undefined
    });

    let javaRp: Blob|undefined = undefined;
    let bedrockRp: Blob|undefined = undefined; 

    outputEverything(rp => {
        setTimeout(() => {
            javaRp = rp;
        }, 500);
    }, (total, current) => {
        console.log(total, current)
        progressStore.set({ total, current })
    }, rp => {
        bedrockRp = rp;
    });
</script>

<main class="bg-surface-500 rounded-md p-2 h-[50%] overflow-y-auto w-[90%] sm:w-[80%] lg:w-[50%] flex items-center justify-center gap-4 flex-col">
    {#if javaRp}
        <h4 class="h4 mt-2 font-minecraft">Your resourcepack is ready!</h4>
        <div class="flex gap-4">
            <div class="card flex flex-col gap-4 p-4 rounded-lg">
                <div class="card-header w-full p-0">
                    <img src={minecraft_logo} alt="resourcepackicon" class="aspect-square object-contain w-52" style="image-rendering: optimizeQuality;">
                </div>
                <MinecraftButton on:click={() => saveAs(javaRp, 'resourcepack.zip')}>Download for<br>Minecraft: Java Edition</MinecraftButton>
            </div>

            <div class="card flex flex-col gap-4 p-4 rounded-lg">
                <div class="card-header w-full p-0">
                    <img src={geyser_logo} alt="resourcepackicon" class="w-52 aspect-square object-contain" style="image-rendering: optimizeQuality;">
                </div>
                <MinecraftButton enabled={bedrockRp != undefined} on:click={() => saveAs(bedrockRp, 'resourcepack-geysermc.mcpack')}>
                    {#if bedrockRp == undefined}
                        Generating for<br>GeyserMC...
                    {:else}
                        Download for<br>GeyserMC
                    {/if}
                </MinecraftButton>
            </div>
        </div>
    {:else}
        <h4 class="h4 mt-2 font-minecraft">Generating your assets...</h4>
        <img src={loading} alt="loading">
        <ProgressBar bind:max={$progressStore.total} bind:value={$progressStore.current} />
    {/if}
</main>