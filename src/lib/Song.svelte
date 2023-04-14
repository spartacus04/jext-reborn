<script lang="ts">
	import { Tooltip, DungeonPopup, DungeonFragmentPopup } from '@lib';

	import { dropFile, hoversrc, inputFile, outline, restartAnim } from '@ui';

	import { convertToOgg } from '@/ffmpeg';
	import type { SongData } from '@/config';
	import { versionStore } from '@/store';

	import { default_disc, loading, creeper, chest, delete_btn, delete_btn_hover, fragment_icon, audio_btn, audio_btn_selected, default_fragment } from '@assets';


	export let song : SongData;
	export let id : number;
	export let onRemove = () => { null; };


	let dungeon_popup_active = false;
	let dungeon_fragment_popup_active = false;


	const regenNamespace = () => {
		song.namespace = `${song.name}${song.author}${id}`
			.replace(/[^a-zA-Z0-9]/g, '')
			.replaceAll('1', 'one')
			.replaceAll('2', 'two')
			.replaceAll('3', 'three')
			.replaceAll('4', 'four')
			.replaceAll('5', 'five')
			.replaceAll('6', 'six')
			.replaceAll('7', 'seven')
			.replaceAll('8', 'eight')
			.replaceAll('9', 'nine')
			.replaceAll('0', 'zero')
			.toLowerCase();
	};

	const prepareDisc = async () : Promise<void> => {
		if(song.name === '' && song.author === '') {
			const splitName = song.uploadedFile.name.replace(/(\.mp3)|(\.ogg)|(\.wav)/g, '').split(/ ?- ?/g);

			if(splitName.length >= 1) song.name = splitName.shift();
			if(splitName.length >= 1) song.author = splitName.join();
		}

		regenNamespace();

		song.oggFile = await convertToOgg(song.uploadedFile);

		song.fragmentTexture = await (await fetch(default_fragment)).blob();

		song.texture = await (await fetch(default_disc)).blob();
	};

	const prepareDiscPromise = prepareDisc();


	const setTexture = (files: File[]) => {
		const file = files![0];

		if (file) {
			song.texture = file;
		}
	};
</script>

<DungeonPopup bind:selectedDungeons={song.lootTables} bind:active={dungeon_popup_active}/>
<DungeonFragmentPopup bind:selectedDungeons={song.fragmentLootTables} bind:active={dungeon_fragment_popup_active} bind:texture={song.fragmentTexture}/>

<div id="song">
	{#await prepareDiscPromise}
		<div id="loading">
			<img use:restartAnim={loading} alt="loading">
		</div>
	{:then _}

		<div id="icons">
			<Tooltip text="Changes the disc icon">
				<img use:outline id="disc_texture" src={URL.createObjectURL(song.texture)}
					height="64" width="64" alt="disc icon"
					use:inputFile={{ accept: 'image/*', cb: setTexture }} on:keydown={null}
					use:dropFile={{ accept: 'image/*', cb: setTexture }}
				>
			</Tooltip>

			<div id="buttons">
				<Tooltip text="Toggles Creeper drops">
					<img use:outline id="toggle_creeper" src={creeper}
						alt="creeper icon" on:click={() => song.creeperDrop = !song.creeperDrop} on:keydown={null}
						class="{song.creeperDrop ? '' : 'grayscale'}"
					>
				</Tooltip>
				<Tooltip text="Selects structures in which the disc can be found">
					<img use:outline id="loot_selector" src={chest} alt="chest icon"
						on:click={() => dungeon_popup_active = true} on:keydown={null}
					>
				</Tooltip>
				{#if $versionStore >= 9}
					<Tooltip text="Selects structures in which the disc fragments can be found">
						<img use:outline id="loot_fragment_selector" src={fragment_icon}
							alt="fragment icon" on:click={() => dungeon_fragment_popup_active = true} on:keydown={null}
						>
					</Tooltip>
				{:else}
					<Tooltip text="Not available in versions lower than 1.19">
						<img use:outline id="loot_fragment_selector" src={fragment_icon}
							alt="fragment icon" class="disabled"
						>
					</Tooltip>
				{/if}
				<Tooltip
					text="(M)ono: single audio channel but music fading<br>(S)tereo: multiple audio channels but no music fading"
					width="22em"
				>
					<p use:outline id="toggle_mono" on:click={() => song.isMono = !song.isMono} on:keydown={null}>
						{song.isMono ? 'M' : 'S'}
					</p>
				</Tooltip>
				<Tooltip text="Enabled: Normalizes the audio<br>Disabled: Keeps the song as is" width="15em">
					<img use:outline src={song.normalize ? audio_btn_selected : audio_btn} id="audio_normalize"
						alt="audio icon" on:click={() => song.normalize = !song.normalize} on:keydown={null}
					>
				</Tooltip>
				<Tooltip text="Removes the disc">
					<img use:outline use:hoversrc={{ src: delete_btn, hover: delete_btn_hover }} id="song_delete"
						alt="delete icon" on:click={onRemove} on:keydown={null}
					>
				</Tooltip>
			</div>
		</div>
		<div id="names">
			<input type="text" name="song_name" id="song_name_input" bind:value={song.name} on:input={regenNamespace}>
			<input type="text" name="song_author" id="song_author_input" bind:value={song.author} on:input={regenNamespace}>
			<p id="disknamespace">
				{#if song.namespace.length > 90}
					{song.namespace.substring(0, 90)}...
				{:else}
					{song.namespace}
				{/if}
			</p>
		</div>
		<textarea name="song_lore" id="song_lore_input" cols="30" rows="6" bind:value={song.lores}></textarea>
	{/await}
</div>

<style lang="scss">

	#song {
		display: flex;
		align-items: center;
		justify-content: space-between;
		color: white;
		font-size: 1.2em;
		border-bottom: 1px solid #808080;

		#loading {
			display: flex;
			justify-content: center;

			flex: 1;

			img {
				max-width: none;
			}
		}

		#icons {
			display: flex;

			img {
				background-color: #202020;

				padding: 12px;
				cursor: pointer;
			}

			#buttons {
				display: flex;
				margin-left: 1em;
				width: 60px;
				height: 90px;
				flex-direction: column;
				justify-content: space-between;
				flex-wrap: wrap;

				img {
					width: 24px;
					height: 24px;
					padding: 2px;

					margin-bottom: -5px;
				}

				#toggle_mono {
					width: 24px;
					height: 24px;
					padding: 2px;
					margin: 0;
					font-size: 27px;
					font-family: 'Minecraft';

					user-select: none;

					text-align: center;
					background-color: #202020;

					cursor: pointer;
				}
			}
		}

		#names {
			display: flex;
			flex-direction: column;

			input {
				width: 30em;
				margin: 0.5em 0 0 0;
				border-radius: 0;
				border: 1px solid #808080;
				background-color: #202020;
				color: white;
				font-size: 0.7em;
				padding: 0.5em;
			}

			p {
				color: #808080;
				font-size: 0.6em;
				width: 30em;
			}
		}

		textarea {
			resize: none;
			background-color: #202020;
			color: white;
		}
	}

	.disabled {
		opacity: 0.5;
	}
</style>