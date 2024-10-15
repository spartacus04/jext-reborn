<script lang="ts">
	import { slide } from "svelte/transition";
    import BottomBar from "./BottomBar.svelte";
	import AddButton from "./AddButton.svelte";
</script>

<div class="flex-1">
    <AddButton />
</div>

<div transition:slide={{ duration: 50 }}>
    <BottomBar />
</div>
