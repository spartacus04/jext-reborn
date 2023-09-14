<script lang="ts">
	import { ClickableImage, Textbox, LauncherCombobox, AddDiscPopup } from '@lib';
	import { pack_icon } from '@assets';
	import { resourcePackStore } from './discs';
	import { versions } from '@/constants';
	import { dropFile } from '@ui';

	export let discPopupOpen = false;

	const addDiscs = (files: File[]) => {
		console.log(files);
	};
</script>

<AddDiscPopup bind:open={discPopupOpen} cb={addDiscs} />

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
	<div class="addsongsbtn" use:dropFile={{ accept: import.meta.env.PROD ? 'audio/*' : '.ogg', cb: null, multiple: true }} on:click={() => discPopupOpen = true}>
		<h1>+</h1>
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
				user-select: none;
			}

			&:hover {
				background-color: #404040;
			}
		}
	}
</style>