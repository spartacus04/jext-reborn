<script lang="ts">
	export let value: boolean;
	export let classes = '';

	export let small = false;
	export let disableMinecraftFont = false;

	export let label = '';
</script>

<label class="flex items-center gap-2 {classes}">
	<div id="container" class="flex appearance-none bg-surface-background aspect-square {small ? 'h-3' : 'h-5'}">
		<input
			type="checkbox"
			bind:checked={value}
			on:change
			class="accent-[#0dd166] {small ? 'h-3 w-3' : 'h-5 w-5'} text-[#0dd166] border-2 border-[#0dd166] rounded-sm focus:outline-none"
		/>
	</div>
	<span class="text-white {small ? 'text-sm' : 'text-lg'} {disableMinecraftFont ? '' : 'font-minecraft'}">{label}</span>
</label>

<style>
	#container > input[type='checkbox'] {
		appearance: none;
		background-color: #1e1e1e;
	}

	#container > input[type='checkbox']:checked {
		appearance: revert;
		accent-color: #0dd166;
	}
</style>
