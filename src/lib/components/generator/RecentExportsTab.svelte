<script lang="ts">
    import { exists, readDir, writeFile, mkdir, remove, stat, type DirEntry, readFile } from '@tauri-apps/plugin-fs'
    import { appCacheDir } from '@tauri-apps/api/path';
	import JSZip from 'jszip';
	import LauncherButton from '../buttons/LauncherButton.svelte';
	import { addDisc } from '$lib/discs/discManager';
	import { JextReader } from '$lib/exporter/importer';
	import { cAlert } from '$lib/utils';

    const getRecentExportsPromise = async () => {
        const baseDir = await appCacheDir() + '/recentExports';

        if(!(await exists(baseDir))) {
            return [];
        }

        const recentExports = (await readDir(baseDir)).map(entry => entry.name);

        const getDateTime = (name: string) => {
            const date = name.split('-')[1].replaceAll('+', '-').replaceAll('=', ':');
            return new Date(date);
        }

        const sortedExports = recentExports.sort((a, b) => getDateTime(b).getTime() - getDateTime(a).getTime());

        return await Promise.all(sortedExports.map(async (filename) => {
            const splitted = filename.split('-');
            splitted.shift();
            splitted.shift();
            const name = splitted.join('-')

            // open file with JSZip and get the pack.mcmeta file
            const file = await readFile(`${baseDir}/${filename}`);
            const zip = new JSZip();

            const zipFile = await zip.loadAsync(file);

            const packMcMeta = await zipFile.file('pack.mcmeta')?.async('text')!;

            const description = JSON.parse(packMcMeta).pack.description;
            const icon = await zipFile.file('pack.png')?.async('base64')!;

            return {
                name,
                description,
                icon,
                filename,
                date: getDateTime(filename)
            }
        }));
    }

    const importPack = async (filename: string) => {
        const binary = await readFile(`${await appCacheDir()}/recentExports/${filename}`);
        const blob = new Blob([binary], { type: 'application/zip' });

        const discs = await JextReader(blob);

        for(const disc of discs) {
            await addDisc(disc);
        }

        await cAlert('Successfully imported the pack!');
    }
</script>

{#await getRecentExportsPromise()}
<div class="flex w-full h-full items-center justify-center">
    <h1 class="font-minecraft text-white text-2xl">Fetching recent exports...</h1>
</div>
{:then recentExports} 
    {#if recentExports.length == 0}
        <div class="flex w-full h-full items-center justify-center">
            <h1 class="font-minecraft text-white text-2xl">No recent exports found</h1>
        </div>
    {:else} 
        <div class="grid p-4 pt-2 gap-2 grid-cols-1 xs:grid-cols-2 lg:grid-cols-1 overflow-y-auto overflow-x-hidden">
            <div class="flex w-full mt-4 items-center justify-center">
                <h1 class="font-minecraft text-white text-2xl">Recent exports</h1>
            </div>
            {#each recentExports as exportRp}
                <div
                    class="flex items-center justify-between p-4 bg-surface-separator flex-col lg:flex-row text-white font-minecraft"
                >
                    <div class="flex flex-col lg:flex-row items-center w-full flex-shrink lg:max-w-[85%]">
                        <img src={`data:image/png;base64, ${exportRp.icon}`} alt={exportRp.filename} class="w-28 lg:w-20 lg:h-20 rounded-lg" />
                        <div class="lg:ml-4 block flex-shrink overflow-x-auto w-full">
                            <h3
                                class="h3 text-white font-bold font-minecraft text-lg lg:text-2xl w-full overflow-hidden whitespace-nowrap text-ellipsis"
                            >
                                {exportRp.name} - {exportRp.date.toLocaleDateString()} {exportRp.date.toLocaleTimeString()}
                            </h3>
                            <p
                                class="text-gray-400 font-minecraft w-full overflow-hidden whitespace-nowrap text-ellipsis"
                            >
                                {exportRp.description}
                            </p>
                        </div>
                    </div>

                    <div class="lg:ml-4 flex gap-2 lg:flex-col">
                        <LauncherButton
                            text="Import"
                            type="primary"
                            on:click={async () => {
                                await importPack(exportRp.filename);
                            }}
                        />
                    </div>
                </div>
            {/each}
        </div>
    {/if}   
{/await}