<script lang="ts">
    export let input: HTMLInputElement|undefined = undefined;
    export let value: string;
    export let placeholder = '';
    export let classes = '';
</script>

<input
    on:input
    bind:this={input}
    bind:value
    type="text"
    class="w-full px-2 pt-0.5 pb-1 text-white bg-[#0e0e0e] border-2 border-[#0dd166] rounded-sm focus:border-white focus:outline-none text-lg {classes}"
    {placeholder}
/>