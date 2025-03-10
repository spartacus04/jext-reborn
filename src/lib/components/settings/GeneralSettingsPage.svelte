<script lang="ts">
	import { jukebox_disc_anim } from "$lib/assets";
	import LauncherCheckbox from "../inputs/LauncherCheckbox.svelte";

    const getProperty = (key: string, def: boolean) => {
        const val = localStorage.getItem(key);

        if(val === null) {
            return def;
        }

        return val === 'true';
    }

    let disableAnimations = getProperty('disable-animations', false);
    let generationProgressBarDarkMode = getProperty('generation-progress-bar-dark-mode', false);
    let generationAnimation = getProperty('generation-animation', true);
    let disableAudioTranscoding = getProperty('disable-audio-transcoding', false);

    let accessibilityTextSize = getProperty('accessibility-text-size', false);
    let accessibilityDyslexicFont = getProperty('accessibility-dyslexic-font', false);

    const setProperty = (key: string, value: any) => {
        localStorage.setItem(key, value);
    }

    $: if(accessibilityTextSize) {
        document.documentElement.setAttribute('data-font-size', 'large');
    } else {
        document.documentElement.removeAttribute('data-font-size');
    }

    $: if(accessibilityDyslexicFont) {
        document.documentElement.setAttribute('data-font-type', 'dyslexic');
    } else {
        document.documentElement.removeAttribute('data-font-type');
    }

    const resetSettings = () => {
        localStorage.clear();
        disableAnimations = false;
        generationProgressBarDarkMode = false;
        generationAnimation = true;
        accessibilityTextSize = false;
        accessibilityDyslexicFont = false;
        disableAudioTranscoding = false;
    }
</script>

<div class="flex p-4 gap-2 w-fit text-white flex-col text-md">
    <div class="flex p-4 w-fit items-center">
        <a href="https://github.com/sponsors/spartacus04" target="_blank" class="border border-white pt-1 pb-2 px-5 font-bold hover:bg-[#5c5c5c]">Sponsor the project</a>
        <img src={jukebox_disc_anim} alt="disc anim" class="w-10 ml-4" />
    </div>

    <b>JEXT COMPANION APP SETTINGS</b>

    <LauncherCheckbox disableMinecraftFont={true} small={true} label="Disable animations" bind:value={disableAnimations} on:change={() => setProperty('disable-animations', disableAnimations)} />
    <LauncherCheckbox disableMinecraftFont={true} small={true} label="Generation progress bar dark mode" bind:value={generationProgressBarDarkMode} on:change={() => setProperty('generation-progress-bar-dark-mode', generationProgressBarDarkMode)} />
    <LauncherCheckbox disableMinecraftFont={true} small={true} label="Generation progress bar animation" bind:value={generationAnimation} on:change={() => setProperty('generation-animation', generationAnimation)} />
    <LauncherCheckbox disableMinecraftFont={true} small={true} label="Disable audio file transcoding" bind:value={disableAudioTranscoding} on:change={() => setProperty('disable-audio-transcoding', disableAudioTranscoding)} />

    <b>ACCESSIBILITY SETTINGS</b>

    <LauncherCheckbox disableMinecraftFont={true} small={true} label="Make text size bigger" bind:value={accessibilityTextSize} on:change={() => setProperty('accessibility-text-size', accessibilityTextSize)} />
    <LauncherCheckbox disableMinecraftFont={true} small={true} label="Use font for dyslexic people" bind:value={accessibilityDyslexicFont} on:change={() => setProperty('accessibility-dyslexic-font', accessibilityDyslexicFont)} />

    <button class="bg-[#ffd055] mt-4 pt-1 pb-2 px-5 font-bold text-black" on:click={resetSettings}>
        Reset all settings
    </button>
</div>

