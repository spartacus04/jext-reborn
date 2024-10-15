<script lang="ts">
    import LauncherButton from "../buttons/LauncherButton.svelte";
    import { discsStore, removeDisc, selectedDiscs } from "$lib/discs/discManager";

    const selectAll = () => {
        selectedDiscs.update((discs) => {
            return discs.length > 0 ? [] : $discsStore;
        });
    };

    const deleteSelected = () => {
        // ALERT: This will delete all selected discs
        if(!confirm("Are you sure you want to delete all selected discs?")) return;

        selectedDiscs.update((discs) => {
            discs.forEach((disc) => {
                removeDisc(disc);
            });
            return [];
        });
    };

    const deselectAll = () => {
        selectedDiscs.set([]);
    };
</script>

<div class="p-4 pb-2 text-white font-minecraft">
    {#if $selectedDiscs.length > 0}
        <LauncherButton text="Deselect All" type="tertiary" on:click={deselectAll} />
        <LauncherButton text="Delete Selected" type="danger" on:click={deleteSelected} />
        <LauncherButton text="Edit Selected" type="primary" />
    {:else}
        <LauncherButton text="Select All" type="tertiary" on:click={selectAll}/>
    {/if}
</div>