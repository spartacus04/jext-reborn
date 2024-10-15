import { writable } from "svelte/store";
import type { BaseDisc } from "./baseDisc";
import { MusicDisc } from "./musicDisc";

export const discsStore = writable<BaseDisc[]>([]);
export const selectedDiscs = writable<BaseDisc[]>([]);

export function addDisc(disc: BaseDisc) {
    console.debug(disc);
    discsStore.update((discs) => [...discs, disc]);
}

export function createNewDisc(files: File[] | undefined) {
    if (!files) return;

    console.debug(files);

    files.forEach(file => {
        console.debug(file.type)

        if(file.type.includes('audio')) {
            const audioDisc = new MusicDisc(file);

            audioDisc.RerollTextures().then(() => {
                addDisc(audioDisc);
            })
        } else if(file.type.endsWith('.zip')) {
            // TODO: setup zip file handling
        } else if(file.name.endsWith('.nbs')) {
            // TODO: setup nbs file handling
        }
    })
}

export function removeDisc(disc: BaseDisc) {
    discsStore.update((discs) => discs.filter(d => d !== disc));
}

export function editDiscs(...discs: BaseDisc[]) {
    
}