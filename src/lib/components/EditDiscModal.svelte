<script lang="ts">
    import { discsStore } from "$lib/config";
    import default_disc from "$lib/assets/default_disc.png";
    import default_fragment from '$lib/assets/default_fragment.png';
	import MinecraftTextbox from "$lib/components/MinecraftTextbox.svelte";
    import FormatEditor from "$lib/components/FormatEditor.svelte";
    import MinecraftCheckbox from "$lib/components/MinecraftCheckbox.svelte";
	import MinecraftButton from "./MinecraftButton.svelte";
	import { getModalStore, type ModalComponent } from "@skeletonlabs/skeleton";
    import DungeonSelectModal from "./DungeonSelectModal.svelte";
	import MinecraftComboBox from "./MinecraftComboBox.svelte";
    import { inputFile, dropFile } from "$lib/directives";
	import { processImage } from "$lib/resourcepack/utils";
    
    export let discNamespaces: string[];
    let multiple: boolean;
    const discs = $discsStore.filter(disc => discNamespaces.includes(disc["disc-namespace"]))
    
    const uploadedDiscs = discs.filter(disc => disc.uploadData);
    const modalStore = getModalStore();

    $: multiple = discNamespaces.length > 1;

    export let tempDisc : {
        title: { value: string, edited: boolean },
        author: { value: string, edited: boolean },
        "creeper-drop": { value: boolean, edited: boolean },
        lores: { value: string[], edited: boolean },
        "loot-tables": { value: { [key: string]: number }, edited: boolean },
        "fragment-loot-tables": { value: { [key: string]: number }, edited: boolean },

        mono?: { value: boolean, edited: boolean },
        normalize?: { value: boolean, edited: boolean },
        quality?: { value: 'none' | 'low' | 'medium' | 'high', edited: boolean },

        texture: { value: Blob, edited: boolean },
        fragment_texture: { value: Blob, edited: boolean },
    }

    let initalText: string;

    const load = (async () => {
        // Warning: state is not preserved between modals, this is a shitty workaround but it works
        if($modalStore[0].meta) {
            tempDisc = $modalStore[0].meta;
            return;
        }

        tempDisc = {
            title: { value: discs.every(disc => disc.title === discs[0].title) ? discs[0].title : "", edited: false },
            author: { value: discs.every(disc => disc.author === discs[0].author) ? discs[0].author : "", edited: false },
            "creeper-drop": { value: discs.every(disc => disc["creeper-drop"] === discs[0]["creeper-drop"]) ? discs[0]["creeper-drop"] : false, edited: false },
            lores: { value: discs.every(disc => disc.lores === discs[0].lores) ? discs[0].lores : [], edited: false },
            "loot-tables": { value: discs.every(disc => disc["loot-tables"] === discs[0]["loot-tables"]) ? discs[0]["loot-tables"] : {}, edited: false },
            "fragment-loot-tables": { value: discs.every(disc => disc["fragment-loot-tables"] === discs[0]["fragment-loot-tables"]) ? discs[0]["fragment-loot-tables"] : {}, edited: false },

            mono: uploadedDiscs.length != 0 ? { value: uploadedDiscs.every(disc => disc.uploadData!.mono === uploadedDiscs[0].uploadData!.mono) ? uploadedDiscs[0].uploadData!.mono : true, edited: false } : undefined,
            normalize: uploadedDiscs.length != 0 ? { value: uploadedDiscs.every(disc => disc.uploadData!.normalize === uploadedDiscs[0].uploadData!.normalize) ? uploadedDiscs[0].uploadData!.normalize : true, edited: false } : undefined,
            quality: uploadedDiscs.length != 0 ? { value: uploadedDiscs.every(disc => disc.uploadData!.quality === uploadedDiscs[0].uploadData!.quality) ? uploadedDiscs[0].uploadData!.quality : "high", edited: false } : undefined,

            texture: {
                value: multiple ? await (await fetch(default_disc)).blob() : discs[0].packData ? discs[0].packData.texture : discs[0].uploadData!.uploadedTexture,
                edited: false,
            },

            fragment_texture: {
                value: multiple ? await (await fetch(default_fragment)).blob() : discs[0].packData ? discs[0].packData.fragmentTexture : discs[0].uploadData!.uploadedFragmentTexture,
                edited: false,
            }
        }

        initalText = tempDisc.lores.value.join('\n');
    })();

    const setTexture = async (files?: File[]) => {
        if(files && files.length > 0 && files[0]) {
            tempDisc.texture.value = files[0];
            tempDisc.texture.edited = true;
        }
    }

    const setFragmentTexture = (files?: File[]) => {
        if(files && files.length > 0 && files[0]) {
            tempDisc.fragment_texture.value = files[0];
            tempDisc.fragment_texture.edited = true
        }
    }

    const openDungeonModal = (isFragment: Boolean) => {
        $modalStore[0].meta = tempDisc;

		const modalComponent: ModalComponent = { 
            ref: DungeonSelectModal, 
            props: { 
                source: 'chests/*', 
                defaultValue: 0, 
                value: isFragment ? tempDisc["fragment-loot-tables"].value : tempDisc["loot-tables"].value,
                isChance: true,
            }
        };

        // not using modalStore.trigger because i need to show this modal on top of the current one
        modalStore.update(modals => {
            modals.unshift({
                type: 'component',
                component: modalComponent,
                title: isFragment ? 'Edit fragment loot tables' : 'Edit disc loot tables',
                response(r) {
                    if(!r || Object.keys(r).length == 0) return;

                    tempDisc[isFragment ? "fragment-loot-tables" : "loot-tables"].value = r;
                    tempDisc[isFragment ? "fragment-loot-tables" : "loot-tables"].edited = true;
                },
            })
            return modals;
        })
	}

    const openArchaeologyModal = (isFragment: boolean) => {
        $modalStore[0].meta = tempDisc;

        const modalComponent: ModalComponent = { 
            ref: DungeonSelectModal, 
            props: { 
                source: 'archaeology/*', 
                defaultValue: 0, 
                value: isFragment ? tempDisc["fragment-loot-tables"].value : tempDisc["loot-tables"].value,
                isBoolean: true,
            }
        };

        // not using modalStore.trigger because i need to show this modal on top of the current one
        modalStore.update(modals => {
            modals.unshift({
                type: 'component',
                component: modalComponent,
                title: isFragment ? 'Edit fragment loot tables' : 'Edit disc loot tables',
                response(r) {
                    if(!r || Object.keys(r).length == 0) return;

                    tempDisc[isFragment ? "fragment-loot-tables" : "loot-tables"].value = r;
                    tempDisc[isFragment ? "fragment-loot-tables" : "loot-tables"].edited = true;
                },
            })
            return modals;
        })
    }

    const setUpdatedPropery = (property: string) => {
        (tempDisc as any)[property].edited = true;
        console.log(tempDisc);
    }

    const exit = () => {
        // apply tempDisc to namespaces
        discsStore.update((discs) => {
            discs.forEach(async disc => {
                if(discNamespaces.includes(disc["disc-namespace"])) {
                    for(const [key, value] of Object.entries(tempDisc)) {
                        if(value != undefined && value.edited) {
                            switch(key) {
                                case 'mono':
                                case 'normalize':
                                case 'quality':
                                    if(!disc.uploadData) break;
                                    (disc.uploadData as any)[key] = value.value;
                                    break;
                                case 'texture':
                                    if(disc.packData) {
                                        disc.packData.texture = await processImage(value.value as Blob);
                                    } else {
                                        disc.uploadData!.uploadedTexture = value.value as Blob;
                                    }
                                    break;
                                case 'fragment_texture':
                                    if(disc.packData) {
                                        disc.packData.fragmentTexture = await processImage(value.value as Blob);
                                    } else {
                                        disc.uploadData!.uploadedFragmentTexture = value.value as Blob;
                                    }
                                    break;
                                default:
                                    (disc as any)[key] = value.value;
                                    break;
                            }
                        }
                    }
                }
            })

            return discs;
        })

        if($modalStore[0]) {
            $modalStore[0].response!!(true);
            modalStore.close();
        }
    }

    const deleteDisc = () => {
        discsStore.update(discs => {
            return discs.filter(disc => !discNamespaces.includes(disc["disc-namespace"]));
        })

        if($modalStore[0]) {
            $modalStore[0].response!!(true);
            modalStore.close();
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
                <img class="bg-[#202020] p-3 cursor-pointer h-28 sm:h-32 h-max-32 aspect-square border border-transparent hover:border-white" src={URL.createObjectURL(tempDisc.texture.value)} alt="disc icon" use:inputFile={{
                    accept: 'image/*',
                    cb: setTexture
                }} use:dropFile={{
                    accept: 'image/*',
                    cb: setTexture
                }} />
                <img class="bg-[#202020] p-3 cursor-pointer h-28 sm:h-32 aspect-square border border-transparent hover:border-white" src={URL.createObjectURL(tempDisc.fragment_texture.value)} alt="disc icon" use:inputFile={{
                    accept: 'image/*',
                    cb: setFragmentTexture
                }} use:dropFile={{
                    accept: 'image/*',
                    cb: setFragmentTexture
                }} />
            </div>
            <div class="flex flex-col flex-1 justify-around gap-2">
                <MinecraftTextbox placeholder="Name" bind:value={tempDisc.title.value} on:input={() => setUpdatedPropery('name')} />
                <MinecraftTextbox placeholder="Author" bind:value={tempDisc.author.value} on:input={() => setUpdatedPropery('author')}/>
            </div>
        </div>
        
        <div class="h-2 border-b-2 border-[#232323] mb-2" />

        <FormatEditor on:input={() => setUpdatedPropery('lores')} bind:split={tempDisc.lores.value} text={initalText} firstline={tempDisc.author.value != '' ? `${tempDisc.title.value} - ${tempDisc.author.value}` : tempDisc.title.value} />

        <div class="h-2 border-b-2 border-[#232323] mb-2" />

        <div class="flex flex-col justify-around gap-4 sm:flex-row items-center mt-4">
            <div class="flex justify-start gap-4 flex-row items-center">
                <MinecraftCheckbox onClick={() => setUpdatedPropery('creeper-drop')} bind:value={tempDisc["creeper-drop"].value} size="0px"/>
                <h5 class="h5 font-minecraft h-min">Enable as Creeper drop</h5>
            </div>
        </div>

        <div class="flex flex-col justify-around gap-4 sm:flex-row items-center mt-4">
            <MinecraftButton on:click={() => openDungeonModal(false)}>Disc loot table</MinecraftButton>
            <MinecraftButton on:click={() => openDungeonModal(true)}>Fragment loot table</MinecraftButton>
        </div>

        <div class="flex flex-col justify-around gap-4 sm:flex-row items-center mt-4">
            <MinecraftButton on:click={() => openArchaeologyModal(false)}>Disc archaeology loot table</MinecraftButton>
            <MinecraftButton on:click={() => openArchaeologyModal(true)}>Fragment archaeology loot table</MinecraftButton>
        </div>

        {#if tempDisc.mono && tempDisc.normalize && tempDisc.quality}
            <div class="h-2 border-b-2 border-[#232323] mb-2" />

            <div class="flex flex-col justify-around gap-4 items-center mt-4 w-full">
                <div class="flex justify-start gap-4 flex-row items-center">
                    <MinecraftCheckbox bind:value={tempDisc.mono.value} size="0px" onClick={() => setUpdatedPropery('mono')}/>
                    <h5 class="h5 font-minecraft h-min">Convert to mono</h5>
                </div>
                <div class="flex justify-start gap-4 flex-row items-center">
                    <MinecraftCheckbox bind:value={tempDisc.normalize.value} size="0px" onClick={() => setUpdatedPropery('normalize')}/>
                    <h5 class="h5 font-minecraft h-min">Normalize audio</h5>
                </div>
                <div class="flex justify-start gap-4 flex-row items-center w-[30%]">
                    <h5 class="h5 font-minecraft h-min">Quality preset</h5>
                    <MinecraftComboBox values={['none', 'low', 'medium', 'high']} bind:value={tempDisc.quality.value} on:input={() => setUpdatedPropery('quality')}/>
                </div>
            </div>
        {/if}

        <div class="flex w-full items-center justify-center mt-4 gap-4">
            <button class="btn variant-filled-secondary text-white" on:click={exit}>Save</button>
            <button class="btn variant-filled-error text-white" on:click={deleteDisc}>Delete disc{multiple ? 's' : ''}</button>
        </div>

    {/await}
    
</main>