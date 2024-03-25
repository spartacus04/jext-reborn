<script lang="ts">
	export let max: number;
	export let value: number;
	export let invisible: boolean = false;
</script>

{#if !invisible}
	<div
		class="w-full h-8 bg-gray-200 relative border-mc-light-gray border {invisible
			? 'border-transparent'
			: ''}"
	>
		<div class="h-full bg-[#cb3d35]" style="width: {(value / max) * 100}%"></div>
		<div
			class="absolute top-0 left-0 w-full h-full flex items-center justify-center text-black font-minecraft text-2xl"
		>
			{value}/{max}
		</div>
	</div>
{:else}
	<div class="h-8" />
{/if}
