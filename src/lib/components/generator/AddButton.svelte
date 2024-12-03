<script lang="ts">
	import { dropFile, inputFile } from "$lib/directives";
	import { createNewDisc } from "$lib/discs/discManager";
	import { isTauri } from "$lib/state";
	import DownloadModal from "../modals/DownloadModal.svelte";

    let downloadModal: DownloadModal;
</script>

<DownloadModal bind:this={downloadModal} />

<div class="flex p-3 mb-2">
    <button class="flex-1 text-[#aeaeae] bg-[#404040] hover:bg-[#505050] p-2 flex justify-center border-black border-2" use:inputFile={{
        accept: ".zip,.nbs,.mp3,.wav,.ogg,.aac,.flac",
        multiple: true,
        cb: createNewDisc,
    }} use:dropFile={{
        accept: ".zip,.nbs,.mp3,.wav,.ogg,.aac,.flac",
        multiple: true,
        cb: createNewDisc,
    }}>
        <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-14 w-14"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
        >
            <path
                stroke-width="2"
                d="M12 6v6m0 0v6m0-6h6m-6 0H6"
            />
        </svg>
    </button>

    {#if isTauri}
        <button class="ml-4 aspect-square text-[#aeaeae] bg-[#404040] hover:bg-[#505050] p-2 flex justify-center border-black border-2" on:click={() => downloadModal.openModal()}>
            <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-14 w-14"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
            >
                <path
                    stroke-width="2"
                    d="M19 14l-7 7m0 0l-7-7m7 7V3"
                />
                <path
                    stroke-width="2"
                    d="M5 21h14"
                />
            </svg>
        </button>
    {/if}
</div>