<script lang="ts">
	import { Song } from '@lib';
	import { inputFile } from '@ui';

	import { discStore } from '@/store';
	import type { SongData } from '@/config';


	const addSong = (files : FileList) => {
		if(!files || files.length === 0) return;

		const tempDiscData : SongData[] = [];

		for(let i = 0; i < files.length; i++) {
			tempDiscData.push({
				uploadedFile: files[i],
				name: 'Disc Name',
				author: 'Disc Author',
				lores: 'This is the lore of the disc\n\nYou can have multiple lines\n\nIf you don\'t want any lores you can leave this empty',
				texture: null,
				isMono: true,
				normalize: false,
				creeperDrop: true,
				lootTables: [],
				monoFile: null,
				namespace: '',
				oggFile: null,
				fragmentLootTables: [],
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
	<div id="addsongsbtn" use:inputFile={{ accept: import.meta.env.PROD ? 'audio/*' : '.ogg', cb: addSong }} on:keydown={null}>
		<h1 class="noselect">+</h1>
	</div>
</div>

<style lang="scss">
	#content {
		flex-grow: 1;
		padding: 1em;
		overflow-y: auto;
		height: max-content;

		#addsongsbtn {
			background-color: #484848;
			height: 64px;
			margin: 0;
			display: flex;
			align-items: center;
			justify-content: center;
			border: 2px solid black;

			cursor: pointer;

			h1 {
				color: #d3d3d3;
			}

			&:hover {
				background-color: #404040;
			}
		}
	}
</style>