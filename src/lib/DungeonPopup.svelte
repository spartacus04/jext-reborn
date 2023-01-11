<script lang="ts">
    import DungeonElement from './DungeonElement.svelte';
    import { fade } from 'svelte/transition';

    import dirt from '../assets/dirt.png';

    import { dungeons } from '../config';
    import { versionStore } from '../store';

    export let selectedDungeons : string[] = []
    export let closePopup : boolean;
    

    const selectItem = (value : string) => {
        if(selectedDungeons.includes(value)) selectedDungeons = selectedDungeons.filter(e => e != value);
        else selectedDungeons = [...selectedDungeons, value];
    };

    const close = () => {
    	closePopup = false;
        (<HTMLElement>document.querySelector('.popup')).style.display = 'none';
    };
</script>

<div class="popupbackground" style="background-image: url({dirt});" transition:fade on:click={close} on:keydown={null}></div>

{#if closePopup}
    <div class="popup">
        <div class="popupcontainer">
            {#each dungeons as dungeon}
                {#if dungeon.minVersion && dungeon.minVersion <= $versionStore}
                    <DungeonElement image={dungeon.img} name={dungeon.name} value={selectedDungeons.includes(dungeon.source)} onClick={() => selectItem(dungeon.source)}/>
                {/if}
            {/each}
        </div>
        <div id="messagepopupconfirm" class="popupconfirm" on:click={close} on:keydown={null}>
            <div id="content">
                <p id="generate_text" class="noselect">OK</p>
            </div>
        </div>
    </div>
{/if}

<style lang="scss">
    @import '../styles/crisp.scss';

    .popup {
        z-index: 101;
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
        background-color: #202020;
        padding: 1.5em;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;

        .popupcontainer {
            display: flex;
            flex-flow: row wrap;
            align-items: flex-start;
            padding: 0;
            width: 816px;
            max-width: 816px;
            max-height: 420px;
            overflow-y: scroll;

            padding: 0.5em;
            background-color: #303030;
            border: 1px solid black;
            color: white;
        }

        .popupconfirm {
            display: flex;
            align-items: center;
            justify-content: center;

            cursor: pointer;
            margin-top: 2em;
            width: min-content;
            font-size: 1vw;
            border: 1px solid white;
            color: white;
            height: min-content;
            padding: 0.5em 1em 0.5em 1em;

            transition: ease-in-out all 0.4s;
        }

        .popupconfirm:hover,
        .popupconfirm:hover>#content>#generate_text {
            color: black;
            background-color: white;
        }
    }

    .popupbackground {
        z-index: 100;
        background-repeat: repeat;
        background-size: 10% auto;
        position: absolute;
        width: 100%;
        height: 100%;
        top: 0;
        left: 0;

        transition: ease-in-out all 0.2s;

        @extend %crisp;
    }
</style>