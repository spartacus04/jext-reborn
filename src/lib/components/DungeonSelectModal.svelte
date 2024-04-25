<!-- This is extremely messy, but I'm too lazy to rewrite this -->
<script lang="ts">
	import { TabGroup, Tab, ProgressRadial, getModalStore } from '@skeletonlabs/skeleton';
	import { onDestroy } from 'svelte';

	import { loottables } from '../';

	import { base } from '$app/paths';

	const modalStore = getModalStore();

	export let source: string = 'chests/*';
	export let defaultValue = 2;
	export let isChance: boolean = false;
	export let isBoolean: boolean = false;

	export let value: {
		[key: string]: number;
	} = {};

	const items = loottables[source];

	let tabSet: number = 0;

	for (const [_, val] of Object.entries(items)) {
		val.contents.forEach((item) => {
			if (!value[item]) {
				value[item] = defaultValue;
			}
		});
	}

	let cleanUp: string;
	let flag = false;

	onDestroy(() => {
		URL.revokeObjectURL(cleanUp);
	});

	const update = (e: Event) => {
		const target = e.target as HTMLInputElement;

		if (target.value === '') {
			target.value = '0';
		}

		if (+target.value < 0) {
			target.value = '0';
		}

		if (+target.value > 1000) {
			target.value = '1000';
		}

		const type = target.getAttribute('data-type');

		if (type === 'group') {
			const group = target.getAttribute('data-target') ?? '';

			const elements = items[group].contents;

			elements.forEach((item) => {
				value[item] = parseInt(target.value);
			});
		} else {
			const item = target.getAttribute('data-target') ?? '';

			value[item] = parseInt(target.value);
		}

		flag = !flag;
	};

	const updateBoolean = (e: Event) => {
		const target = e.target as HTMLInputElement;

		const type = target.getAttribute('data-type');
		const itemOrGroup = target.getAttribute('data-target') ?? '';

		if (type === 'group') {
			const elements = items[itemOrGroup].contents;

			if (elements.every((item) => value[item] === value[elements[0]])) {
				elements.forEach((item) => {
					value[item] = value[item] == 0 ? 1 : 0;
				});
			} else {
				elements.forEach((item) => {
					value[item] = value[elements[0]];
				});
			}
		} else {
			value[itemOrGroup] = value[itemOrGroup] == 0 ? 1 : 0;
		}
	};

	const getImage = async (item: string) => {
		const response = fetch(`${base}/${item}`);
		const blob = await response.then((res) => res.blob());

		cleanUp = URL.createObjectURL(blob);

		return cleanUp;
	};

	const getGroupValue = (group: string): string => {
		const elements = items[group].contents;

		return elements.every((item) => value[item] === value[elements[0]])
			? value[elements[0]].toString()
			: '';
	};

	const exit = () => {
		if ($modalStore[0]) {
			// clone value and remove each element with default value
			const parsedValue = { ...value };

			for (const [key, val] of Object.entries(parsedValue)) {
				if (val === defaultValue) {
					delete parsedValue[key];
				}
			}

			$modalStore[0]!.response!(parsedValue);
			modalStore.close();
		}
	};
</script>

<main class="bg-surface-500 rounded-md p-2 h-[50%] overflow-y-auto w-[90%] sm:w-[80%] lg:w-[50%]">
	<TabGroup>
		<Tab bind:group={tabSet} name="tab1" value={0}>Simple</Tab>
		<Tab bind:group={tabSet} name="tab2" value={1}>Advanced</Tab>
		<!-- Tab Panels --->
		<svelte:fragment slot="panel">
			{#if tabSet === 0}
				<div class="grid sm:grid-cols-4 p-2 gap-2">
					{#each Object.entries(items) as [key, val]}
						{#if isBoolean}
							<button
								class="card rounded-lg flex flex-col justify-between [&>*]:pointer-events-none outline-none {value &&
								+getGroupValue(key) > 0
									? 'variant-filled-tertiary'
									: ''}"
								data-type="group"
								data-target={key}
								on:click={updateBoolean}
							>
								<header class="card-header flex items-center justify-center w-full aspect-[9/10]">
									{#await getImage(val.img)}
										<ProgressRadial />
									{:then url}
										<img src={url} alt="dungeon" class="w-full" />
									{/await}
								</header>
								<section class="p-4 w-full">
									<h3 class="h4 text-center w-full">{key}</h3>
								</section>
							</button>
						{:else}
							<div class="card rounded-lg flex flex-col justify-between">
								<header class="card-header flex items-center justify-center w-full aspect-[9/10]">
									{#await getImage(val.img)}
										<ProgressRadial />
									{:then url}
										<img src={url} alt="dungeon" class="w-full" />
									{/await}
								</header>
								<section class="p-4 w-full">
									<h3 class="h4 text-center w-full">{key}</h3>
								</section>
								<footer class="card-footer">
									{#if isChance}
										<div class="input appearance-none flex items-center">
											<input
												type="number"
												class="input text-left appearance-none outline-none"
												min="-1"
												max="1000"
												data-type="group"
												data-target={key}
												on:input={update}
												placeholder="-"
												value={+getGroupValue(key)}
											/>
											{#key flag}
												<span class="text-sm mx-2">
													{+getGroupValue(key) / 10}%
												</span>
											{/key}
										</div>
									{:else}
										<input
											type="number"
											class="input text-center appearance-none"
											min="-1"
											max="12"
											data-type="group"
											data-target={key}
											on:input={update}
											placeholder="-"
											value={getGroupValue(key)}
										/>
									{/if}
								</footer>
							</div>
						{/if}
					{/each}
				</div>
			{:else if tabSet === 1}
				<div class=" h-full grid grid-cols-1 sm:grid-cols-2">
					<h3 class="h3 hidden sm:flex text-center w-full">Loot table</h3>
					<h3 class="h3 hidden sm:flex w-full">
						{isChance ? 'Disc chance to be found' : 'Max. number of discs'}
					</h3>
					<div class="hidden sm:flex mb-4" />
					<div />

					{#each Object.entries(value) as [key, val]}
						<h5 class="h5">{key}</h5>
						{#if isBoolean}
							<div class="flex items-center">
								<input
									type="checkbox"
									class="checkbox aspect-square"
									data-type="item"
									data-target={key}
									on:change={updateBoolean}
									checked={val === 1}
								/>
								<span class="text-sm mx-2">Enabled</span>
							</div>
						{:else if isChance}
							<div class="input appearance-none flex items-center">
								<input
									type="number"
									class="input"
									min="0"
									max="1000"
									value={val}
									data-type="item"
									data-target={key}
									on:input={update}
									placeholder="0"
								/>
								{#key flag}
									<span class="text-sm mx-2">
										{val / 10}%
									</span>
								{/key}
							</div>
						{:else}
							<input
								type="number"
								class="input"
								min="0"
								max="12"
								value={val}
								data-type="item"
								data-target={key}
								on:input={update}
								placeholder="2"
							/>
						{/if}
					{/each}
				</div>
			{/if}

			<div class="flex w-full items-center justify-center">
				<button class="btn variant-filled-secondary text-white" on:click={exit}>Save</button>
			</div>
		</svelte:fragment>
	</TabGroup>
</main>

<style>
	input::-webkit-outer-spin-button,
	input::-webkit-inner-spin-button {
		-webkit-appearance: none;
		margin: 0;
	}
</style>
