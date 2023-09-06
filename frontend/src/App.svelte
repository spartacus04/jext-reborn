<script lang="ts">
	import { fade } from 'svelte/transition';
    import { ENDPOINT } from './config';
	import TabContainer from './lib/TabContainer.svelte';
	import DiscTab from './lib/DiscTab.svelte';
	import ConfigTab from './lib/configtab/ConfigTab.svelte';
	import DocsTab from './lib/docsTab.svelte';

	const isServed = async () => {
		try {
			const response = await fetch(`${ENDPOINT}/api/health`);
			const data = await response.json();

			return data.status === 'ok';
		}
		catch (error) {
			return false;
		}
	};
</script>

<main>
	{#await isServed()}
		<div class="blackbox" out:fade />
	{:then isLocal}
		{#if isLocal}
			<TabContainer items={[
				{
					name: 'Discs',
					component: DiscTab,
				},
				{
					name: 'Config',
					component: ConfigTab,
				},
				{
					name: 'API documentation',
					component: DocsTab,
				},
			]} />
		{:else}
			<DiscTab />
		{/if}
	{/await}
</main>

<style lang="scss">
	@import 'styles/crisp.scss';

	.blackbox {
		background-color: black;
		height: 100%;
		width: 100%;
		position: absolute;
		top: 0;
		left: 0;
	}

	main {
		height: 100%;
		width: 100%;
		background-color: #303030;
	}
</style>
