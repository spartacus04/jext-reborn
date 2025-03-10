<script lang="ts">
	import LauncherButton from '../buttons/LauncherButton.svelte';

	let dialog: HTMLDialogElement;

	export const open = () => dialog.show();
	export const close = () => dialog.close();
	export const isOpen = () => dialog.open;
	export const toggle = () => (dialog.open ? close() : open());
	export const openModal = () => dialog.showModal();

	const onClick = async (e: MouseEvent) => {
		const id = (e.target as HTMLElement).id;

		console.log(id);

		if (id === 'backdrop') {
			close();
		} else e.stopPropagation();
	};

    export let licenses: any[];
</script>

<!-- svelte-ignore a11y-no-noninteractive-element-interactions a11y-click-events-have-key-events -->
<dialog
	bind:this={dialog}
	id="backdrop"
	on:click={onClick}
	class="bg-surface-background md:w-[51rem] md:bg-transparent p-0 backdrop:opacity-55 backdrop:bg-surface-background h-[80%] pt-10 w-full"
>
	<div
		class="m-0 md:m-auto md:w-[50rem] h-fit rounded-md bg-surface-background animate-slide-down block p-4 text-white shadow-md overflow-y-auto max-h-full"
	>
		<!-- This is a stupid ass hack -->
		<p
			class="md:hidden overflow-hidden whitespace-nowrap text-ellipsis w-[calc(100%)] h-0 select-none pointer-events-none"
		>
			Looking at the source code? Welp, can't do anything about it since this is open source
		</p>


        <div class="flex flex-col gap-4">
            {#each licenses as license}
                <div class="flex flex-col">
                    <h1 class="text-2xl">{license.name} - {license.version}</h1>

                    <p class="text-wrap w-full bg-surface-background-variant max-h-64 overflow-y-auto">
                        {license.license}
                    </p>
                </div>
            {/each}
        </div>

		<div class="flex gap-2 pt-4 justify-end text-white font-minecraft font-bold">
			<LauncherButton
				text="Close"
				type="tertiary"
				on:click={() => {
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
