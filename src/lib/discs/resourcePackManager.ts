import { default_icon } from "$lib/assets";
import { versions } from "$lib/constants";
import { writable } from "svelte/store";

interface PackData {
    icon: Blob;
    name: string;
    contents: Blob;
}

export interface ExportPackData {
    icon: Blob;
    name: string;
    description: string;
    version: number;
    packs: PackData[];
}

export const ResourcePackData = writable<ExportPackData>({
    icon: new Blob(),
    name: 'Jext Resources',
    description: 'Adds custom music discs to Minecraft!',
    version: Math.max(...Array.from(versions.keys())),
    packs: []
});

// hack to load the default icon
(async () => {
    const response = await fetch(default_icon);
    const icon = await response.blob();

    ResourcePackData.update(data => {
        data.icon = icon;
        return data;
    });
})();