<script lang="ts">
	import { slide } from "svelte/transition";
    import BottomBar from "./BottomBar.svelte";
	import AddButton from "./AddButton.svelte";
	import DiscsList from "./DiscsList.svelte";
	import SelectionManager from "./SelectionManager.svelte";
	import { discsStore } from "$lib/discs/discManager";
</script>

<div class="flex-grow overflow-y-auto" id="main">
    {#if $discsStore.length > 0} 
        <SelectionManager />
        <hr class="mx-4 border-surface-separator">
        <DiscsList />
    {/if}
    <AddButton />
</div>

<div class="" in:slide={{ duration: 50 }}>
    <BottomBar />
</div>