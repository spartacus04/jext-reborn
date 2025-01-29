<script lang="ts">
	import { discsStore } from '$lib/discs/discManager';
	import DiscElement from './DiscElement.svelte';
</script>

<div class="grid p-4 pt-2 gap-2 grid-cols-1 xs:grid-cols-2 lg:grid-cols-1">
	{#each $discsStore as disc}
		<DiscElement {disc} />
	{/each}
</div>
