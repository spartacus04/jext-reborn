import { writable } from "svelte/store";
import { loadFFmpeg } from "./ffmpeg";
import { preProcessDiscs } from "./preprocessor";
import type { BaseExporter } from "./baseExporter";

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
    exporterSteps.set([
        { status: 'Loading FFmpeg', current: 0, total: 3 },
        undefined,
        undefined,
    ]);
    const ffmpeg = await loadFFmpeg();

    updateSteps(0, 'Building music objects', 1, 3);
    await preProcessDiscs(ffmpeg);

    updateSteps(0, 'Building resource packs', 2, 3);
    const output = await exporter.export();
}