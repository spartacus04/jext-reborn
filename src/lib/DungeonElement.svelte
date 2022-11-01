<script lang="ts">
    import loading from '../assets/loading.webp';

    import { fade } from 'svelte/transition'; 

    export let value = false;
    export let image : string;
    export let name : string;
    export let onClick = () => {};


    const getImage = async (name: string) : Promise<string> => {
        const response = await fetch(name);

        const blob = await response.blob();

        return URL.createObjectURL(blob);
    }

    const getImagePromise = getImage(image); 
</script>

{#await getImagePromise}
    {#if value}
        <div class="selected" on:click={onClick} on:keydown={null} in:fade>
            <img src={loading} alt={name}>
            <p>{name}</p>
        </div>
        {:else}
        <div on:click={onClick} on:keydown={null} in:fade>
            <img src={image} alt={name}>
            <p>{name}</p>
        </div>
    {/if}
{:then imageSrc} 
    {#if value}
        <div class="selected" on:click={onClick} on:keydown={null} in:fade>
            <img src={imageSrc} alt={name}>
            <p>{name}</p>
        </div>
        {:else}
        <div on:click={onClick} on:keydown={null} in:fade>
            <img src={image} alt={name}>
            <p>{name}</p>
        </div>
    {/if}
{/await}

<style lang="scss">
    div {
        background-color: #484848;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        align-items: center;
        border-radius: 0.5em;
        height: 200px;
        width: 150px;
        transition: ease-in-out all 0.2s;
        cursor: pointer;
        margin: 5px;
        font-size: 1.2em;
        text-align: center;

        img {
            max-width: 150px;
            max-height: 150px;
            flex: 1;
            object-fit: contain;
        }

        
    }

    div:hover:not(.selected) {
	    background-color: #252525;
    }

    .selected {
        background-color: white;
	    color: black;
    }
</style>