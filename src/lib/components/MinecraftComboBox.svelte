<script lang="ts" generics="T">
	export let value: T;
    export let values: T[] = [];
	export let labels: string[] = [];
	export let fontsize: string = '1em';
</script>

<select class="w-full text-white p-2 h-min font-minecraft bg-[#303030] hover:bg-[#404040] border-[3px] border-black appearance-none hover:border-white" bind:value={value} style:font-size={fontsize} on:input>
    {#each values as option, i}
        <option value={option}>{labels.length > i ? labels[i] : option}</option>
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