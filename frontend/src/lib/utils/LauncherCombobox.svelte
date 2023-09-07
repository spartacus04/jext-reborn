<script lang="ts">
	export let selected : string;
	export let options : Map<string, string>;
</script>

<select bind:value={selected}>
	{#each [...options] as [key, value]}
		<option value={key}>{value}</option>
	{/each}
</select>

<style lang="scss">
	select, option {
		border-radius: 0;
		color: white;
		font-size: 1.1em;
		padding: 0.5em;
		width: fit-content;
		background-color: #303030;
		font-family: 'minecraft_regular';
		text-shadow: 1px 1px 3px black;

		&:hover {
			background-color: #404040;
		}
	}
</style>