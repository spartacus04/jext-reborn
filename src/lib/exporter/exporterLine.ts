import { get, writable } from "svelte/store";
import { loadFFmpeg } from "./ffmpeg";
import { preProcessDiscs } from "./preprocessor";
import type { BaseExporter } from "./baseExporter";
import { ResourcePackData } from "$lib/discs/resourcePackManager";
import { mergeResourcePacks } from "./rpmerger";

export const exporterSteps = writable<({
    status: string,
    current: number,
    total: number,
}|undefined)[]>([]);

export const removeStep = (index: number) => {
    exporterSteps.update((steps) => {
        steps[index] = undefined;
        return steps;
    });
}

export const updateSteps = (index: number, status: string, current: number, total: number) => {
    exporterSteps.update((steps) => {
        steps[index] = { status, current, total };
        return steps;
    });
};

export const exportResourcePack = async (exporter: BaseExporter) => {
    const pack = get(ResourcePackData);

    exporterSteps.set([
        { status: 'Loading FFmpeg', current: 0, total: pack.packs.length > 0 ? 4 : 3 },
        undefined,
        undefined,
    ]);
    const ffmpeg = await loadFFmpeg();

    updateSteps(0, 'Building music objects', 1, pack.packs.length > 0 ? 4 : 3);
    await preProcessDiscs(ffmpeg);

    updateSteps(0, 'Building resource pack', 2, pack.packs.length > 0 ? 4 : 3);
    const output = await exporter.export();

    if(pack.packs.length > 0) {
        updateSteps(0, 'Merging resource pack', 3, 4);

        const merged = await mergeResourcePacks(output.javaRP);

        updateSteps(0, 'Finishing up', 4, 4);

        return {
            javaRP: merged,
            bedrockRP: output.bedrockRP
        };
    }

    updateSteps(0, 'Finishing up', 3, 3);

    return output;
}