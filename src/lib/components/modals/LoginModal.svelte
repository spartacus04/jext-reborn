<script lang="ts">
	import { PluginConnector, pluginConnectorStore } from '$lib/pluginAccess/pluginConnector';
	import { cAlert, cConfirm } from '$lib/utils';
	import LauncherButton from '../buttons/LauncherButton.svelte';
	import LauncherTextbox from '../inputs/LauncherTextbox.svelte';
	import { addDisc, discsStore } from '$lib/discs/discManager';
	import { areDefaultPackSettings, ResourcePackData } from '$lib/discs/resourcePackManager';
	import { downloadQueue } from '$lib/downloader/downloader';
	import { JextReader } from '$lib/exporter/importer';
	let dialog: HTMLDialogElement;

	export const open = () => dialog.show();
	export const close = () => dialog.close();
	export const isOpen = () => dialog.open;
	export const toggle = () => (dialog.open ? close() : open());
	export const openModal = () => dialog.showModal();

	const onClick = (e: MouseEvent) => {
		const id = (e.target as HTMLElement).id;

		if (id === 'backdrop') {
			close()
		} else e.stopPropagation();
	};

    const connect = async () => {
        if(address.trim() === '') {
            await cAlert('Please enter a valid address');
            return;
        }

		if(
			$discsStore.length != 0 ||
			!areDefaultPackSettings() ||
			$downloadQueue.length != 0
		) {
			const result = await cConfirm({
				text: "Warning: connecting to the JEXT server will reset all the progress in the disc manager. Do you still wish to continue?",
				confirmText: "Yes",
				discardText: "No",
				cancelText: undefined
			})

			if(result == 'discard') return;
		}

        try {
            const connector = await PluginConnector.fromPassword(address, password);
            $pluginConnectorStore = connector;
            close();
            await cAlert('Connected to JEXT WebAPI Successfully! Importing discs form Server...');

			const results = await $pluginConnectorStore.getDiscs()

			$discsStore = [];
			$downloadQueue = [];

			if(results != null) {
				const discs = await JextReader(results);

				discs.forEach(disc => {
					addDisc(disc);
				});
			}

			await cAlert('Discs imported successfully!');
        } catch(e: any) {
            await cAlert(e);
        }
    }

    let address: string;
    let password: string;
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
		<div class="p-4 text-white flex flex-col gap-4">
			<h1 class="font-minecraft text-xl">Connect to the JEXT WebAPI</h1>
            <div class="flex flex-col flex-1">
                <b class="text-[#aeaeae] text-xs h-min">JEXT SERVER ADDRESS</b>
                <LauncherTextbox bind:value={address} placeholder="http://localhost:9871" />
            </div>

            <div class="flex flex-col flex-1">
                <b class="text-[#aeaeae] text-xs h-min">JEXT SERVER PASSWORD</b>
                <LauncherTextbox bind:value={password} placeholder="Password: Leave empty if unset" />
            </div>

			<hr class="-mx-4 border-surface-separator" />

			<div class="flex gap-2 justify-end font-minecraft">
				<LauncherButton text="Cancel" type="tertiary" on:click={close} />
				<LauncherButton text="Connect!" type="primary" on:click={connect} />
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
