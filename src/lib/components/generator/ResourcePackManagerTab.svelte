<script lang="ts">
	import { inputFile, dropFile } from "$lib/directives";
    import { ResourcePackData } from "$lib/discs/resourcePackManager";
	import { onDestroy } from "svelte";
	import { get } from "svelte/store";
	import LauncherTextbox from "../inputs/LauncherTextbox.svelte";
	import LauncherCombobox from "../inputs/LauncherCombobox.svelte";
	import { versions } from "$lib/constants";
	import ResourcePackMerge from "./ResourcePackMerge.svelte";

    export let iconUrl = URL.createObjectURL(get(ResourcePackData).icon);

    export let setTexture = (file: File[] | undefined) => {
        if(file) {
            ResourcePackData.update(data => {
                data.icon = file[0];
                return data;
            });

            URL.revokeObjectURL(iconUrl);
            iconUrl = URL.createObjectURL(file[0]);
        }
    }

    onDestroy(() => URL.revokeObjectURL(iconUrl));
</script>

<div class="flex flex-col p-4 gap-4 h-full">
    <div class="flex flex-col sm:flex-row gap-4 w-full items-center">
        <div>
            <img
                class="bg-[#0a0a0a] p-3 cursor-pointer h-32 h-max-32 aspect-square border border-transparent hover:border-white"
                src={iconUrl}
                alt="disc icon"
                use:inputFile={{
                    accept: '.png,.avif,.webp,.jpg,jpeg,bmp,.tiff',
                    cb: setTexture
                }}
                use:dropFile={{
                    accept: '.png,.avif,.webp,.jpg,jpeg,bmp,.tiff',
                    cb: setTexture
                }}
            />
        </div>
    
        <div class="flex flex-col w-full flex-1 gap-2 justify-center">
            <div class="flex flex-col xs:flex-row w-full gap-4">
                <div class="flex flex-col flex-1">
                    <b class="text-[#aeaeae] text-xs h-min">RESOURCE PACK NAME</b>
                    <LauncherTextbox placeholder="Resourcepack Name" bind:value={$ResourcePackData.name} />
                </div>
                <div class="flex flex-col">
                    <b class="text-[#aeaeae] text-xs h-min">RESOURCE PACK VERSION</b>
                    <LauncherCombobox classes="h-[38px]" bind:value={$ResourcePackData.version} options={Array.from(versions.keys())} labels={Array.from(versions.values())} />
                </div>
            </div>
            
            <div class="flex flex-col">
                <b class="text-[#aeaeae] text-xs h-min">RESOURCE PACK DESCRIPTION</b>
                <LauncherTextbox placeholder="Resourcepack Description" bind:value={$ResourcePackData.description} />
            </div>
        </div>
    </div>

    <ResourcePackMerge />
</div>