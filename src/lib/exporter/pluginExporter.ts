import JSZip from "jszip";
import { BaseExporter } from "./baseExporter";
import { get } from "svelte/store";
import { discsStore, isMusicDisc } from "$lib/discs/discManager";
import type { NbsDisc } from "$lib/discs/nbsDisc";
import { getDuration } from "$lib/utils";
import { ResourcePackData } from "$lib/discs/resourcePackManager";

export class PluginExporter extends BaseExporter {
    public name = 'JEXT Reborn';
    public icon: string = '';
    public subtitle: string = '';

    public async export() {
        return {
            javaRP: await this.buildJavaRP(),
            bedrockRP: await this.buildBedrockRP(),
        }
    }

    private async buildJavaRP(): Promise<Blob> {
        const packData = get(ResourcePackData);

        const rp = new JSZip();

        const discs = get(discsStore);

        rp.file(
            'pack.mcmeta',
            JSON.stringify({
                pack: {
                    pack_format: packData.version,
                    description: packData.description
                }
            }, null, 2)
        );

        rp.file(
            'pack.png',
            packData.icon
        )

        const sounds: { [key: string]: unknown } = {};

        for(const disc of discs) {
            if(isMusicDisc(disc)) {
                sounds[disc.namespace] = {
                    sounds: [
                        {
                            name: `records/${disc.namespace}`,
                            stream: true
                        }
                    ]
                }
            }
        };

        rp.file("assets/minecraft/sounds.json", JSON.stringify(sounds, null, 2));

        rp.file(
            "assets/minecraft/models/item/music_disc_11.json",
            JSON.stringify(
                {
                    parent: 'item/generated',
                    textures: {
                        layer0: 'item/music_disc_11'
                    },
                    overrides: discs.map((disc) => {
                        return {
                            predicate: {
                                custom_model_data: disc.modelData
                            },
                            model: `item/music_disc_${disc.namespace}`
                        };
                    })
                }, null, 2
            )
        );

        rp.file(
            "assets/minecraft/models/item/music_disc_5.json",
            JSON.stringify(
                {
                    parent: 'item/generated',
                    textures: {
                        layer0: 'item/music_disc_5'
                    },
                    overrides: discs.map((disc) => {
                        return {
                            predicate: {
                                custom_model_data: disc.modelData
                            },
                            model: `item/fragment_${disc.namespace}`
                        };
                    })
                }, null, 2
            )
        );

        for(const disc of discs) {
            rp.file(
                `assets/minecraft/models/item/music_disc_${disc.namespace}.json`,
                JSON.stringify(
                    {
                        parent: 'item/generated',
                        textures: {
                            layer0: `item/music_disc_${disc.namespace}`
                        }
                    }, null, 2
                )
            );
    
            // fragment model
            rp.file(
                `assets/minecraft/models/item/fragment_${disc.namespace}.json`,
                JSON.stringify(
                    {
                        parent: 'item/generated',
                        textures: {
                            layer0: `item/fragment_${disc.namespace}`
                        }
                    }, null, 2
                )
            );
    
            // disc texture
            rp.file(
                `assets/minecraft/textures/item/music_disc_${disc.namespace}.png`,
                disc.discTexture
            );
    
            // fragment texture
            rp.file(
                `assets/minecraft/textures/item/fragment_${disc.namespace}.png`,
                disc.fragmentTexture
            );
    
            // track
            if(isMusicDisc(disc)) {
                rp.file(`assets/minecraft/sounds/records/${disc.namespace}.ogg`, disc.cachedFinalAudioFile);
            } else {
                rp.file(`assets/jext/${disc.namespace}.nbs`, (disc as NbsDisc).nbsFile);
            }
        }

        rp.file(
            'jext.json',
            JSON.stringify(
                discs.filter(isMusicDisc).map(disc => {
                    return {
                        title: disc.title,
                        author: disc.author,
                        duration: getDuration(disc.cachedFinalAudioFile!),
                        'disc-namespace': disc.namespace,
                        'model-data': disc.modelData,
                        'creeper-drop': disc.creeperDroppable,
                        'lores': disc.tooltip.split('\n'),
                        'loot-tables': disc.discLootTables,
                        'fragment-loot-tables': disc.fragmentLootTables
                    }
                }), null, 2
            )
        );

        rp.file(
            'jext.nbs.json',
            JSON.stringify(
                discs.filter(d => !isMusicDisc(d)).map(disc => {
                    return {
                        title: disc.title,
                        author: disc.author,
                        'disc-namespace': disc.namespace,
                        'model-data': disc.modelData,
                        'lores': disc.tooltip.split('\n'),
                        'loot-tables': disc.discLootTables,
                        'fragment-loot-tables': disc.fragmentLootTables
                    }
                }), null, 2
            )
        );

        return await rp.generateAsync({ type: 'blob' });
    }

    private async buildBedrockRP(): Promise<Blob|undefined> {
        return undefined;
    }
}