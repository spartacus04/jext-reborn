<script lang="ts">
	export let value: unknown = '';
    export let values: unknown = [];
	export let fontsize: string = '1em';

    const vals = (values as string[]).map((v) => v.toString());
</script>

<select class="w-full text-white p-2 h-min font-minecraft bg-[#303030] hover:bg-[#404040] border-[3px] border-black appearance-none hover:border-white" bind:value={value} style:font-size={fontsize}>
    {#each vals as option}
        <option value={option}>{option}</option>
    {/each}
</select>

<style>
	select {
		text-shadow: 1px 1px 5px black;
		height: calc(24px + 1.6em);
        padding-top: calc(0.8em - 8px);
		padding-bottom: calc(0.8em - 12px);
	}
</style>