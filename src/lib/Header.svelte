<script lang="ts">
	import { Tooltip, ImportPopup } from '@lib';
	import { outline, inputFile } from '@ui';

	import { versions } from '@/config';
	import { versionStore } from '@/store';

	import { pack_icon } from '@assets';


	export let packname = 'your_pack_name';
	export let imagesrc = pack_icon;


	let import_popup_active = false;


	const updateImage = (files : FileList) => {
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

				imagesrc = canvas.toDataURL('image/png');
			};

			image.src = reader.result as string;
		};

		reader.readAsDataURL(file);
	};

	const replaceText = () => {
		packname = packname
			.replace(' ', '_')
			.replace(/[^a-zA-Z0-9_]/g, '')
			.toLowerCase();
	};
</script>

<ImportPopup bind:active={import_popup_active}/>

<div id="header">
	<Tooltip text="Sets the resourcepack icon">
		<img use:outline src={pack_icon} alt="pack icon" id="pack_icon" class="noselect" use:inputFile={{ accept: 'image/png', cb: updateImage }} on:keypress={null}>
	</Tooltip>

	<input type="text" name="pack_name" id="pack_name_input" bind:value={packname} on:input={replaceText}>

	<select name="version" id="version_input" bind:value={$versionStore}>
		{#each [...versions] as [key, value]}
			<option value={key}>{value}</option>
		{/each}
	</select>

	<div id="import_container">
		<Tooltip text="Import an existing resourcepack" right={false}>
			<input type="button" name="import" id="pack_import" value="import" on:click={() => import_popup_active = true}>
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