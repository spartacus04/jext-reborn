<script lang="ts">
	export let text: string;
	export let type: 'secondary' | 'primary' = 'secondary';
</script>

{#if type === 'secondary'}
	<button
		on:click
		class="border select-none border-surface-separator py-1.5 px-5 text-sm bg-surface-background hover:bg-[#4b4b4b] transition-colors duration-100 ease-in-out"
		>{text}</button
	>
{:else}
	<button
		on:click
		class="border select-none border-[#008542] py-1.5 px-5 text-sm bg-[#008542] hover:bg-[#0dd166] transition-colors duration-100 ease-in-out"
		>{text}</button
	>
{/if}
