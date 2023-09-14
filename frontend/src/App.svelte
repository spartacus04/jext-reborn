<script lang="ts">
	import { fade } from 'svelte/transition';
    import { ENDPOINT } from './constants';
	import { TabContainer, DiscTab, ConfigTab, DocsTab } from '@lib';
	import { isServed } from './state';

	const isServedFn = async () => {
		try {
			const response = await fetch(`${ENDPOINT}/api/health`);
			const data = await response.json();

			isServed.set(data.status === 'ok');
			return $isServed;
		}
		catch (error) {
			return false;
		}
	};
</script>

<main>
	{#await isServedFn()}
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
			<TabContainer items={[
				{
					name: 'Discs',
					component: DiscTab,
				},
				{
					name: 'API documentation',
					component: DocsTab,
				},
			]} />
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

	:global(.tooltip) {
		white-space: nowrap;
		position: relative;
		padding-top: 0.35rem;
		cursor: zoom-in;
		border-bottom: 1px solid currentColor;
	}

	:global(#tooltip) {
		position: absolute;
		bottom: 100%;
		right: 0.78rem;
		display: inline-block;
		transform: translate(50%, 0);
		padding: 0.2rem 0.35rem;
		background: hsl(0, 0%, 20%);
		color: hsl(0, 0%, 98%);
		font-size: 0.95em;
		font-family: 'minecraft_regular';
		text-shadow: 1px 1px 5px black;
		border-radius: 0.25rem;
		filter: drop-shadow(0 1px 2px hsla(0, 0%, 0%, 0.2));
		width: max-content;
	}

	:global(.tooltip:not(:focus) #tooltip::before) {
		content: '';
		position: absolute;
		top: 100%;
		left: 50%;
		transform: translateX(-50%);
		width: 0.6em;
		height: 0.25em;
		background: inherit;
		clip-path: polygon(0% 0%, 100% 0%, 50% 100%);
	}
</style>
