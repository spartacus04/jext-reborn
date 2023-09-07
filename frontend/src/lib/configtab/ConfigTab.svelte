<script lang="ts">
	import PrimaryButton from '../utils/PrimaryButton.svelte';
	import ConfigElement from './ConfigElement.svelte';
	import { configStore, fetchConfigData, saveConfigData } from './config';

	import { spinner, dark_dirt_background } from '@/assets';

	let reload = fetchConfigData();

	const saveConfig = () => {
		reload = saveConfigData().then(() => {
			reload = fetchConfigData();
		}).catch((err) => {
			alert(err);
		});
	};
</script>

<div class="header">
	<h1>Plugin configuration</h1>
	<h4>Here you can configure the plugin to your likings</h4>
</div>
<div class="configOptionsContainer" style:background-image="url({dark_dirt_background})">
	{#await reload}
		<img src={spinner} alt="Loading...">
	{:then _}
		{#each $configStore as configNode}
			{#if configNode}
				<ConfigElement bind:configNode={configNode} />
			{/if}
		{/each}
	{/await}
</div>
<div class="footer">
	<PrimaryButton on:click={() => { reload = fetchConfigData(true); }}>
		<div class="btn">Cancel</div>
	</PrimaryButton>
	<PrimaryButton on:click={saveConfig}>
		<div class="btn">Apply</div>
	</PrimaryButton>
</div>

<style lang="scss">
	@import '../../styles/crisp.scss';

	.header {
		display: flex;
		flex-direction: column;
		justify-content: baseline;
		align-items: baseline;
		background-color: #202020;
		width: calc(100% - 1em);
		padding: 0.5em;

		* {
			margin: 0.5em 0.5em 0.5em 0.5em;
			font-family: 'minecraft_regular';
			text-shadow: 1px 1px 3px black;

			height: min-content;
			width: clamp(2em, auto, 20em);

		}

		h1 {
			color: white;
		}

		h4 {
			color: #a0a0a0;
		}
	}

	.configOptionsContainer {
		@extend %crisp;

		flex: 1;
		display: flex;
		flex-direction: column;
		align-items: center;
		background-size: 64px 64px;

		overflow-y: scroll;

		padding: 0 2em 2em 2em;

		@media screen and (max-width: 375px) {
			padding: 0 1em 2em 1em;
		}

		img {
			width: 2em;
			height: 2em;
		}
	}

	.footer {
		display: flex;
		align-items: end;
		justify-content: space-around;
		background-color: #202020;

		padding: 1em;
	}

	.btn {
		font-family: 'minecraft_ten';
		font-size: 2em;
		color: white;
		transition: ease-in-out all .4s;
	}
</style>