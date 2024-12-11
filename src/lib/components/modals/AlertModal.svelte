<script lang="ts">
	import LauncherButton from '../buttons/LauncherButton.svelte';
	let dialog: HTMLDialogElement;

	export const open = () => dialog.show();
	export const close = () => {
		dialog.close();
		onFinish();
	};
	export const isOpen = () => dialog.open;
	export const toggle = () => (dialog.open ? close() : open());
	export const openModal = () => dialog.showModal();

	export let text: string = 'Are you sure?';
	export let confirmText: string = 'Ok';

	export let onFinish: () => unknown;

	const onClick = (e: MouseEvent) => {
		const id = (e.target as HTMLElement).id;

		if (id === 'backdrop') {
			close();
		} else e.stopPropagation();
	};
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
			<h1 class="font-minecraft text-xl mb-3">{text}</h1>

			<hr class="-mx-4 border-surface-separator" />

			<div class="flex gap-2 pt-4 justify-end">
				<LauncherButton text={confirmText} type="primary" on:click={() => close()} />
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
