<script lang="ts">
    export let input: HTMLSelectElement|undefined = undefined;
    export let value: string;
    export let options: string[];
    export let classes = '';
</script>

<select
    on:input
    bind:this={input}
    bind:value
    class="w-full px-2 pt-0.5 pb-1 text-white bg-[#0e0e0e] border-2 border-[#0dd166] rounded-sm focus:border-white focus:outline-none text-lg {classes}"
>
    {#each options as option}
        <option class="bg-surface-background hover:bg-[#0dd166]" value={option}>{option}</option>
    {/each}
</select>