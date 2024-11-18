import { get, writable } from "svelte/store";
import type { BaseDisc } from "./baseDisc";
import { MusicDisc } from "./musicDisc";
import EditDiscsModal from "$lib/components/modals/EditDiscsModal.svelte";
import { baseElement } from "$lib/state";
import { NbsDisc } from "./nbsDisc";
import EditDungeonModal from "$lib/components/modals/EditDungeonModal.svelte";

export const discsStore = writable<BaseDisc[]>([]);
export const selectedDiscs = writable<BaseDisc[]>([]);

export function addDisc(disc: BaseDisc) {
    discsStore.update((discs) => [...discs, disc]);
}

export function createNewDisc(files: File[] | undefined) {
    if (!files) return;

    files.forEach(async file => {
        if(file.type.includes('audio')) {
            const audioDisc = new MusicDisc(file);

            audioDisc.RerollTextures().then(() => {
                addDisc(audioDisc);
            })
        } else if(file.type.endsWith('.zip')) {
            // TODO: setup zip file handling
        } else if(file.name.endsWith('.nbs')) {
            const nbsDisc = new NbsDisc(file, await file.arrayBuffer());

            nbsDisc.RerollTextures().then(() => {
                addDisc(nbsDisc);
            })
        }
    })
}

export function removeDisc(disc: BaseDisc) {
    discsStore.update((discs) => discs.filter(d => d !== disc));
}

export async function editDiscs(...discs: BaseDisc[]) {
    const changes = await new Promise<{ [key: string]: unknown }>(resolve => {
        const editDiscsModal = new EditDiscsModal({
            target: get(baseElement)!,
            props: {
                onFinish: (changes) => {
                    editDiscsModal.$destroy();
                    resolve(changes);
                },
                discs,
            }
        });
    
        editDiscsModal.openModal();
    });

    for(const change of Object.keys(changes)) {
        for(let i = 0; i < discs.length; i++) {
            if(['monoChannel', 'normalizeVolume', 'qualityPreset'].includes(change) && !isMusicDisc(discs[i])) continue;

            // @ts-expect-error this is fine
            discs[i][change] = changes[change];
            if(discs[i].isNew) {
                discs[i].autoSetNamespace();
                discs[i].refreshTextures();
            }
        }
    }
}

export async function editLootTables(data: { [key: string]: number }) {
    const dataCopy = { ...data };

    return await new Promise<{ [key: string]: number }|null>(resolve => {
        const editDungeonModal = new EditDungeonModal({
            target: get(baseElement)!,
            props: {
                onFinish: (changes) => {
                    editDungeonModal.$destroy();
                    resolve(changes);
                },
                dungeons: dataCopy,
            }
        });
    
        editDungeonModal.openModal();
    });
}

export function isMusicDisc(disc: BaseDisc): disc is MusicDisc {
    // @ts-expect-error this is fine
    return disc instanceof MusicDisc || disc['audioFile'] !== undefined;
}