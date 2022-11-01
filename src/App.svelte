<script lang="ts">
    import Content from './lib/Content.svelte';
    import Header from './lib/Header.svelte';
    import Popup from './lib/Popup.svelte';

	import generate_btn from './assets/generate_btn.png';

    import type { songData } from './config';
    import { generatePack } from './generator';


	let packIcon : string;
	let packName : string;
	let packVersion : number;
	let useMono : boolean;

	let discDataList : songData[] = [];

	let popup = false;

	const showPopup = () => {
		popup = true;
	};

	const generate = () => {
		generatePack(discDataList, packVersion, packIcon, packName, useMono);
	};
</script>

<main>
	{#if popup}
		<Popup text="Add at least a disc" bind:closePopup={popup}></Popup>
	{/if}

	<Header bind:packname={packName} bind:version={packVersion} bind:imagesrc={packIcon} bind:useMono={useMono}/>

	<Content bind:discData={discDataList} bind:version={packVersion}/>

	<div id="footer">
		{#if discDataList.length > 0}
			<div id="generate_button" style="background-image: url({generate_btn});" on:click={generate} on:keydown={null}>
				<p id="generate_text" class="noselect">GENERATE</p>
			</div>
		{:else}
			<div id="generate_button" style="background-image: url({generate_btn});" class="grayscale" on:click={showPopup} on:keydown={null}>
				<p id="generate_text" class="noselect">GENERATE</p>
			</div>
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

				#generate_text {
					font-family: 'minecraft';
					color: white;
					font-size: 2em;
					padding: 0;
					margin: 0;

					transition: ease-in-out all 0.4s;
				}
			}
		}
	}
</style>