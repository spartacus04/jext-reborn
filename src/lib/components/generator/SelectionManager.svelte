<script lang="ts">
	import LauncherButton from '../buttons/LauncherButton.svelte';
	import { discsStore, editDiscs, removeDisc, selectedDiscs } from '$lib/discs/discManager';
	import { get } from 'svelte/store';
	import { cConfirm } from '$lib/utils';

	const selectAll = () => {
		selectedDiscs.update((discs) => {
			return discs.length > 0 ? [] : $discsStore;
		});
	};

	const deleteSelected = async () => {
		const response = await cConfirm({
			text: 'Are you sure you want to delete all selected discs?',
			cancelText: undefined,
			confirmText: 'Yes',
			discardText: 'No'
		});

		if (response === 'discard') return;

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
		<LauncherButton
			text="Edit Selected"
			type="primary"
			on:click={() => editDiscs(...get(selectedDiscs))}
		/>
	{:else}
		<LauncherButton text="Select All" type="tertiary" on:click={selectAll} />
	{/if}
</div>
