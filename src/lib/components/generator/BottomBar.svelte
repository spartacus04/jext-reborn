<script lang="ts">
	import { discsStore } from '$lib/discs/discManager';
	import { get } from 'svelte/store';
	import { MinecraftLaunchButton } from '../buttons';
	import OutputModal from '../modals/OutputModal.svelte';
	import { baseElement } from '$lib/state';

	const generate = () => {
		const outputModal = new OutputModal({
            target: get(baseElement)!,
            props: {
                onFinish: () => {
                    outputModal.$destroy();
                }
            }
        });

        outputModal.openModal();
	};
</script>

<div class="w-full h-[4.5rem] bg-surface-background flex items-center justify-center">
	<div class="relative top-[-20%]">
		{#if $discsStore.length > 0}
			<MinecraftLaunchButton on:click={generate}>Generate</MinecraftLaunchButton>
		{:else}
			<div class="grayscale cursor-not-allowed [&>*]:pointer-events-none">
				<MinecraftLaunchButton>Generate</MinecraftLaunchButton>
			</div>
		{/if}
	</div>
</div>
