<script lang="ts">
	import { delete_btn, signalum_security_lock } from '$lib/assets';
	import { inputFile, dropFile } from '$lib/directives';
	import { addRP, ResourcePackData } from '$lib/discs/resourcePackManager';
	import { onDestroy } from 'svelte';

	const shiftRp = (index: number, shift: number) => {
		const packs = $ResourcePackData.packs;

		const temp = packs[index];
		packs[index] = packs[index + shift];
		packs[index + shift] = temp;

		$ResourcePackData.packs = packs;
	};

	const removeRp = (index: number) => {
		const packs = $ResourcePackData.packs;
		packs.splice(index, 1);
		$ResourcePackData.packs = packs;
	};
</script>

<h4 class="text-2xl text-center font-minecraft text-white">Merge with other resourcepacks</h4>

<div
	class="grid grid-cols-1 sm:grid-cols-3 lg:grid-cols-5 xl:grid-cols-7 mt-4 p-4 gap-4 bg-surface-background text-white"
>
	<div class="card p-4 bg-surface-background-variant">
		<div class="card-header flex items-center justify-center">
			<img
				src={URL.createObjectURL($ResourcePackData.icon)}
				alt="icon"
				class="w-full max-h-40 max-w-40 aspect-square"
			/>
		</div>
		<section class="p-4 font-minecraft text-center w-[calc(100%)]">JEXT Resources</section>
		<footer class="card-footer flex items-center justify-center">
			<button class="cursor-not-allowed">
				<img src={signalum_security_lock} class="w-8 h-8" alt="locked" />
			</button>
		</footer>
	</div>
	{#each $ResourcePackData.packs as pack, i}
		<div class="card p-4 bg-surface-background-variant">
			<div class="card-header flex items-center justify-center">
				<img
					src={URL.createObjectURL(pack.icon)}
					alt="icon"
					class="w-full max-h-40 max-w-40 aspect-square"
				/>
			</div>
			<section
				class="p-4 font-minecraft text-center text-ellipsis w-[calc(100%)] whitespace-nowrap overflow-hidden"
			>
				{pack.name}
			</section>
			<footer class="card-footer flex items-center justify-center gap-4">
				<button class="card-hover" disabled={i == 0} on:click={() => shiftRp(i, -1)}>
					<svg
						xmlns="http://www.w3.org/2000/svg"
						width="24"
						height="24"
						viewBox="0 0 24 24"
						fill="none"
						stroke="currentColor"
						stroke-width="2"
						stroke-linecap="round"
						stroke-linejoin="round"
						class="lucide lucide-chevron-left rotate-90 sm:rotate-0 {i == 0
							? 'stroke-gray-600 cursor-not-allowed'
							: ''}"><path d="m15 18-6-6 6-6" /></svg
					>
				</button>
				<button class="card-hover" on:click={() => removeRp(i)}>
					<!-- on hover change color to red-->
					<img src={delete_btn} alt="delete" id="delbtn" />
				</button>
				<button
					class="card-hover"
					disabled={i == $ResourcePackData.packs.length - 1}
					on:click={() => shiftRp(i, 1)}
				>
					<svg
						xmlns="http://www.w3.org/2000/svg"
						width="24"
						height="24"
						viewBox="0 0 24 24"
						fill="none"
						stroke="currentColor"
						stroke-width="2"
						stroke-linecap="round"
						stroke-linejoin="round"
						class="lucide lucide-chevron-right rotate-90 sm:rotate-0 {i ==
						$ResourcePackData.packs.length - 1
							? 'stroke-gray-600 cursor-not-allowed'
							: ''}"><path d="m9 18 6-6-6-6" /></svg
					>
				</button>
			</footer>
		</div>
	{/each}
	<button
		class="card p-4 text-[#aeaeae] bg-[#404040] hover:bg-[#505050] flex items-center justify-center card-hover cursor-pointer [&>*]:pointer-events-none"
		use:inputFile={{
			accept: '.zip',
			cb: addRP
		}}
		use:dropFile={{
			accept: '.zip',
			cb: addRP
		}}
	>
		<svg
			xmlns="http://www.w3.org/2000/svg"
			class="h-14 w-14"
			fill="none"
			viewBox="0 0 24 24"
			stroke="currentColor"
		>
			<path stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
		</svg>
	</button>
</div>

<p class="text-md text-justify font-minecraft text-orange-400">Warning: the resource pack merge feature doesn't always merge resource packs correctly. If that happens it won't be considered a bug.</p>


<style>
	.card button:hover:not(:disabled) {
		transform: scale(1.2);
	}
</style>
