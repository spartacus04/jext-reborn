<script lang="ts">
	import { base } from '$app/paths';
	import {
		addDownloadQueuePack,
		downloaderLine,
		startDownloadQueue,
		type CommunityPack
	} from '$lib/downloader/downloader';
	import { cAlert, cConfirm } from '$lib/utils';
	import LauncherButton from '../buttons/LauncherButton.svelte';

	const communityPacks = async () => {
		const response = await fetch(`${base}/community-packs`);
		const json = await response.json();

		return json as CommunityPack[];
	};

	const importPack = async (pack: CommunityPack) => {
		await addDownloadQueuePack(pack);
		startDownloadQueue();

		await cAlert('Started downloading community pack...');
	};
</script>

{#await communityPacks()}
	<div class="flex w-full h-full items-center justify-center">
		<h1 class="font-minecraft text-white text-2xl">Fetching community packs</h1>
	</div>
{:then packs}
	{#if packs.length == 0}
		<div class="flex w-full h-full items-center justify-center">
			<h1 class="font-minecraft text-white text-2xl">No community packs available</h1>
		</div>
	{:else}
		<div
			class="grid p-4 pt-2 gap-2 grid-cols-1 xs:grid-cols-2 lg:grid-cols-1 flex-grow overflow-y-auto overflow-x-hidden"
		>
			<div class="flex w-full items-center justify-center">
				<h1 class="font-minecraft text-white text-2xl">Community packs</h1>
			</div>
			{#each packs as pack}
				<div
					class="flex items-center justify-between p-4 bg-surface-separator flex-col lg:flex-row text-white font-minecraft"
				>
					<div class="flex flex-col lg:flex-row items-center w-full flex-shrink lg:max-w-[85%]">
						<img src={pack.icon} alt={pack.name} class="w-28 lg:w-20 lg:h-20 rounded-lg" />
						<div class="lg:ml-4 block flex-shrink overflow-x-auto w-full">
							<h3
								class="h3 text-white font-bold font-minecraft text-lg lg:text-2xl w-full overflow-hidden whitespace-nowrap text-ellipsis"
							>
								{pack.name} by {pack.authors.join(', ')}
							</h3>
							<p
								class="text-gray-400 font-minecraft w-full overflow-hidden whitespace-nowrap text-ellipsis"
							>
								{pack.description}
							</p>
						</div>
					</div>

					<div class="lg:ml-4 flex gap-2 lg:flex-col">
						<LauncherButton
							text="Import"
							type="primary"
							on:click={async () => {
								await importPack(pack);
							}}
						/>
					</div>
				</div>
			{/each}
		</div>
	{/if}
{/await}
