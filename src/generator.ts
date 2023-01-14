import JSZip from 'jszip';
import { resizeImageBlob, saveAs } from './utils';
import type { songData } from './config';
import README from './assets/readme.txt';
import { versionStore } from './store';
import { isMinecraftRP } from './importer';

let version : number;

versionStore.subscribe(v => version = v);

export const generatePack = async (data: songData[], icon : string, name : string, mono : boolean, merge : boolean) => {
    const rp = new JSZip();

    const packmcmeta = `
        {"pack": {"pack_format": ${version},"description": "Adds custom musics discs"}}
    `;

    rp.file('pack.mcmeta', packmcmeta);

    const packIcon = icon;
    const packpng = await (await (await fetch(packIcon)).blob()).arrayBuffer();

    rp.file('pack.png', packpng);

    const minecraft = rp.folder('assets')!.folder('minecraft')!;

    const soundsjson : {
        [key: string]: {
            sounds: {
                name: string,
                stream: boolean
            }[]
        }
    } = {};

    data.forEach(disc => {
        soundsjson[`music_disc.${disc.namespace}`] = {
            sounds: [
                {
                    name: `records/${disc.namespace}`,
                    stream: true,
                },
            ],
        };
    });

    minecraft.file('sounds.json', JSON.stringify(soundsjson, null, 2));

    const models = minecraft.folder('models')!.folder('item')!;
    const sounds = minecraft.folder('sounds')!.folder('records')!;
    const textures = minecraft.folder('textures')!.folder('item')!;

    const m11 = {
        parent: 'item/generated',
        textures: {
            layer0: 'item/music_disc_11',
        },
        overrides: data.map((disc, i) => {
            return {
                predicate: {
                    custom_model_data: i + 1,
                },
                model: `item/music_disc_${disc.namespace}`,
            };
        }),
    };

    models.file('music_disc_11.json', JSON.stringify(m11, null, 2));

    for(let i = 0; i < data.length; i++) {
        const disc = data[i];

        const resizedTexture = await (await resizeImageBlob(disc.texture, 16, 16)).arrayBuffer();
        textures.file(`music_disc_${disc.namespace}.png`, resizedTexture);

        const soundbuffer = mono ? await disc.monoFile.arrayBuffer() : await disc.oggFile.arrayBuffer();

        sounds.file(`${disc.namespace}.ogg`, soundbuffer);

        console.log(disc);
        models.file(`music_disc_${disc.namespace}.json`, JSON.stringify({
            parent: 'item/generated',
            textures: {
                layer0: `item/music_disc_${disc.namespace}`,
            },
        }, null, 2));
    }

    if(merge) {
        const input = document.createElement('input');
        input.type = 'file';
        input.accept = '.zip';
        input.click();

        await new Promise<void>((resolve, reject) => {
            input.onchange = async () => {
                if(!input.files || input.files.length == 0) reject('No file was selected');

                if(!(await isMinecraftRP(input.files[0]))) reject('The selected file is not a valid resource pack');

                const ufile = input.files[0];
                const zip = await JSZip.loadAsync(ufile);

                // foreach file in the zip
                await zip.forEach(async (path, nfile) => {
                    if(path == 'pack.mcmeta') return;
                    if(path == 'pack.png') return;

                    if(nfile.dir) {
                        if(!rp.folder(path)) rp.folder(path);
                    }
                    else if(!rp.file(path)) {
                        rp.file(path, await nfile.async('arraybuffer'));
                    }
                    else {
                        reject('Cannot merge resource pack manually as there are conflicting files');
                    }
                });

                resolve();
            };
        }).catch(e => {
            return alert(e);
        });
    }

    rp.generateAsync({ type: 'blob' }).then(async content => {
        const zip = new JSZip();

        const discjson = data.map((disc, i) => {
            return {
                title: disc.name,
                author: disc.author,
                'disc-namespace': `music_disc.${disc.namespace}`,
                'model-data': i + 1,
                'creeper-drop': disc.creeperDrop,
                lores: disc.lores.split('\n'),
                'loot-tables': disc.lootTables.join(',').split(','),
            };
        });

        const jukelooper = await jukelooperIntegration(data, mono);

        zip.file('integrations.txt', jukelooper);

        zip.file('README.md', await (await fetch(README)).arrayBuffer());
        zip.file(`${name}.zip`, await content.arrayBuffer());
        zip.file('discs.json', JSON.stringify(discjson, null, 2));

        zip.generateAsync({ type: 'blob' }).then(async resources => {
            saveAs(resources, 'open me.zip');
        });
    });
};

const jukelooperIntegration = async (data: songData[], mono: boolean) : Promise<string> => {
    const map : { namespace: string, duration: number}[] = await Promise.all(data.map(async disc =>
        new Promise(resolve => {
            const ogg = mono ? disc.monoFile : disc.oggFile;

            const audio = document.createElement('audio');
            audio.src = URL.createObjectURL(ogg);
            // get audio source duration
            audio.onloadedmetadata = () => {
                URL.revokeObjectURL(audio.src);
                resolve({ namespace: disc.namespace, duration: Math.ceil(audio.duration) });
            };
        })
    ));

    const yaml = map.map(disc => `    music_disc.${disc.namespace}: ${disc.duration}`).join('\n');

    console.log(yaml);

    return `# JukeLooper integration\n## Copy the following lines in the config.yaml file of JukeLooper under the namespace node\n\n${yaml}`;
};