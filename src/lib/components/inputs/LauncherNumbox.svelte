<script lang="ts">
	export let input: HTMLInputElement | undefined = undefined;
	export let value: number;
	export let placeholder = '';
	export let classes = '';
	export let min: number = 1;
	export let max: number = 15;
</script>

<input
	on:input
	bind:this={input}
	bind:value
	type="number"
	class="w-full px-2 pt-0.5 pb-1 text-white bg-[#0e0e0e] border-2 border-[#0dd166] rounded-sm focus:border-white focus:outline-none text-lg {classes}"
	{placeholder}
	{min}
	{max}
/>

<style>
	input[type='number'] {
		-moz-appearance: textfield;
		appearance: textfield;
	}

	input[type='number']::-webkit-inner-spin-button,
	input[type='number']::-webkit-outer-spin-button {
		-webkit-appearance: none;
		margin: 0;
	}
</style>
