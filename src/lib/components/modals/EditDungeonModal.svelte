<script lang="ts">
	import { groups, lootTables, type Loottable } from '$lib/constants';
	import { cConfirm } from '$lib/utils';
	import LauncherButton from '../buttons/LauncherButton.svelte';

	let dialog: HTMLDialogElement;

	export const open = () => dialog.show();
	export const close = () => dialog.close();
	export const isOpen = () => dialog.open;
	export const toggle = () => (dialog.open ? close() : open());
	export const openModal = () => dialog.showModal();

	const onClick = async (e: MouseEvent) => {
		const id = (e.target as HTMLElement).id;

		if (id === 'backdrop') {
			const response = await cConfirm({
				text: 'Do you want to save your changes?',
				cancelText: 'Cancel',
				confirmText: 'Save',
				discardText: 'Discard'
			});

			switch (response) {
				case 'cancel':
					return;
				case 'discard':
					onFinish(null);
					close();
					return;
				case 'confirm':
					saveChanges();
					close();
					break;
			}
		} else e.stopPropagation();
	};

	function saveChanges(): void {
		Object.keys(dungeons).forEach((key) => dungeons[key] === 0 && delete dungeons[key]);

		onFinish(dungeons);
	}

	export let dungeons: { [key: string]: number };

	lootTables.forEach((lootTable) => {
		lootTable.contents.forEach((name) => {
			if (!dungeons[name]) {
				dungeons[name] = 0;
			}
		});
	});

	let advancedMenuOpen = false;
	let flag = true;

	const update = (e: Event) => {
		const target = e.target as HTMLInputElement;

		const type = target.getAttribute('data-type');
		const group = target.getAttribute('data-target') ?? '';

		if (target.value === '') {
			target.value = '0';
		}

		if (+target.value < 0) {
			target.value = '0';
		}

		if (type === 'group') {
			if (
				lootTables.filter((item) => group === `${item.displayName}-${item.subtitle}`)[0].mode ==
					'percentage' &&
				+target.value > 1000
			) {
				target.value = '1000';
			}

			const elements = lootTables.filter(
				(item) => group === `${item.displayName}-${item.subtitle}`
			)[0].contents;

			elements.forEach((item) => {
				dungeons[item] = parseInt(target.value);
			});
		} else {
			if (
				lootTables.filter((item) => item.contents.includes(group))[0].mode == 'percentage' &&
				+target.value > 1000
			) {
				target.value = '1000';
			}
		}

		flag = !flag;
	};

	const getGroupValue = (group: Loottable): string => {
		const elements = lootTables.filter(
			(item) => `${item.displayName}-${item.subtitle}` === `${group.displayName}-${group.subtitle}`
		)[0].contents;

		return elements.every((item) => dungeons[item] === dungeons[elements[0]])
			? dungeons[elements[0]].toString()
			: '----';
	};

	const getPercentage = (value: number, group: Loottable) => {
		let result;

		if (group.mode == 'percentage') {
			result = value / 10;
		} else {
			result =
				Math.round(
					(value /
						((group.defaultLoottableWeight! + value) * (1 - (group.ignorePercentage ?? 0)))) *
						1000
				) / 10;
		}

		if (isNaN(result)) {
			return '?%';
		} else {
			return `${result}%`;
		}
	};

	const keyToGroup = (key: string) => {
		const item = lootTables.find((item) => item.contents.includes(key));
		return item!;
	};

	export let onFinish: (changes: { [key: string]: number } | null) => unknown;
</script>

<!-- svelte-ignore a11y-no-noninteractive-element-interactions a11y-click-events-have-key-events -->
<dialog
	bind:this={dialog}
	id="backdrop"
	on:click={onClick}
	class="bg-surface-background md:w-[51rem] md:bg-transparent p-0 backdrop:opacity-55 backdrop:bg-surface-background"
>
	<div
		class="m-0 md:m-auto md:w-[50rem] h-fit rounded-md bg-surface-background animate-slide-down block p-4 font-minecraft text-white shadow-md"
	>
		<!-- This is a stupid ass hack -->
		<p
			class="md:hidden overflow-hidden whitespace-nowrap text-ellipsis w-[calc(100%)] h-0 select-none pointer-events-none"
		>
			Looking at the source code? Welp, can't do anything about it since this is open source
		</p>

		<div>
			{#if !advancedMenuOpen}
				<div class="grid sm:grid-cols-4 xs:grid-cols-2 p-2 gap-2">
					{#each lootTables as lootTable}
						<div class="flex flex-col p-4 bg-surface-background-variant">
							<div class="flex items-center justify-center w-full aspect-[9/10]">
								<img src={lootTable.img} alt="dungeon" class="w-full" />
								<img src={groups[lootTable.group]} alt="group" class="self-end -ml-12 w-12 h-12" />
							</div>

							<div class="flex flex-col p-4 w-full">
								<h4 class="text-xl text-center w-full font-bold">{lootTable.displayName}</h4>
								{#if lootTable.subtitle}
									<h5 class="text-md text-center w-full">
										{lootTable.subtitle}
									</h5>
								{/if}
							</div>

							<div class="flex items-end flex-1">
								<div
									class="flex items-baseline w-full px-2 pt-0.5 pb-1 text-white bg-[#0e0e0e] border-2 border-[#0dd166] rounded-sm focus-within:border-white focus-within:outline-none text-lg"
								>
									<input
										type="number"
										class="bg-transparent text-left appearance-none outline-none w-full"
										min="-1"
										data-type="group"
										data-target="{lootTable.displayName}-{lootTable.subtitle}"
										on:input={update}
										placeholder="-"
										value={+getGroupValue(lootTable)}
									/>
									{#key flag}
										<span
											class="text-sm border-l-2 border-l-surface-separator pl-2 w-20 text-right"
										>
											{getPercentage(+getGroupValue(lootTable), lootTable)}
										</span>
									{/key}
								</div>
							</div>
						</div>
					{/each}
				</div>
			{:else}
				{#each Object.entries(dungeons) as [key, _]}
					<div
						class="flex items-baseline w-full px-2 py-1 text-white bg-[#0e0e0e] border-2 border-[#0dd166] rounded-sm focus-within:border-white focus-within:outline-none text-lg"
					>
						<!-- group icon -->
						<img
							src={keyToGroup(key).img}
							alt="dungeon"
							class="w-8 h-8 aspect-square self-center"
						/>
						<!-- group icon -->
						<img
							src={groups[keyToGroup(key).group]}
							alt="group"
							class="self-center aspect-square w-8 h-8 mr-2"
						/>

						<span class="w-full border-l-2 border-l-surface-separator pl-2 flex-grow">{key}</span>
						<input
							type="number"
							class="bg-transparent text-left outline-none border-l-2 border-l-surface-separator pl-2 w-44"
							min="-1"
							data-type="item"
							data-target={key}
							on:input={update}
							placeholder="0"
							bind:value={dungeons[key]}
						/>
						{#key flag}
							<span class="text-sm border-l-2 border-l-surface-separator text-right pl-2 w-20">
								{getPercentage(dungeons[key], keyToGroup(key))}
							</span>
						{/key}
					</div>
				{/each}
			{/if}
		</div>

		<div class="border-b-2 border-surface-separator my-2" />

		<div class="flex gap-2 pt-4 justify-end text-white font-minecraft font-bold">
			<LauncherButton
				text={advancedMenuOpen ? 'Basic' : 'Advanced'}
				type="tertiary"
				on:click={() => (advancedMenuOpen = !advancedMenuOpen)}
			/>
			<LauncherButton
				text="Discard"
				type="danger"
				on:click={() => {
					onFinish({});
					close();
				}}
			/>
			<LauncherButton
				text="Save"
				type="primary"
				on:click={() => {
					saveChanges();
					close();
				}}
			/>
		</div>
	</div>
</dialog>

<style lang="postcss">
	dialog {
		transition:
			display 0.1s allow-discrete,
			overlay 0.1s allow-discrete;

		animation: open 0.1s forwards;
	}

	dialog:not([open]) {
		animation: close 0.1s forwards;
	}

	@keyframes open {
		from {
			opacity: 0;
			transform: translateY(-2%);
		}
		to {
			opacity: 1;
			transform: translateY(0);
		}
	}

	@keyframes close {
		from {
			opacity: 1;
			transform: translateY(0);
		}
		to {
			opacity: 0;
			transform: translateY(-2%);
		}
	}

	input[type='number'] {
		-moz-appearance: textfield;
		appearance: textfield;
	}

	input[type='number']::-webkit-inner-spin-button,
	input[type='number']::-webkit-outer-spin-button {
		-webkit-appearance: none;
		margin: 0;
	}
</style>
