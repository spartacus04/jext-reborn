<script lang="ts">
	export let value: string = '';
	export let placeholder: string = '';
	export let fontsize: string = '1em';
</script>

<input type="text" bind:value={value} placeholder={placeholder} style:font-size={fontsize}>

<style lang="scss">
	input {
		width: 100%;
		margin: 0.5em 0 0 0;
		border-radius: 0;
		border: 1px solid #808080;
		color: white;
		padding: 0.5em;
		height: min-content;
		font-family: 'minecraft_regular';
		text-shadow: 1px 1px 5px black;
		background-color: #303030;
		margin: 0;

		&:hover {
			background-color: #404040;
		}
	}
</style>