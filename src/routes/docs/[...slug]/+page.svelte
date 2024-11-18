<script lang="ts">
	import LauncherButton from "$lib/components/buttons/LauncherButton.svelte";
    import type { PageData } from "./$types";
    import { base } from '$app/paths';

    export let data: PageData;
</script>

<div class="p-4 w-full h-full">
    {#if data.isFound}
        {@html data.text}
    {:else}
        <div class="h-full flex items-center justify-center flex-col text-white font-minecraft">
            <div class="text-center">
                <p class="text-4xl font-minecraft-launcher text-white">We couldn't find what you were searching for</p>
                <p class="text-[#aeaeae] text-xs font-bold mb-2 font-sans">404 ERROR</p>
                <a href="{base}/docs">
                    <LauncherButton text="Go back to documentation" type="primary" />
                </a>
            </div>
        </div>
    {/if}
</div>
