<script lang="ts">
	import type { ConfigNode } from './config';
	import SecondaryButton from '../utils/SecondaryButton.svelte';
	import CheckboxButton from '../utils/CheckboxButton.svelte';
	import SecondaryCombobox from '../utils/SecondaryCombobox.svelte';

	export let configNode: ConfigNode<boolean|number|string|{[key : string] : boolean}>;

	$: can_reset = configNode.value !== configNode.defaultValue;
</script>

<div class="configOptionContainer">
	<div class="metadata">
		<div class="name">{configNode.name}</div>
		<div class="description">{configNode.description}</div>
	</div>
	<div class="setting">
		<div class="set">
			{#if typeof configNode.defaultValue == 'boolean' && typeof configNode.value == 'boolean'}
				<CheckboxButton bind:value={configNode.value} />
			{:else if typeof configNode.defaultValue == 'string' && typeof configNode.value == 'string'}
				{#if configNode.enumValues && configNode.enumValues.length > 0}
					<SecondaryCombobox bind:selected={configNode.value} options={configNode.enumValues} />
				{/if}
			{/if}
		</div>
		<SecondaryButton bind:enabled={can_reset} on:click={() => { configNode.value = configNode.defaultValue; }}>
			Reset
		</SecondaryButton>
	</div>
</div>

<style lang="scss">
	.configOptionContainer {
		display: flex;
		margin-top: 1em;
		width: 100%;

		@media (max-width: 769px) {
			flex-direction: column;
		}
	}

	.metadata {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: flex-start;

		* {
			font-family: 'minecraft_regular';
			text-shadow: 1px 1px 2px black;
			color: white;
		}

		.name {
			font-size: 1.5em;
		}

		.description {
			font-size: 0.9em;
			color: #aaa;
		}
	}

	.setting {
		display: flex;
		flex-direction: row;
		justify-content: flex-end;
		align-items: center;
		height: min-content;

		@media (max-width: 769px) {
			justify-content: space-between;
		}

		flex: 1;
		width: 100%;

		.set {
			min-width: 200px;
			display: flex;
			align-self: stretch;
			align-items: center;
			justify-content: center;
			margin: 0 1em 0 1em;
		}
	}
</style>