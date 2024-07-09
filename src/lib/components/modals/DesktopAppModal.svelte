<script lang="ts">
	import { default_icon } from '$lib/assets';
	import LauncherButton from '../buttons/LauncherButton.svelte';
	let dialog: HTMLDialogElement;

	export const open = () => dialog.show();
	export const close = () => dialog.close();
	export const isOpen = () => dialog.open;
	export const toggle = () => (dialog.open ? close() : open());
	export const openModal = () => dialog.showModal();

	const onClick = (e: MouseEvent) => {
		const id = (e.target as HTMLElement).id;
		console.debug(id);

		if (id === 'backdrop') close();
		else e.stopPropagation();
	};

	const getDesktopAppDownload = (async () => {
		const response = await fetch(
			'https://api.github.com/repos/spartacus04/jext-reborn/actions/workflows/build-tauri.yml/runs?status=success&per_page=1'
		);

		const json = await response.json();

		if (json.total_count == 0) return;

		return `${json.workflow_runs[0].html_url}#artifacts`;
	})();

	const download = async () => {
		const url = await getDesktopAppDownload;
		window.open(url, '_blank');
	};
</script>

<!-- svelte-ignore a11y-no-noninteractive-element-interactions a11y-click-events-have-key-events -->
<dialog
	bind:this={dialog}
	id="backdrop"
	on:click={onClick}
	class="bg-surface-background md:bg-transparent p-0"
>
	<div
		class="m-0 md:m-auto md:w-[32rem] h-fit rounded-md bg-surface-background animate-slide-down flex flex-col items-center justify-center md:block"
	>
		<div class="w-full rounded-t-md bg-[#262523] flex items-center justify-center">
			<img src={default_icon} alt="icon" class="w-20 m-4" />
		</div>
		<div class="p-4 font-minecraft text-white">
			<h1 class="font-minecraft-title text-2xl">Download the desktop app</h1>
			<p>By downloading the desktop app you get access to some exclusive features such as:</p>
			<ul class="list-disc ml-4 mb-2">
				<li>Faster generation speed</li>
				<li>Bulk importing</li>
				<li>Direct download from Youtube/Soundcloud</li>
				<li>Remote disc management</li>
				<li>Remote configuration management</li>
			</ul>
			<hr class="-mx-4 border-surface-separator" />
			<div class="flex pt-4 justify-end">
				<LauncherButton text="Dismiss" on:click={close} />
				<div class="w-2" />
				<LauncherButton text="Download" type="primary" on:click={download} />
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
