<script lang="ts">
	import LauncherButton from '../buttons/LauncherButton.svelte';

	import { downloadQueue, type DownloadQueueElement } from '$lib/downloader/downloader';

	export let elem: DownloadQueueElement;

	const removeElem = (elem: DownloadQueueElement) => {
        downloadQueue.update((queue) => queue.filter((e) => e !== elem));
    };

	let update = {};
</script>

<div
	class="flex items-center justify-between p-4 bg-surface-separator flex-col lg:flex-row text-white font-minecraft"
>
	{#key update}
		<div class="flex flex-col lg:flex-row items-center w-full ml-7 flex-shrink lg:max-w-[85%]">
			<img src={elem.iconUrl} alt={elem.url} class="w-28 lg:w-20 lg:h-20 rounded-lg" />
			<div class="lg:ml-4 block flex-shrink overflow-x-auto">
				<h3
					class="h3 text-white font-bold font-minecraft text-lg lg:text-2xl w-full overflow-hidden whitespace-nowrap text-ellipsis"
				>
					{elem.displayName ?? elem.url}
				</h3>
				<p
					class="text-gray-400 font-minecraft w-full overflow-hidden whitespace-nowrap text-ellipsis"
				>
					{elem.status}
				</p>
			</div>
		</div>
	{/key}
	<div class="lg:ml-4 flex gap-2 lg:flex-col">
        {#if elem.status == 'Download pending...'}
		    <LauncherButton text="Remove from queue" type="danger" on:click={() => removeElem(elem)} />
        {/if}
	</div>
</div>
