<script lang="ts">
	export let value: unknown = 0;
	export let placeholder: unknown;
	export let fontsize: string = '1em';

	export const plc = (placeholder as number).toString() ?? '';

	$: (() => {
		if (value == '' || value == null) {
			return (value = 0);
		}

		if (+value < 0) {
			value = 0;
		}

		if ((value as number).toString().includes('e') || (value as number).toString().includes('E')) {
			value = 0;
		}
	})();
</script>

<input
	class="w-full text-white p-2 h-min font-minecraft bg-[#303030] hover:bg-[#404040] border-[3px] border-black appearance-none"
	type="number"
	bind:value
	placeholder={plc}
	style:font-size={fontsize}
/>

<style>
	input {
		text-shadow: 1px 1px 5px black;
		height: calc(24px + 1.6em);
		padding-top: calc(0.8em - 8px);
		padding-bottom: calc(0.8em - 12px);
	}

	input::-webkit-outer-spin-button,
	input::-webkit-inner-spin-button {
		-webkit-appearance: none;
		margin: 0;
	}
</style>
