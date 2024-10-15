<script lang="ts">
	import { isTauri } from "$lib/state";
	import TopBarItem from "./TopBarItem.svelte";

    const pages : {[key: string]: boolean} = {
        'Discs': false,
        'Resource pack manager': false,
        'Bulk import': true,
        'Community packs': false,
        'Recent exports': true,
    };

    export let currentPage: string = Object.keys(pages)[0];
</script>

<div class="flex flex-col bg-surface-background w-full pt-4 px-8">
    <b class="text-white text-xs">MANAGE CUSTOM DISCS</b>

    <div class="flex w-full items center gap-2 overflow-x-auto mt-1">
        {#each Object.keys(pages) as page}
            {#if (pages[page] && isTauri) || (!pages[page])}
                <TopBarItem active={ currentPage === page} {page} on:click={() => currentPage = page} />
            {/if}
        {/each}
    </div>
</div>
