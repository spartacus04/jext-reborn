<script lang="ts">
    import { convertToOgg, stereoToMono } from "../utils";
    import Tooltip from "./Tooltip.svelte";

    import default_disk from '../assets/default_disk.png';
    import loading from '../assets/loading.webp';
    import creeper from '../assets/creeper.png';
    import chest from '../assets/chest.png';
    import delete_btn from '../assets/delete_btn.png';
    import delete_btn_hover from '../assets/delete_btn_hover.png';


    export let uploadedFile : File;
    export let oggFile : Blob = null;
    export let monoFile : Blob = null;

    export let name = 'Disc Name';
    export let author = 'Disc Author';
    export let lores = 'This is the lore of the disc\n\nYou can have multiple lines\n\nIf you don\'t want any lores you can leave this empty';
    export let texture : Blob = null;
    export let id : number;
    export let namespace = "";
    export let creeperDrop = true;
    export let lootTables : string[] = [];

    const regenNamespace = () => {
        namespace = `${name}${author}${id}`
			.replace(/[^a-zA-Z0-9]/g, '')
			.replaceAll('1', 'one')
			.replaceAll('2', 'two')
			.replaceAll('3', 'three')
			.replaceAll('4', 'four')
			.replaceAll('5', 'five')
			.replaceAll('6', 'six')
			.replaceAll('7', 'seven')
			.replaceAll('8', 'eight')
			.replaceAll('9', 'nine')
			.replaceAll('0', 'zero')
			.toLowerCase();
    }

    const prepareDisc = async () : Promise<string> => {
        const splitName = uploadedFile.name.replace(/(\.mp3)|(\.ogg)|(\.wav)/g, '').split(/\ ?-\ ?/g);

        if(splitName.length >= 1) name = splitName.shift();
        if(splitName.length >= 1) author = splitName.join();

        regenNamespace();

        oggFile = await convertToOgg(uploadedFile);
        monoFile = await stereoToMono(oggFile);

        texture = await (await fetch(default_disk)).blob();

        return URL.createObjectURL(texture)
    }

    const loadingImage = async () => {
        return await new Promise<void>(resolve => {
            setTimeout(() => {
                resolve();
            }, 1);
        })
    }
    
    let prepareDiscPromise = prepareDisc()
    let loadingImagePromise = loadingImage()

    const toggle_creeper = () => {
        creeperDrop = !creeperDrop;
    }

    let trash_hovered = false;

    const trash_toggle = () => {
        trash_hovered = !trash_hovered;
    }
</script>

<div id="song">
    {#await prepareDiscPromise}
        {#await loadingImagePromise}
            <div id="loading">
                <img src="" alt="loading">
            </div>
        {:then _} 
            <div id="loading">
                <img src={loading} alt="loading">
            </div>
        {/await}
    {:then textureUrl} 
        <div id="icons" style="">
            <Tooltip text="Changes the disc icon">
                <img src={textureUrl} height="64" width="64" alt="disc icon">
            </Tooltip>
            <div id="buttons">
                <div id="wrapper">
                    <Tooltip text="Toggles Creeper drops">
                        {#if !creeperDrop}
                            <img id="toggle_creeper" src={creeper} alt="creeper icon" on:click={toggle_creeper} on:keydown={null} class="grayscale">
                        {:else}
                            <img id="toggle_creeper" src={creeper} alt="creeper icon" on:click={toggle_creeper} on:keydown={null}>
                        {/if}
                    </Tooltip>
                </div>
                <div id="wrapper">
                    <Tooltip text="Selects structures in which the disc can be found">
                        <img id="loot_selector" src={chest} alt="chest icon">
                    </Tooltip>
                </div>
                <div id="wrapper">
                    <Tooltip text="Removes the disc">
                        {#if !trash_hovered}
                            <img id="song_delete" src={delete_btn} alt="delete icon" on:mouseenter={trash_toggle}>
                        {:else}
                            <img id="song_delete" src={delete_btn_hover} alt="delete icon" on:mouseleave={trash_toggle}>
                        {/if}
                    </Tooltip>
                </div>
            </div>
        </div>
        <div id="names">
            <input type="text" name="song_name" id="song_name_input" bind:value={name} on:input={regenNamespace}>
            <input type="text" name="song_author" id="song_author_input" bind:value={author} on:input={regenNamespace}>
            <p id="disknamespace">
                {#if namespace.length > 90}
                    {namespace.substring(0, 90)}...
                {:else}
                    {namespace}
                {/if}
            </p>
        </div>
        <textarea name="song_lore" id="song_lore_input" cols="30" rows="6" bind:value={lores}></textarea>
    {/await}
</div>

<style lang="scss">

    #song {
        display: flex;
        align-items: center;
        justify-content: space-between;
        color: white;
        font-size: 1.2em;
        border-bottom: 1px solid #808080;

        #loading {
            display: flex;
            justify-content: center;

            flex: 1;

            img {
                max-width: none;
            }
        }

        #icons {
            display: flex;

            img {
                background-color: #202020;

                padding: 10px;
                cursor: pointer;
            }

            #buttons {
                display: flex;

                height: 84px;
                flex: 1;
                flex-direction: column;
                justify-content: space-between;
                margin-left: 1em;

                img {
	                width: 24px;
	                height: 24px;
	                padding: 2px;

                    margin-bottom: -5px;
                }

                #wrapper:hover {
                    border: 1px solid white;
                    margin-right: -2px;
                    margin-top: -1px;
                    margin-bottom: -1px;
                    z-index: 10;
                }
            }
        }

        #names {
            display: flex;
	        flex-direction: column;

            input {
                width: 30em;
                margin: 0.5em 0 0 0;
                border-radius: 0;
                border: 1px solid #808080;
                background-color: #202020;
                color: white;
                font-size: 0.7em;
                padding: 0.5em;
            }

            p {
                color: #808080;
                font-size: 0.6em;
                width: 30em;
            }
        }

        textarea {
            resize: none;
	        background-color: #202020;
	        color: white;
        }
    }

    .grayscale {
        filter: grayscale(100%);
    }
</style>