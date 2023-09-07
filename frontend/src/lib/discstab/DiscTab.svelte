<script lang="ts">
	import ClickableImage from '../utils/ClickableImage.svelte';
	import { pack_icon } from '@assets';
	import Textbox from '../utils/Textbox.svelte';
	import { resourcePackStore } from './discs';
	import LauncherCombobox from '../utils/LauncherCombobox.svelte';
	import { versions } from '@/constants';
	import { inputFile } from '@/ui/inputfile';

</script>

<div class="header">
	<ClickableImage imageUrl={pack_icon} bind:image={$resourcePackStore.icon} />
	<div class="names">
		<Textbox bind:value={$resourcePackStore.description} placeholder="Resource pack description" fontsize="1.2em" />
		<LauncherCombobox bind:selected={$resourcePackStore.version} options={versions} />
	</div>
</div>
<div class="content">
	<div id="songscontainer">

	</div>
	<hr class="hidden">
	<div class="addsongsbtn" use:inputFile={{ accept: import.meta.env.PROD ? 'audio/*' : '.ogg', cb: null, multiple: true, drag: true }}>
		<h1 class="noselect">+</h1>
	</div>
</div>

<style lang="scss">
	.header {
		background-color: #202020;
		height: min-content;
		padding: 1em;
		display: flex;
		align-items: center;

		.names {
			display: flex;
			flex-direction: row;
			justify-content: space-between;
			margin-left: 1em;
			height: min-content;
			gap: 1em;

			@media screen and (max-width: 600px) {
				width: clamp(9em, 50%, 20em);
				flex-direction: column;
				gap: 0.5em;
			}
		}
	}

	.content {
		flex-grow: 1;
		padding: 1em;
		overflow-y: auto;
		height: max-content;

		.addsongsbtn {
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