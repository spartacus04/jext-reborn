<script lang="ts">
	import type { BaseDisc } from '$lib/discs/baseDisc';
	import LauncherButton from '../buttons/LauncherButton.svelte';

	import { editDiscs, removeDisc, selectedDiscs } from '$lib/discs/discManager';

	export let disc: BaseDisc;

	const toggleSelected = () => {
		selectedDiscs.update((discs) => {
			if (discs.includes(disc)) {
				return discs.filter((d) => d != disc);
			} else {
				return [...discs, disc];
			}
		});
	};

	let update = {};
</script>

<div
	class="flex items-center justify-between p-4 bg-surface-separator flex-col lg:flex-row text-white font-minecraft"
>
	<input
		type="checkbox"
		class="-mb-[14px] lg:mb-0 lg:self-center self-end lg:mr-4 z-10"
		checked={$selectedDiscs.includes(disc)}
		on:click={toggleSelected}
	/>

	{#key update}
		<div class="flex flex-col lg:flex-row items-center w-full">
			<img src={disc.discTextureURL} alt={disc.title} class="w-28 lg:w-20 lg:h-20 rounded-lg" />
			<div class="lg:ml-4 block w-full">
				<h3
					class="h3 text-white font-bold font-minecraft text-lg lg:text-2xl overflow-hidden whitespace-nowrap text-ellipsis w-[calc(100%)]"
				>
					{disc.author.trim() != '' ? `${disc.title} - ${disc.author}` : disc.title}
				</h3>
				<p
					class="text-gray-400 font-minecraft overflow-hidden whitespace-nowrap text-ellipsis w-[calc(100%)]"
				>
					{disc.namespace}
				</p>
			</div>
		</div>
	{/key}
	<div class="lg:ml-4 flex gap-2 lg:flex-col">
		<LauncherButton
			text="Edit"
			type="primary"
			on:click={async () => {
				await editDiscs(disc);
				update = {};
			}}
		/>
		<LauncherButton text="Delete" type="danger" on:click={() => removeDisc(disc)} />
	</div>
</div>
