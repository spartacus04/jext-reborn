<script lang="ts">
	import { Song } from '@lib';
	import { dropFile, inputFile } from '@ui';

	import { discStore } from '@/store';
	import type { SongData } from '@/config';

	let songUrl = '';

	const downloadSong = () => {
		discStore.update(discs => discs = [...discs, {
			downloadLink: songUrl,
			uploadedFile: null,
			name: 'Disc Name',
			author: 'Disc Author',
			lores: 'This is the lore of the disc\n\nYou can have multiple lines\n\nIf you don\'t want any lores you can leave this empty',
			namespace: '',
			creeperDrop: true,
			lootTables: [],
			fragmentLootTables: [],
			normalize: false,
			isMono: true,
			oggFile: null,
			monoFile: null,
			texture: null,
			fragmentTexture: null,
		}]);

		songUrl = '';
	};

	const addSong = (files : File[]) => {
		if(!files || files.length === 0) return;

		const tempDiscData : SongData[] = [];

		for(let i = 0; i < files.length; i++) {
			tempDiscData.push({
				uploadedFile: files[i],
				name: 'Disc Name',
				author: 'Disc Author',
				lores: 'This is the lore of the disc\n\nYou can have multiple lines\n\nIf you don\'t want any lores you can leave this empty',
				namespace: '',
				creeperDrop: true,
				lootTables: [],
				fragmentLootTables: [],
				normalize: false,
				isMono: true,
				oggFile: null,
				monoFile: null,
				texture: null,
				fragmentTexture: null,
			});
		}

		discStore.update(discs => discs = [...discs, ...tempDiscData]);
	};

	const removeSong = (song : SongData) => discStore.update(discs => discs = discs.filter(e => e != song));
</script>

<div id="content">
	<div id="songscontainer">
		{#each $discStore as song, i}
			<Song id={i} bind:song={song} onRemove={() => removeSong(song)}></Song>
		{/each}
	</div>
	<hr class="hidden">
	<div class="addsongsbtn" >
		<div class="songaddbtn" use:inputFile={{ accept: import.meta.env.PROD ? 'audio/*' : '.ogg', cb: addSong, multiple: true }}
			use:dropFile={{ accept: import.meta.env.PROD ? 'audio/*' : '.ogg', cb: addSong, multiple: true }} on:keydown={null}>
			<h1 class="noselect">+</h1>
		</div>
		<div class="songdownloaddiv">
			<p>Or download from Youtube</p>
			<div>
				<input type="text" placeholder="Youtube link" bind:value={songUrl}>
				<button on:click={downloadSong}>Download</button>
			</div>
		</div>
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

	.songdownloaddiv {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		width: 100%;
		height: 95px;

		input, button {
			@extend %textSettings;
			margin-left: 1em;
			background-color: #303030;
		}

		input {
			width: 25em;
		}

		p {
			margin: 10px;
			font-size: 20px;
			color: white;
			font-family: 'VT323';
		}
	}

	.songaddbtn {
		border-bottom: 2px solid #202020;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		width: 100%;
		height: 64px;
		cursor: pointer;

		&:hover {
			background-color: #404040;
		}
	}

	#content {
		flex-grow: 1;
		padding: 1em;
		overflow-y: auto;
		height: max-content;

		.addsongsbtn {
			flex-direction: column;
			background-color: #484848;
			margin: 0;
			display: flex;
			align-items: center;
			justify-content: center;
			border: 2px solid black;
			height: 160px;


			h1 {
				color: #d3d3d3;
			}
		}
	}
</style>