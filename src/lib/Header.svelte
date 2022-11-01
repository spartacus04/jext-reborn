<script lang="ts">
    import Tooltip from "./Tooltip.svelte";
    import pack_icon from '../assets/pack_icon.png'
    import { versions } from '../config';

    export let packname = "your_pack_name";
    export let version = 9;
    export let imagesrc = pack_icon;

    const updateImage = () => {
        document.querySelector('#pack_icon_input')?.addEventListener('change', () => {
			const files = (<HTMLInputElement>document.querySelector('#pack_icon_input')).files;
			if(!files || files.length === 0) return;

			const file = files[0];
			const reader = new FileReader();

			reader.onload = () => {
				const image = new Image();

				image.onload = () => {
					const canvas = document.createElement('canvas');

					canvas.width = 64;
					canvas.height = 64;

					const ctx = canvas.getContext('2d');

                    ctx!.drawImage(image, 0, 0, 64, 64);

                    const dataURL = canvas.toDataURL('image/png');

                    (<HTMLImageElement>document.querySelector('#pack_icon')).src = dataURL;
				};

				imagesrc = image.src = <string>reader.result;
			};

			reader.readAsDataURL(file);
		});
        
		(<HTMLInputElement>document.querySelector('#pack_icon_input')).click();
    }

    const replaceText = () => {
        packname = packname
			.replace(' ', '_')
			.replace(/[^a-zA-Z0-9_]/g, '')
			.toLowerCase();
    }
</script>

<div id="header">
    <Tooltip text="Sets the resourcepack icon">
        <img src={pack_icon} alt="pack icon" id="pack_icon" class="noselect" on:click={updateImage} on:keypress={null}>
    </Tooltip>

    <input type="file" name="pack_icon" id="pack_icon_input" class="hidden" accept="image/png">
    <input type="text" name="pack_name" id="pack_name_input" bind:value={packname} on:input={replaceText}>

    <select name="version" id="version_input" bind:value={version}>
        {#each [...versions] as [key, value]}
            <option value={key}>{value}</option>
        {/each}
    </select>
</div>

<style lang="scss">
    #header {
        display: flex;
        align-items: center;
        padding: 1em;
        background-color: #202020;

        #pack_icon {
            cursor: pointer;
        }

        #pack_name_input, select {
            margin-left: 1em;
            border-radius: 0;
            border: 1px solid white;
            background-color: #303030;
            color: white;
            font-size: 1.2em;
            padding: 0.5em;
            width: fit-content;
        }
    }
</style>