<script lang="ts">
    import { discsStore } from "$lib/config";
    import default_disc from "$lib/assets/default_disc.png";
    import default_fragment from '$lib/assets/default_fragment.png';
	import MinecraftTextbox from "$lib/components/MinecraftTextbox.svelte";
    import FormatEditor from "$lib/components/FormatEditor.svelte";
    import MinecraftCheckbox from "$lib/components/MinecraftCheckbox.svelte";
	import MinecraftButton from "./MinecraftButton.svelte";

    export let discNamespaces: string[];
    let multiple: boolean;
    const discs = $discsStore.filter(disc => disc["disc-namespace"] === $discsStore[0]["disc-namespace"])
    const uploadedDiscs = discs.filter(disc => disc.uploadData);

    $: multiple = discNamespaces.length > 1;

    let tempDisc : {
        name: { value: string, edited: boolean },
        author: { value: string, edited: boolean },
        "creeper-drop": { value: boolean, edited: boolean },
        lores: { value: string[], edited: boolean },
        "loot-tables": { value: { [key: string]: number }, edited: boolean },
        "fragment-loot-tables": { value: { [key: string]: number }, edited: boolean },

        mono: { value: boolean, edited: boolean },
        normalize: { value: boolean, edited: boolean },
        quality: { value: 'none' | 'low' | 'medium' | 'high', edited: boolean },

        texture: { value: Blob, edited: boolean },
        fragment_texture: { value: Blob, edited: boolean },
    } 

    const load = (async () => {
        tempDisc = {
            name: { value: discs.every(disc => disc.name === discs[0].name) ? discs[0].name : "", edited: false },
            author: { value: discs.every(disc => disc.author === discs[0].author) ? discs[0].author : "", edited: false },
            "creeper-drop": { value: discs.every(disc => disc["creeper-drop"] === discs[0]["creeper-drop"]) ? discs[0]["creeper-drop"] : false, edited: false },
            lores: { value: discs.every(disc => disc.lores === discs[0].lores) ? discs[0].lores : [], edited: false },
            "loot-tables": { value: discs.every(disc => disc["loot-tables"] === discs[0]["loot-tables"]) ? discs[0]["loot-tables"] : {}, edited: false },
            "fragment-loot-tables": { value: discs.every(disc => disc["fragment-loot-tables"] === discs[0]["fragment-loot-tables"]) ? discs[0]["fragment-loot-tables"] : {}, edited: false },

            mono: { value: uploadedDiscs.every(disc => disc.uploadData!.mono === uploadedDiscs[0].uploadData!.mono) ? uploadedDiscs[0].uploadData!.mono : true, edited: false },
            normalize: { value: uploadedDiscs.every(disc => disc.uploadData!.normalize === uploadedDiscs[0].uploadData!.normalize) ? uploadedDiscs[0].uploadData!.normalize : true, edited: false },
            quality: { value: uploadedDiscs.every(disc => disc.uploadData!.quality === uploadedDiscs[0].uploadData!.quality) ? uploadedDiscs[0].uploadData!.quality : "high", edited: false },

            texture: {
                value: multiple ? await (await fetch(default_disc)).blob() : discs[0].packData ? discs[0].packData.texture : discs[0].uploadData!.uploadedTexture,
                edited: false,
            },

            fragment_texture: {
                value: multiple ? await (await fetch(default_fragment)).blob() : discs[0].packData ? discs[0].packData.fragmentTexture : discs[0].uploadData!.uploadedFragmentTexture,
                edited: false,
            }
        }
    })();

    const apply = () => {
        discsStore.update(discs => {
            discs.forEach(disc => {
                if(discNamespaces.includes(disc["disc-namespace"])) {
                    if(tempDisc.name.edited) disc.name = tempDisc.name.value
                    if(tempDisc.author.edited) disc.author = tempDisc.author.value
                    if(tempDisc["creeper-drop"].edited) disc["creeper-drop"] = tempDisc["creeper-drop"].value
                    if(tempDisc.lores.edited) disc.lores = tempDisc.lores.value
                    if(tempDisc["loot-tables"].edited) disc["loot-tables"] = tempDisc["loot-tables"].value
                    if(tempDisc["fragment-loot-tables"].edited) disc["fragment-loot-tables"] = tempDisc["fragment-loot-tables"].value
                }
            })

            return discs;
        })
    }

    const setTexture = (files: File[]) => {
        if(files.length > 0 && files[0]) {
            tempDisc.texture.value = files[0];
            tempDisc.texture.edited = true;
        }
    }

    const setFragmentTexture = (files: File[]) => {
        if(files.length > 0 && files[0]) {
            tempDisc.fragment_texture.value = files[0];
            tempDisc.fragment_texture.edited = true
        }
    }

</script>

<main class="bg-surface-500 rounded-md p-2 h-[50%] overflow-y-auto w-[90%] sm:w-[80%] lg:w-[50%]">
    {#await load}
        <div />
    {:then _} 
        <h3 class="h3 mb-2">Edit one or more discs</h3>
        <!-- Icons, author, name -->
        <div class="flex flex-col justify-between gap-4 sm:flex-row">
            <div class="flex gap-2 justify-around">
                <img class="bg-[#202020] p-3 cursor-pointer h-28 sm:h-32 h-max-32 aspect-square border border-transparent hover:border-white" src={URL.createObjectURL(tempDisc.texture.value)} alt="disc icon">
                <img class="bg-[#202020] p-3 cursor-pointer h-28 sm:h-32 aspect-square border border-transparent hover:border-white" src={URL.createObjectURL(tempDisc.fragment_texture.value)} alt="disc icon">
            </div>
            <div class="flex flex-col flex-1 justify-around gap-2">
                <MinecraftTextbox placeholder="Name"></MinecraftTextbox>
                <MinecraftTextbox placeholder="Author"></MinecraftTextbox>
            </div>
        </div>
        
        <div class="h-2 border-b-2 border-[#232323] mb-2" />

        <FormatEditor text="test" firstline={tempDisc.author.value != '' ? `${tempDisc.name.value} - ${tempDisc.author.value}` : tempDisc.name.value} />

        <div class="h-2 border-b-2 border-[#232323] mb-2" />

        <div class="flex flex-col justify-between gap-4 sm:flex-row items-center">
            <div class="flex justify-start gap-4 flex-row items-center">
                <MinecraftCheckbox value={true} size="0px"/>
                <h5 class="h5 font-minecraft h-min">Enable as Creeper drop</h5>
            </div>

            <MinecraftButton>Disc loot table</MinecraftButton>
            <MinecraftButton>Fragment loot table</MinecraftButton>
        </div>
    {/await}
    
</main>