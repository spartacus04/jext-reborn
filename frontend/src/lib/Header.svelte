<script lang="ts">
	import { Tooltip, ImportPopup } from '@lib';
	import { outline, inputFile, dropFile } from '@ui';

	import { versions } from '@/config';
	import { versionStore, nameStore, iconStore } from '@/store';


	let import_popup_active = false;


	export let canEdit = true;


	const updateImage = (files : File[]) => {
		if(!files || files.length === 0) return;

		const file = files[0];
		const reader = new FileReader();

		reader.onload = () => {
			const image = new Image();

			image.onload = () => {
				const canvas = document.createElement('canvas');

				canvas.width = 64;
				canvas.height = 64;

				const ctx = canvas.getContext('2d');

				ctx!.drawImage(image, 0, 0, 64, 64);

				iconStore.set(canvas.toDataURL('image/png'));
			};

			image.src = reader.result as string;
		};

		reader.readAsDataURL(file);
	};

	const replaceText = () => {
		nameStore.update(packName => packName
			.replace(' ', '_')
			.replace(/[^a-zA-Z0-9_]/g, '')
			.toLowerCase()
		);
	};
</script>

<ImportPopup bind:active={import_popup_active}/>

<div id="header">
	<Tooltip text="Sets the resourcepack icon">
		{#if canEdit}
			<img use:outline src={$iconStore} alt="pack icon" id="pack_icon" class="noselect" use:inputFile={{ accept: 'image/png', cb: updateImage }}
				use:dropFile={{ accept: 'image/png', cb: updateImage }} on:keypress={null}>
		{:else}
			<img src={$iconStore} alt="pack icon" id="pack_icon" class="noselect" on:keypress={null}>
		{/if}
	</Tooltip>

	{#if canEdit}
		<input type="text" name="pack_name" id="pack_name_input" bind:value={$nameStore} on:input={replaceText}>
	{:else}
		<input type="text" name="pack_name" id="pack_name_input" bind:value={$nameStore} on:input={replaceText} readonly>
	{/if}

	<select name="version" id="version_input" bind:value={$versionStore}>
		{#each [...versions] as [key, value]}
			{#if canEdit}
				<option value={key}>{value}</option>
			{:else}
				{#if key === $versionStore}
					<option value={key}>{value}</option>
				{:else}
					<option value={key} disabled>{value}</option>
				{/if}
			{/if}
		{/each}
	</select>

	<div id="import_container">
		<Tooltip text="Import an existing resourcepack" right={false}>
			{#if canEdit}
				<input type="button" name="import" id="pack_import" value="import" on:click={() => import_popup_active = true}>
			{:else}
				<input type="button" name="import" id="pack_import" value="import">
			{/if}
		</Tooltip>
	</div>
</div>

<style lang="scss">
	%textSettings {
		border-radius: 0;
		color: white;
		font-size: 1.2em;
		padding: 0.5em;
		width: fit-content;

		&:hover {
			background-color: #404040;
		}
	}

	#header {
		display: flex;
		align-items: center;
		padding: 1em;
		background-color: #202020;

		#pack_icon {
			cursor: pointer;
		}

		#pack_name_input, select, #pack_import {
			@extend %textSettings;
			margin-left: 1em;
			background-color: #303030;
		}

		#import_container {
			justify-self: flex-end;
			margin-left: auto;
		}
	}
</style>