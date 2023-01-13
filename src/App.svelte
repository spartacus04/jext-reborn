<script lang="ts">
    import Content from './lib/Content.svelte';
    import Header from './lib/Header.svelte';
    import Popup from './lib/Popup.svelte';

	import generate_btn from './assets/generate_btn.png';

    import type { songData } from './config';
    import { generatePack } from './generator';
	import CSelect from './lib/CSelect.svelte';
	import { saveAs } from './utils';

	let packIcon : string;
	let packName : string;
	let useMono : boolean;

	let discDataList : songData[] = [];

	let popup = false;
	let type = 'Generate';

	const showPopup = () => {
		popup = true;
	};

	const generate = () => {
		generatePack(discDataList, packIcon, packName, useMono, type == 'Merge');
	};
</script>

<main>
	{#if popup}
		<Popup text="Add at least a disc" bind:closePopup={popup}></Popup>
	{/if}

	<Header bind:packname={packName} bind:imagesrc={packIcon} bind:useMono={useMono}/>

	<Content bind:discData={discDataList} />

	<div id="footer">
		{#if discDataList.length > 0}
			<div id="generate_button" style="background-image: url({generate_btn});" on:click={generate} on:keydown={null} />
		{:else}
			<div id="generate_button" style="background-image: url({generate_btn});" class="grayscale" on:click={showPopup} on:keydown={null} />
		{/if}
		<CSelect options={['Generate', 'Merge']} bind:selected={type} />
	</div>
</main>

<style lang="scss">
	@import 'styles/crisp.scss';

	main {
		height: 100%;
		width: 100%;
		background-color: #303030;
		padding: 0;
		display: flex;
		flex-direction: column;
		justify-content: center;

		#footer {
			display: flex;
			align-items: center;
			justify-content: center;
			padding: 1em;
			background-color: #202020;
			padding: 0.5em;

			#generate_button {
				
				width: 256px;
				height: 64px;
				display: flex;
				align-items: center;
				justify-content: center;

				cursor: pointer;
				background-repeat: no-repeat;
				background-position: center;
				background-size: cover;

				@extend %crisp;

				&:hover:not(.grayscale) {
					filter: drop-shadow(1px 1px 5px green) contrast(130%);
				}
			}
		}
	}
</style>