<script lang="ts">
	import { dropFile, inputFile } from '$lib/directives';

	// This is going to be extremely ugly, but I want to get this done ASAP
	import type { BaseDisc } from '$lib/discs/baseDisc';
	import { cConfirm } from '$lib/utils';
	import LauncherButton from '../buttons/LauncherButton.svelte';
	import LauncherCheckbox from '../inputs/LauncherCheckbox.svelte';
	import LauncherTextbox from '../inputs/LauncherTextbox.svelte';
	import WysiwygEditor from '../inputs/WYSIWYGEditor.svelte';
	import { editLootTables, isMusicDisc } from '$lib/discs/discManager';
	import LauncherCombobox from '../inputs/LauncherCombobox.svelte';
	import { onDestroy } from 'svelte';

	export let discs: BaseDisc[] = [];
	let dialog: HTMLDialogElement;

	export let changes: {
		[key: string]: {
			value: any;
			edited: boolean;
		};
	} = {};

	// Set up the changes object
	changes = {
		title: {
			value: discs.every((disc) => disc.title === discs[0].title) ? discs[0].title : '',
			edited: false
		},
		author: {
			value: discs.every((disc) => disc.author === discs[0].author) ? discs[0].author : '',
			edited: false
		},
		discTexture: {
			value: discs[0].discTexture,
			edited: false
		},
		fragmentTexture: {
			value: discs[0].fragmentTexture,
			edited: false
		},
		tooltip: {
			value: discs.every((disc) => disc.tooltip === discs[0].tooltip) ? discs[0].tooltip : '',
			edited: false
		},
		creeperDroppable: {
			value: discs.every((disc) => disc.creeperDroppable === discs[0].creeperDroppable)
				? discs[0].creeperDroppable
				: false,
			edited: false
		},
		discLootTables: {
			value: discs.every((disc) => disc.discLootTables === discs[0].discLootTables)
				? discs[0].discLootTables
				: {},
			edited: false
		},
		fragmentLootTables: {
			value: discs.every((disc) => disc.fragmentLootTables === discs[0].fragmentLootTables)
				? discs[0].fragmentLootTables
				: {},
			edited: false
		}
	};

	if (discs.some(isMusicDisc)) {
		const musicDiscs = discs.filter(isMusicDisc);

		changes['monoChannel'] = {
			value: musicDiscs.every((disc) => disc.monoChannel === musicDiscs[0].monoChannel)
				? musicDiscs[0].monoChannel
				: true,
			edited: false
		};

		changes['normalizeVolume'] = {
			value: musicDiscs.every((disc) => disc.normalizeVolume === musicDiscs[0].normalizeVolume)
				? musicDiscs[0].normalizeVolume
				: true,
			edited: false
		};

		changes['qualityPreset'] = {
			value: musicDiscs.every((disc) => disc.qualityPreset === musicDiscs[0].qualityPreset)
				? musicDiscs[0].qualityPreset
				: 'none',
			edited: false
		};
	}

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
					onFinish({});
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
		const parsedChanges = Object.keys(changes).filter((key) => changes[key].edited);

		let obj: { [key: string]: any } = {};

		for (const change of parsedChanges) {
			obj[change] = changes[change].value;
		}

		onFinish(obj);
	}

	function setUpdatedPropery(property: string): void {
		changes[property].edited = true;
	}

	function setTexture(file: File[] | undefined): void {
		if (file) {
			changes['discTexture'].value = file[0];
			changes['discTexture'].edited = true;

			URL.revokeObjectURL(textureUrl);
			textureUrl = URL.createObjectURL(file[0]);
		}
	}

	function setFragmentTexture(file: File[] | undefined): void {
		if (file) {
			changes['fragmentTexture'].value = file[0];
			changes['fragmentTexture'].edited = true;

			URL.revokeObjectURL(fragmentTextureUrl);
			fragmentTextureUrl = URL.createObjectURL(file[0]);
		}
	}

	async function editDiscLt() {
		const data = await editLootTables(changes.discLootTables.value);

		if (data) {
			changes['discLootTables'].value = data;
			changes['discLootTables'].edited = true;
		}
	}

	async function editFragmentLt() {
		const data = await editLootTables(changes.fragmentLootTables.value);

		if (data) {
			changes['fragmentLootTables'].value = data;
			changes['fragmentLootTables'].edited = true;
		}
	}

	let textureUrl = URL.createObjectURL(changes.discTexture.value);
	let fragmentTextureUrl = URL.createObjectURL(changes.fragmentTexture.value);

	onDestroy(() => {
		URL.revokeObjectURL(textureUrl);
		URL.revokeObjectURL(fragmentTextureUrl);
	});

	export let onFinish: (changes: { [key: string]: any }) => unknown;
</script>

<!-- svelte-ignore a11y-no-noninteractive-element-interactions a11y-click-events-have-key-events -->
<dialog
	bind:this={dialog}
	id="backdrop"
	on:click={onClick}
	class="bg-surface-background md:bg-transparent p-0 backdrop:opacity-55 backdrop:bg-surface-background"
>
	<div
		class="m-0 md:m-auto md:w-[50rem] h-fit rounded-md bg-surface-background animate-slide-down block p-4"
	>
		<!-- This is a stupid ass hack -->
		<p
			class="md:hidden overflow-hidden whitespace-nowrap text-ellipsis w-[calc(100%)] h-0 select-none pointer-events-none"
		>
			Looking at the source code? Welp, can't do anything about it since this is open source
		</p>

		<div class="flex flex-col justify-between gap-4 sm:flex-row w-full">
			<div class="flex gap-2 justify-around">
				<img
					class="bg-[#0a0a0a] p-3 cursor-pointer h-28 sm:h-32 h-max-32 aspect-square border border-transparent hover:border-white"
					src={textureUrl}
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
				<img
					class="bg-[#0a0a0a] p-3 cursor-pointer h-28 sm:h-32 aspect-square border border-transparent hover:border-white"
					src={fragmentTextureUrl}
					alt="disc icon"
					use:inputFile={{
						accept: 'image/*',
						cb: setFragmentTexture
					}}
					use:dropFile={{
						accept: 'image/*',
						cb: setFragmentTexture
					}}
				/>
			</div>
			<div class="flex flex-col flex-1 justify-around gap-2">
				<div class="flex flex-col">
					<b class="text-[#aeaeae] text-xs h-min">TITLE</b>
					<LauncherTextbox
						placeholder="Insert the disc title"
						bind:value={changes.title.value}
						on:input={() => setUpdatedPropery('title')}
					/>
				</div>
				<div class="flex flex-col">
					<b class="text-[#aeaeae] text-xs mb-0">AUTHOR</b>
					<LauncherTextbox
						placeholder="Insert the disc author"
						bind:value={changes.author.value}
						on:input={() => setUpdatedPropery('author')}
					/>
				</div>
			</div>
		</div>

		<div class="flex flex-col mt-3 w-full">
			<b class="text-[#aeaeae] text-xs h-min">TOOLTIP</b>

			<WysiwygEditor
				bind:text={changes.tooltip.value}
				firstline={changes.author.value != ''
					? `${changes.title.value} - ${changes.author.value}`
					: changes.title.value}
				on:input={() => setUpdatedPropery('tooltip')}
			/>
		</div>

		<div class="border-b-2 border-surface-separator my-2" />

		<div class="flex flex-col justify-between gap-4 sm:flex-row w-full">
			<div class="flex gap-4">
				<LauncherCheckbox
					bind:value={changes.creeperDroppable.value}
					on:change={() => setUpdatedPropery('creeperDroppable')}
					label="Can be dropped by creepers"
				/>
			</div>

			<div class="flex flex-1 flex-col gap-4 justify-between sm:flex-row w-full">
				<LauncherButton
					classes="font-minecraft font-bold text-white"
					text="Edit disc loot tables"
					type="primary"
					on:click={editDiscLt}
				/>
				<LauncherButton
					classes="font-minecraft font-bold text-white"
					text="Edit fragment loot tables"
					type="primary"
					on:click={editFragmentLt}
				/>
			</div>
		</div>

		{#if changes.monoChannel}
			<div class="border-b-2 border-surface-separator my-2" />

			<div class="flex flex-col gap-4 w-full">
				<div class="flex gap-4">
					<LauncherCheckbox
						bind:value={changes.monoChannel.value}
						on:change={() => setUpdatedPropery('monoChannel')}
						label="Mono channel"
					/>
					<LauncherCheckbox
						bind:value={changes.normalizeVolume.value}
						on:change={() => setUpdatedPropery('normalizeVolume')}
						label="Normalize volume"
					/>
				</div>
				<div class="flex flex-col w-full">
					<b class="text-[#aeaeae] text-xs h-min">QUALITY PRESET</b>
					<LauncherCombobox
						classes="font-minecraft"
						bind:value={changes.qualityPreset.value}
						options={['none', 'low', 'medium', 'high']}
						on:input={() => {
							setUpdatedPropery('qualityPreset');
						}}
					/>
				</div>
			</div>
		{/if}

		<div class="border-b-2 border-surface-separator my-2" />

		<div class="flex gap-2 pt-4 justify-end text-white font-minecraft font-bold">
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
</style>
