<script lang="ts">
    import Content from './lib/Content.svelte';
    import Header from './lib/Header.svelte';
    import Popup from './lib/Popup.svelte';

	import generate_btn from './assets/generate_btn.png';
	import spinner from './assets/spinner.gif';

    import type { songData } from './config';
    import { generatePack } from './generator';
	import CSelect from './lib/CSelect.svelte';

	let packIcon : string;
	let packName : string;

	let discDataList : songData[] = [];

	let popup = false;
	let type = 'Generate';

	let is_generating = false;

	const showPopup = () => {
		popup = true;
	};

	const generate = async () => {
		if(is_generating) return;

		is_generating = true;
		await generatePack(discDataList, packIcon, packName, type == 'Merge');
		is_generating = false;
	};
</script>

<main>
	{#if popup}
		<Popup text="Add at least a disc" bind:closePopup={popup}></Popup>
	{/if}

	<Header bind:packname={packName} bind:imagesrc={packIcon} />

	<Content bind:discData={discDataList} />

	<div id="footer">
		{#if is_generating}
			<div id="generate_button" style="background-image: url({generate_btn});">
				<img src={spinner} alt="loading" height="32" width="32">
			</div>
		{:else}
			{#if discDataList.length > 0}
				<div id="generate_button" style="background-image: url({generate_btn});" on:click={generate} on:keydown={null} />
			{:else}
				<div id="generate_button" style="background-image: url({generate_btn});" class="grayscale" on:click={showPopup} on:keydown={null} />
			{/if}
			<CSelect options={['Generate', 'Merge']} bind:selected={type} />
		{/if}
		
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