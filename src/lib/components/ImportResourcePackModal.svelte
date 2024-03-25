<script lang="ts">
	import { JextFileChecker, RPChecker } from '$lib/resourcepack/utils';
	import { Stepper, Step, getModalStore, FileDropzone, FileButton } from '@skeletonlabs/skeleton';

	const modalStore = getModalStore();

	let files: FileList;
	let discFileOverride: FileList;
	let resourcePackStatus: string;
	let discsStatus: string;

	$: (async () =>
		(resourcePackStatus =
			files && files.length > 0 && files[0] !== null ? await RPChecker(files[0]) : 'NotValid'))();
	$: (async () =>
		(discsStatus =
			discFileOverride && discFileOverride.length > 0 && discFileOverride[0] !== null
				? await JextFileChecker(discFileOverride[0])
				: 'NotValid'))();

	const onBack = () => {
		if ($modalStore[0]) {
			modalStore.close();
		}
	};

	const complete = async () => {
		if ($modalStore[0]) {
			$modalStore[0]!.response!({
				resourcePack: files[0],
				discs: resourcePackStatus == 'JextRP' ? null : await discFileOverride[0].text()
			});

			modalStore.close();
		}
	};
</script>

<main class="bg-surface-500 rounded-md p-2 h-[50%] overflow-y-auto w-[90%] sm:w-[80%] lg:w-[50%]">
	<Stepper on:complete={complete}>
		<Step locked={resourcePackStatus == 'NotValid'}>
			<svelte:fragment slot="header">Upload a JEXT Resource pack</svelte:fragment>
			<FileDropzone name="ResourcePack" bind:files accept=".zip">
				<svelte:fragment slot="message">
					{#if (resourcePackStatus == 'NotValid' && !files) || files.length == 0}
						<p><b>Upload a resource pack</b> or drag and drop</p>
					{:else if resourcePackStatus == 'NotValid'}
						<p><b>Invalid resource pack</b></p>
					{:else if files && files.length > 0 && files[0] !== null}
						<p><b>{files[0].name}</b> uploaded!</p>
					{/if}
				</svelte:fragment>
				<svelte:fragment slot="meta">
					<p>Only .zip files are allowed</p>
				</svelte:fragment>
			</FileDropzone>

			<svelte:fragment slot="navigation">
				<button class="btn variant-filled" on:click={onBack}>Cancel</button>
			</svelte:fragment>
		</Step>

		<Step locked={resourcePackStatus != 'JextRP' && discsStatus == 'NotValid'}>
			<svelte:fragment slot="header">Upload discs.json</svelte:fragment>
			{#if resourcePackStatus == 'JextRP'}
				<div class="card p-4">Discs.json provided by Resource Pack</div>
			{:else}
				<FileDropzone name="ResourcePack" bind:files={discFileOverride} accept=".json">
					<svelte:fragment slot="message">
						{#if (discsStatus == 'NotValid' && !discFileOverride) || discFileOverride.length == 0}
							<p><b>Upload discs.json</b> or drag and drop</p>
						{:else if discsStatus == 'NotValid'}
							<p><b>Invalid discs.json</b></p>
						{:else if discFileOverride && discFileOverride.length > 0 && discFileOverride[0] !== null}
							<p><b>{discFileOverride[0].name}</b> uploaded!</p>
						{/if}
					</svelte:fragment>
					<svelte:fragment slot="meta">
						<p>Only .json files are allowed</p>
					</svelte:fragment>
				</FileDropzone>

				{#if discsStatus == 'Old'}
					<p class="variant-filled-error p-2 rounded-lg">
						Old version of discs.json detected, loot tables for discs and fragments will be reset!
					</p>
				{/if}
			{/if}
		</Step>
	</Stepper>
</main>
