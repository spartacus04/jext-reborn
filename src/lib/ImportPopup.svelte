<script lang="ts">
	import { fade } from 'svelte/transition';

	import { inputFile } from '@ui';

	import { importResourcePack, isDiscsJson, isMinecraftRP } from '@/importer';

	import { dirt, pack_icon, default_disc } from '@assets';


	export let active : boolean;


	let pack_status = 'ready';
	let pack_name = 'Resource pack';
	let pack_file : File;

	let disc_status = 'ready';
	let disc_name = 'discs.json';
	let disc_file : File;

	let isImporting = false;


	$: forbid = pack_status !== 'success' || disc_status !== 'success' ? 'forbid' : '';


	const import_pack = async (files : FileList) => {
		if(!files || files.length === 0) return;

		const file = files[0];

		if(file.name.length > 13) pack_name = file.name.substring(0, 10) + '...';
		else pack_name = file.name;

		if(await isMinecraftRP(file)) {
			pack_status = 'success';
		}
		else {
			pack_status = 'error';
		}

		pack_file = file;
	};

	const import_discs = async (files: FileList) => {
		if(!files || files.length === 0) return;

		const file = files[0];

		if(file.name.length > 13) disc_name = file.name.substring(0, 10) + '...';
		else disc_name = file.name;

		if(await isDiscsJson(file)) {
			disc_status = 'success';
		}
		else {
			disc_status = 'error';
		}

		disc_file = file;
	};

	const importRP = async () => {
		if(pack_status === 'success' && disc_status === 'success' && !isImporting) {
			isImporting = true;
			await importResourcePack(disc_file, pack_file);
			close();
		}
	};

	const close = () => active = false;
</script>

{#if active}
	<div class="popupbackground" style:background-image="url({dirt})" transition:fade on:click={close} on:keydown={null}></div>
{/if}

{#if active}
	<div class="popup">
		<div class="popupcontainer">
			<div use:inputFile={{ accept: '.zip', cb: import_pack }} on:keydown={null} class={pack_status} in:fade>
				<img src={pack_icon} alt="">
				<p id="error">Not Valid</p>
				<p>{pack_name}</p>
			</div>
			<div use:inputFile={{ accept: '.json', cb: import_discs }} on:keydown={null} class={disc_status} in:fade>
				<input type="file" name="discs_input" id="discs_input" accept=".json">
				<img src={default_disc} alt="">
				<p id="error">Not Valid</p>
				<p>{disc_name}</p>
			</div>
		</div>
		<p style="color:red;margin-bottom: 0;">
			Warning: Importing a resource pack will overwrite any existing resource pack in the tool!
		</p>
		<div id="input">
			<div id="messagepopupconfirm" class="popupconfirm {forbid}" on:click={() => importRP()} on:keydown={null}>
				<p id="generate_text" class="noselect">Import</p>
			</div>
			<div id="messagepopupconfirm" class="popupconfirm" on:click={close} on:keydown={null}>
				<p id="generate_text" class="noselect">Discard</p>
			</div>
		</div>
	</div>
{/if}

<style lang="scss">
	@import '../styles/crisp.scss';

	input {
		display: none;
	}

	.popup {
		z-index: 101;
		position: absolute;
		left: 50%;
		top: 50%;
		transform: translate(-50%, -50%);
		background-color: #202020;
		padding: 1.5em;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;

		.popupcontainer {
			display: flex;
			flex-direction: row;
			align-items: flex-start;
			padding: 0;
			width: 300px;
			max-width: 300px;
			max-height: 420px;

			padding: 0.5em;
			background-color: #303030;
			border: 1px solid black;
			color: white;

			&>div {
				background-color: #484848;
				display: flex;
				flex-direction: column;
				justify-content: flex-end;
				align-items: center;
				border-radius: 0.5em;
				height: 200px;
				width: 150px;
				transition: ease-in-out all 0.2s;
				cursor: pointer;
				margin: 5px;
				font-size: 1.2em;
				text-align: center;

				img {
					justify-self: center;
					max-width: 100px;
					max-height: 100px;
					flex: 1;
					object-fit: contain;
				}

				&.success {
					background-color: #00ff00;
					color: black;
				}

				&.error {
					background-color: #ff0000;
					color: white;

					#error {
						display: block;
					}
				}
			}
		}

		.popupconfirm {
			display: flex;
			align-items: center;
			justify-content: center;

			cursor: pointer;
			margin-top: 2em;
			width: min-content;
			font-size: 1vw;
			border: 1px solid white;
			color: white;
			height: min-content;
			padding: 0.5em 1em 0.5em 1em;

			transition: ease-in-out all 0.4s;
		}

		.popupconfirm:nth-child(1) {
			margin-right: 1em;
		}

		.popupconfirm:hover,
		.popupconfirm:hover>#generate_text {
			color: black;
			background-color: white;
		}

		#input {
			display: flex;
			flex-direction: row;
		}
	}

	.popupbackground {
		z-index: 100;
		background-repeat: repeat;
		background-size: 10% auto;
		position: absolute;
		width: 100%;
		height: 100%;
		top: 0;
		left: 0;

		transition: ease-in-out all 0.2s;

		@extend %crisp;
	}

	#error {
		margin: 0.5em 0 -0.5em;
		display: none;
	}

	.forbid {
		pointer-events: none;
		cursor: not-allowed;
		opacity: 0.5;
	}

	.noselect {
		user-select: none;
	}

	#generate_text {
		margin: 0;
	}
</style>