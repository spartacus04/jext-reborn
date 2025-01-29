<script lang="ts">
	import { generate_btn } from '$lib/assets';
</script>

<button
	class="border-[10px] border-transparent px-8 py-1 select-none font-minecraft-launcher text-3xl text-white hover:brightness-110"
	style:border-image-source="url({generate_btn})"
	on:click
>
	<slot />
</button>

<style>
	button {
		border-image-slice: 2 fill;
		text-shadow: 0 0 10px black;
	}
</style>
