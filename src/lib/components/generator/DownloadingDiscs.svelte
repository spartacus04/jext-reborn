<script lang="ts">
	import { downloadQueue } from '$lib/downloader/downloader';
	import DownloadingDiscElement from './DownloadingDiscElement.svelte';
</script>

<div class="grid p-4 pt-2 gap-2 grid-cols-1 xs:grid-cols-2 lg:grid-cols-1">
	{#each $downloadQueue as elem}
		<DownloadingDiscElement {elem} />
	{/each}
</div>
