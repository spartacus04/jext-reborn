<script lang="ts">
	import type { SvelteComponent } from 'svelte';

	interface Tab {
		name: string;
		component: typeof SvelteComponent<any>;
	}

	export let items : Tab[] = [];
	export let data: any = {};
	let activeTab = items[0].name;

	const setActiveTab = (tabName: string) => {
		activeTab = tabName;
	};
</script>

<div class="tabscontainer">
	{#each items as item}
		<div class="tabheader {activeTab == item.name ? 'active' : ''}" on:click={() => setActiveTab(item.name)}>{item.name}</div>
	{/each}
</div>
<div class="contentcontainer">
	{#each items as item}
		{#if activeTab == item.name}
			<svelte:component this={item.component} bind:data />
		{/if}
	{/each}
</div>


<style lang="scss">
	.tabscontainer {
		display: flex;
		flex-direction: row;

		background-color: #202020;
		border-bottom: 1px solid white;
		height: 3em;

		padding-left: 2em;
		overflow-x: auto;
		overflow-y: hidden;

		@media screen and (max-width: 425px) {
			padding-left: 0;
			justify-content: center;
		}

		.tabheader {
			display: flex;
			align-items: center;
			justify-content: center;

			background-color: #303030;
			border: 1px solid #282828;
			border-bottom: 0;

			border-radius: 0.5em 0.5em 0 0;

			user-select: none;

			padding: 0.5em;
			width: min-content;
			cursor: pointer;
			color: white;
			width: max-content;

			font-family: 'minecraft_regular';
			text-shadow: 1px 1px 3px black;

			&.active {
				background-color: #484848;
				border: 1px solid white;
				border-bottom: 0;
			}
		}
	}

	.contentcontainer {
		height: calc(100% - 3em - 1px);
		display: flex;
		flex-direction: column;
	}
</style>