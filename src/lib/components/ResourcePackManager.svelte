<script lang="ts">
	import { resourcePackStore, versions } from '$lib/config';
	import { inputFile, dropFile } from '$lib/directives';
	import JSZip from 'jszip';
	import MinecraftComboBox from './MinecraftComboBox.svelte';
	import MinecraftTextbox from './MinecraftTextbox.svelte';
	import { randomDiscTexture } from '$lib/resourcepack/discs';

	const setTexture = async (files?: File[]) => {
		if (files && files.length > 0 && files[0]) {
			$resourcePackStore.icon = files[0];
		}
	};

	const addRP = async (files?: File[]) => {
		if (files && files.length > 0 && files[0]) {
			$resourcePackStore.packs = [
				...$resourcePackStore.packs,
				{
					name: files[0].name,
					value: files[0],
					icon: await new JSZip().loadAsync(files[0]).then(async (zip) => {
						const icon = zip.file('pack.png');
						return icon ? icon.async('blob') : (await randomDiscTexture())!;
					})
				}
			];
		}
	};

	const shiftRp = (index: number, shift: number) => {
		const packs = $resourcePackStore.packs;

		const temp = packs[index];
		packs[index] = packs[index + shift];
		packs[index + shift] = temp;

		$resourcePackStore.packs = packs;
	};
</script>

<main class="flex flex-col p-5">
	<div class="flex gap-4 flex-col sm:flex-row items-center">
		<img
			class="bg-[#202020] p-3 cursor-pointer w-28 sm:w-32 max-w-32 h-28 sm:h-32 max-h-32 aspect-square border border-transparent hover:border-white"
			src={URL.createObjectURL($resourcePackStore.icon)}
			alt="disc icon"
			use:inputFile={{
				accept: 'image/*',
				cb: setTexture
			}}
			use:dropFile={{
				accept: 'image/*',
				cb: setTexture
			}}
		/>

		<div class="flex h-28 sm:h-32 h-max-32 justify-around flex-col w-full">
			<MinecraftComboBox
				bind:value={$resourcePackStore.version}
				values={Array.from(versions.keys())}
				labels={Array.from(versions.values())}
			/>

			<MinecraftTextbox
				placeholder="The resource pack description"
				bind:value={$resourcePackStore.description}
			/>
		</div>
	</div>

	<h4 class="h4 text-center font-minecraft">Merge with other resourcepacks</h4>

	<div
		class="grid grid-cols-1 sm:grid-cols-3 lg:grid-cols-5 xl:grid-cols-7 mt-4 p-2 gap-4 border-white rounded-md border"
	>
		<div class="card p-4 rounded-lg">
			<div class="card-header">
				<img
					src={URL.createObjectURL($resourcePackStore.icon)}
					alt="icon"
					class="w-full aspect-square"
				/>
			</div>
			<section class="p-4 font-minecraft text-center w-[calc(100%)]">JEXT Resources</section>
			<footer class="card-footer flex items-center justify-center">
				<button class="cursor-not-allowed">
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
						class="lucide lucide-lock"
						><rect width="18" height="11" x="3" y="11" rx="2" ry="2" /><path
							d="M7 11V7a5 5 0 0 1 10 0v4"
						/></svg
					>
				</button>
			</footer>
		</div>
		{#each $resourcePackStore.packs as pack, i}
			<div class="card p-4 rounded-lg">
				<div class="card-header">
					<img src={URL.createObjectURL(pack.icon)} alt="icon" class="w-full aspect-square" />
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
					<button
						class="card-hover"
						disabled={i == $resourcePackStore.packs.length - 1}
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
							$resourcePackStore.packs.length - 1
								? 'stroke-gray-600 cursor-not-allowed'
								: ''}"><path d="m9 18 6-6-6-6" /></svg
						>
					</button>
				</footer>
			</div>
		{/each}
		<button
			class="card p-4 rounded-lg card-hover cursor-pointer [&>*]:pointer-events-none"
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
				class="w-full aspect-square text-[d3d3d3]"
				viewBox="0 0 24 24"
				fill="none"
				stroke="currentColor"
				stroke-width="2"
				stroke-linecap="round"
				stroke-linejoin="round"><path d="M5 12h14" /><path d="M12 5v14" /></svg
			>
		</button>
	</div>
</main>
