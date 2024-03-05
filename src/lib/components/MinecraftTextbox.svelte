<script lang="ts">
	export let value: unknown = '';
	export let placeholder: unknown = '';
	export let fontsize: string = '1em';

	const plc = (placeholder as string) ?? '';
</script>

<input class="w-full text-white p-2 h-min font-minecraft bg-[#303030] hover:bg-[#404040] border-[3px] border-black appearance-none" type="text" bind:value={value} placeholder={plc} style:font-size={fontsize}>

<style>
	input {
		text-shadow: 1px 1px 5px black;
		height: calc(24px + 1.6em);
        padding-top: calc(0.8em - 8px);
		padding-bottom: calc(0.8em - 12px);
	}
</style>