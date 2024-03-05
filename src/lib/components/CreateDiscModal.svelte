<script lang="ts">
	import { randomDiscTexture } from "$lib/resourcepack/discs";
	import type { Disc } from "$lib/types";
	import { FileDropzone, ProgressRadial } from "@skeletonlabs/skeleton";

    let files: FileList;
    let discs: Promise<Disc>[] = [];

    const onFilesChange = () => {
        console.log(files);

        for(let i = 0; i < files.length; i++) {
            const file = files[i];

            const splitName = file.name.replace(/\.[^\.]+$/g, '').split(/ ?- ?/g);

            let name = 'Song name';
            let author = 'Unknown artist';

            if(splitName.length >= 1) name = splitName.shift()!;
            if(splitName.length >= 1) author = splitName.join(' - ');

            discs.push((async () => {
                const discTexture = await randomDiscTexture();
                const fragmentTexture = await randomDiscTexture();

                console.log(discTexture, fragmentTexture);

                const disc : Disc = {
                    name,
                    author,
                    "creeper-drop": true,
                    "model-data": -1,
                    "disc-namespace": `${name}${author}`
                        .replace(/[^a-zA-Z0-9]/g, '')
                        .replaceAll('1', 'one')
                        .replaceAll('2', 'two')
                        .replaceAll('3', 'three')
                        .replaceAll('4', 'four')
                        .replaceAll('5', 'five')
                        .replaceAll('6', 'six')
                        .replaceAll('7', 'seven')
                        .replaceAll('8', 'eight')
                        .replaceAll('9', 'nine')
                        .replaceAll('0', 'zero')
                        .toLowerCase(),
                    "loot-tables": {},
                    "fragment-loot-tables": {},
                    duration: -1,
                    lores: [],
                    uploadData: {
                        uploadedFragmentTexture: fragmentTexture!,
                        uploadedTexture: discTexture!,
                        uploadedTrack: file,
                        mono: true,
                        normalize: true,
                        quality: "high",
                    },
                    packData: null,
                };

                console.log(disc);

                console.log(URL.createObjectURL(disc.uploadData.uploadedTexture));

                return disc;
            })())
        }
    }
</script>

<main class="bg-surface-500 rounded-md p-2 h-[50%] overflow-y-auto w-[90%] sm:w-[80%] lg:w-[50%]">
	<h3 class="h3 mb-2">Upload one or more music file</h3>
    <FileDropzone name="discs" accept="audio/*" bind:files on:change={onFilesChange}>
        <svelte:fragment slot="message">
            Upload one or more music files or drag and drop
        </svelte:fragment>
        <svelte:fragment slot="meta">
            <p>Only audio files are allowed</p>
        </svelte:fragment>
    </FileDropzone>

    <div class="grid variant-filled-primary m-1 w-[calc(100%_-_0.5rem)] rounded-sm">
        {#each discs as discPromise}
            {#await discPromise}
                <div class="flex items-center justify-center">
                    <ProgressRadial />
                </div>
            {:then disc}
                <div class="flex items-center justify-center">
                    <img src={URL.createObjectURL(disc.uploadData.uploadedTexture)} alt="Disc texture" class="w-10 h-10" />
                    <p>{disc.name} - {disc.author}</p>
                </div>
            {/await}
            
        {/each}
    </div>
</main>