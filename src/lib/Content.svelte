<script lang="ts">
    import Song from "./Song.svelte";

    export let discs : File[] = [];

    const addSong = () => {
        const input = document.createElement('input');
		input.type = 'file';
		input.multiple = true;
		input.accept = import.meta.env.PROD ? 'audio/*' : '.ogg';
		input.click();

		input.addEventListener('change', async () => {
			if(!input.files || input.files.length === 0) return;
            
            discs = [...discs, ...input.files]
		});
    };
</script>

<div id="content">
    <div id="songscontainer">
        {#each discs as disc, i}
            <Song uploadedFile={disc} id={i}></Song>
        {/each}
    </div>
    <hr class="hidden">
    <div id="addsongsbtn" on:click={addSong} on:keydown={null}>
        <h1 class="noselect">+</h1>
    </div>
</div>

<style lang="scss">
    #content {
        flex-grow: 1;
        padding: 1em;
        overflow-y: auto;
        height: max-content;

        #addsongsbtn {
            background-color: #484848;
            height: 64px;
            margin: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            border: 2px solid black;

            cursor: pointer;

            h1 {
                color: #d3d3d3;
            }
        }
    }
</style>