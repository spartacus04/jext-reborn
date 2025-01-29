<script lang="ts">
	export let text: string;
	export let type: 'tertiary' | 'secondary' | 'primary' | 'danger' = 'secondary';

	export let classes = '';
</script>

{#if type === 'secondary'}
	<button
		on:click
		class="{classes} border select-none border-surface-separator py-1.5 px-5 text-sm bg-surface-background hover:bg-[#4b4b4b] transition-colors duration-100 ease-in-out"
		>{text}</button
	>
{:else if type === 'primary'}
	<button
		on:click
		class="{classes} border select-none border-[#008542] py-1.5 px-5 text-sm bg-[#008542] hover:bg-[#0dd166] transition-colors duration-100 ease-in-out"
		>{text}</button
	>
{:else if type === 'danger'}
	<button
		on:click
		class="{classes} border select-none border-[#ff0000] py-1.5 px-5 text-sm bg-[#ff0000] hover:bg-[#ff1d1d] transition-colors duration-100 ease-in-out"
		>{text}</button
	>
{:else if type === 'tertiary'}
	<button
		on:click
		class="{classes} border select-none border-surface-separator py-1.5 px-5 text-sm bg-[#3f3f3f] hover:bg-[#4b4b4b] transition-colors duration-100 ease-in-out"
		>{text}</button
	>
{/if}
