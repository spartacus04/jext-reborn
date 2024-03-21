import type { Disc } from "$lib/types";
import JSZip from "jszip";
import { get, type Writable } from "svelte/store";
import default_disc from "$lib/assets/default_disc.png";
import default_fragment from "$lib/assets/default_fragment.png";
import { resourcePackStore } from "$lib/config";

export const importRP = async (discStore: Writable<Disc[]>, rp: Blob, discsJson: Disc[]) => {
    const zip = await JSZip.loadAsync(rp)

    discStore.set(await Promise.all(discsJson.map(async disc => {
        const namespace = disc['disc-namespace'].substring(disc['disc-namespace'].indexOf('.') + 1);

        const track = await zip.file(`assets/minecraft/sounds/records/${namespace}.ogg`)!.async('blob')

        const texture = (await zip.file(`assets/minecraft/textures/item/music_disc_${namespace}.png`)?.async('blob')) ?? new Blob([
            await (await fetch(default_disc)).blob()
        ], { type: 'image/png' })

        const fragmentTexture = (await zip.file(`assets/minecraft/textures/item/fragment_${namespace}.png`)?.async('blob')) ?? new Blob([
            await (await fetch(default_fragment)).blob()
        ], { type: 'image/png' })

        return {
            ...disc,
            packData: {
                track,
                texture,
                fragmentTexture,
            }
        }
    })))

    resourcePackStore.set({
        icon: await zip.file('pack.png')?.async('blob') ?? get(resourcePackStore).icon,
        description: JSON.parse(await zip.file('pack.mcmeta')!.async('text')).pack.description as string,
        version: JSON.parse(await zip.file('pack.mcmeta')!.async('text')).pack.version as number,
        packs: [{
            name: 'Imported Pack',
            icon: await zip.file('pack.png')?.async('blob') ?? get(resourcePackStore).icon,
            value: rp
        }],
    })
}