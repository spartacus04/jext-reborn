<script lang="ts">
	import type { ConfigNode } from "$lib/types";
	import MinecraftCheckbox from "./MinecraftCheckbox.svelte";
	import MinecraftButton from "./MinecraftButton.svelte";
	import MinecraftTextbox from "./MinecraftTextbox.svelte";
    import MinecraftNumber from "./MinecraftNumber.svelte";
	import MinecraftComboBox from "./MinecraftComboBox.svelte";
	import { getModalStore, type ModalComponent } from "@skeletonlabs/skeleton";
	import DungeonSelectModal from "./DungeonSelectModal.svelte";

    export let node: ConfigNode<boolean|number|string|{[key : string] : number}>;

	const modalStore = getModalStore();

	const type = (() => {
		if(typeof node.defaultValue === 'boolean') return 'boolean';
		if(typeof node.defaultValue === 'number') return 'number';

		if(typeof node.defaultValue === 'string') {
			if(Array.isArray(node.enumValues)) return 'enum';
			return 'string';
		}

		if(typeof node.enumValues === 'string') return 'enumSource';
	})();

	if(type === undefined) throw new Error('Invalid type');

	const open = () => {
		const modalComponent: ModalComponent = { ref: DungeonSelectModal, props: { source: node.enumValues as string, defaultValue: node.id == 'disc-loottables-limit' ? 2 : 3, value: node.value }};

		modalStore.trigger({
			type: 'component',
			component: modalComponent,
			title: node.name,
			response(r) {
				if(!r) return;

				node.value = r;
			},
		})
	}

    $: can_reset = type === 'enumSource' ? JSON.stringify(node.value) !== JSON.stringify(node.defaultValue) : node.value !== node.defaultValue;
</script>

<div class="flex-col flex mx-3 justify-between sm:flex-row">
	<div class="flex flex-col" id="data">
		<h3 class="h3 font-minecraft">{node.name}</h3>
		<h6 class="h6 font-minecraft">{node.description}</h6>
	</div>

	<div id="controls" class="flex-col flex gap-2 w-full justify-end md:flex-row sm:w-72">
		{#if type == 'boolean'}
			<div class="flex-1 flex gap-2 w-full justify-end">
				<MinecraftCheckbox bind:value={node.value} />
				<MinecraftButton flex={true} enabled={can_reset} on:click={() => node.value = node.defaultValue}>Reset</MinecraftButton>
			</div>
		{:else}
			{#if type == 'number'}
				<MinecraftNumber bind:value={node.value} placeholder={node.defaultValue} />
			{:else if type == 'string'}
				<MinecraftTextbox bind:value={node.value} placeholder={node.defaultValue} />
			{:else if type == 'enum'}
				<MinecraftComboBox bind:value={node.value} values={node.enumValues} />
			{:else if type == 'enumSource'}
				<MinecraftButton on:click={open} flex={true}>Open editor</MinecraftButton>
			{/if}
			<MinecraftButton enabled={can_reset} on:click={() => node.value = node.defaultValue}>Reset</MinecraftButton>
		{/if}
	</div>
</div>

<style>
	#data {
		@media screen and (max-width: 640px) {
			width: 100%;
		}

		width: calc(100% - 300px);
		text-shadow: 1px 1px 5px black;
	}
</style>