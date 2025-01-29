<script lang="ts">
	import { soundcloud, spotify, youtube } from '$lib/assets';
	import { downloaderLine } from '$lib/downloader/downloader';
	import LauncherButton from '../buttons/LauncherButton.svelte';
	import LauncherTextbox from '../inputs/LauncherTextbox.svelte';
	let dialog: HTMLDialogElement;

	export const open = () => {
		url = '';
		dialog.show();
	};
	export const close = () => {
		dialog.close();
	};

	export const isOpen = () => dialog.open;
	export const toggle = () => (dialog.open ? close() : open());
	export const openModal = () => {
		url = '';
		dialog.showModal();
	};

	const onClick = (e: MouseEvent) => {
		const id = (e.target as HTMLElement).id;

		if (id === 'backdrop') {
			close();
		} else e.stopPropagation();
	};

	let url = '';
</script>

<!-- svelte-ignore a11y-no-noninteractive-element-interactions a11y-click-events-have-key-events -->
<dialog
	bind:this={dialog}
	id="backdrop"
	on:click={onClick}
	class="bg-surface-background p-0 backdrop:opacity-55 backdrop:bg-surface-background"
>
	<div
		class="m-0 md:m-auto md:w-[32rem] h-fit rounded-md bg-surface-background animate-slide-down flex flex-col items-center justify-center md:block"
	>
		<div class="p-4 font-minecraft text-white">
			<div class="font-minecraft text-xl mb-3 flex items-center gap-2">
				Download from

				<img src={youtube} alt="" class="h-8 aspect-square" />
				<img src={soundcloud} alt="" class="h-8 aspect-square" />
				<!-- <img src={spotify} alt="" class="h-8 aspect-square"> -->
			</div>

			<hr class="-mx-4 border-surface-separator" />

			<div class="flex flex-col flex-1 my-4">
				<b class="text-[#aeaeae] text-xs h-min">TRACK/PLAYLIST/ALBUM URL</b>
				<LauncherTextbox placeholder="Track/Playlist/Album url" bind:value={url} />
			</div>

			<hr class="-mx-4 border-surface-separator" />

			<div class="flex gap-2 pt-4 justify-end">
				<LauncherButton text="Cancel" type="tertiary" on:click={() => close()} />
				<LauncherButton
					text="Download!"
					type="primary"
					on:click={() => {
						downloaderLine(url);
						close();
					}}
				/>
			</div>
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
