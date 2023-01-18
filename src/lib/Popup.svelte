<script lang="ts">
    import dirt from '../assets/dirt.png';

    import { fade } from 'svelte/transition';

    export let text : string;
    export let active : boolean;

	const close = () => active = false;
</script>

{#if active}
	<div class="popupbackground" style="background-image: url({dirt});" transition:fade on:click={close} on:keydown={null}></div>
{/if}

{#if active}
    <div class="popup">
        <div class="popupcontainer" id="messagepopupcontainer">{text}</div>

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
            padding: 0.5em;
            background-color: #303030;
            border: 1px solid black;
            color: white;
            font-size: 1.5vw;
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