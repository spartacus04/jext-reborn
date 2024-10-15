<script lang="ts">
    // This is going to be extremely ugly, but I want to get this done ASAP
	import type { BaseDisc } from "$lib/discs/baseDisc";
	import { MusicDisc } from "$lib/discs/musicDisc";

    export let discs: BaseDisc[] = [];
	let dialog: HTMLDialogElement;

    export let changes: { [key: string]: {
        value: any;
        edited: boolean;
    }} = {};

    // Set up the changes object
    changes = {
        'title': {
            value: discs.every(disc => disc.title === discs[0].title) ? discs[0].title : '',
            edited: false,
        },
        'author': {
            value: discs.every(disc => disc.author === discs[0].author) ? discs[0].author : '',
            edited: false,
        },
        'discTexture': {
            value: discs.every(disc => disc.discTexture === discs[0].discTexture) ? discs[0].discTexture : '',
            edited: false,
        },
        'fragmentTexture': {
            value: discs.every(disc => disc.fragmentTexture === discs[0].fragmentTexture) ? discs[0].fragmentTexture : '',
            edited: false,
        },
        'tooltip': {
            value: discs.every(disc => disc.tooltip === discs[0].tooltip) ? discs[0].tooltip : '',
            edited: false,
        },
        'redstonePower': {
            value: discs.every(disc => disc.redstonePower === discs[0].redstonePower) ? discs[0].redstonePower : '',
            edited: false,
        },
        'creeperDroppable': {
            value: discs.every(disc => disc.creeperDroppable === discs[0].creeperDroppable) ? discs[0].creeperDroppable : '',
            edited: false,
        },
        'discLootTables': {
            value: discs.every(disc => disc.discLootTables === discs[0].discLootTables) ? discs[0].discLootTables : '',
            edited: false,
        },
        'fragmentLootTables': {
            value: discs.every(disc => disc.fragmentLootTables === discs[0].fragmentLootTables) ? discs[0].fragmentLootTables : '',
            edited: false,
        },
    }

    if(discs.some(disc => disc instanceof MusicDisc)) {
        const musicDiscs = discs.filter(disc => disc instanceof MusicDisc);

        changes['monoChannel'] = {
            value: musicDiscs.every(disc => disc.monoChannel === musicDiscs[0].monoChannel) ? musicDiscs[0].monoChannel : '',
            edited: false,
        };

        changes['normalizeVolume'] = {
            value: musicDiscs.every(disc => disc.normalizeVolume === musicDiscs[0].normalizeVolume) ? musicDiscs[0].normalizeVolume : '',
            edited: false,
        };

        changes['qualityPreset'] = {
            value: musicDiscs.every(disc => disc.qualityPreset === musicDiscs[0].qualityPreset) ? musicDiscs[0].qualityPreset : '',
            edited: false,
        };
    }
    
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
