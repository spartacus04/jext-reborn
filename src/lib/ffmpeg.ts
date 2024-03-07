import type { FFmpegData } from "./types"
import { FFmpeg } from "@ffmpeg/ffmpeg";
import { arrayBufferToBase64, base64ToArrayBuffer, blobToArraBuffer } from "./utils";
import { invoke } from '@tauri-apps/api/tauri'
import { get, writable } from "svelte/store";

const qualityArgs = {
    none: [],
    low: ['-ar', '22050', '-qscale:a', '9' ],
    medium: ['-ar', '44100', '-qscale:a', '1' ],
    high: ['-ar', '48000', '-qscale:a', '7' ]
}

export const localFFmpegStore = writable(false);

export const prepareAudio = async (blob: Blob, data: FFmpegData, onProgress?: (percent: number) => unknown) : Promise<Blob|null> => {

    const args = ['-vn', '-acodec', 'libvorbis'];

    if (data.mono) args.push('-ac', '1');

    if (data.normalize) args.push('-af', 'loudnorm');

    args.push(...qualityArgs[data.quality]);

    if (window.__TAURI__ && get(localFFmpegStore)) {
        const result = <string>(await invoke('ffmpeg', {
            input: arrayBufferToBase64(await blob.arrayBuffer()),
            args
        }))

        if(result === "") return null;

        return new Blob([base64ToArrayBuffer(result)], { type: 'audio/ogg' });
    } else {
        console.log("test2");

        const ffmpeg = new FFmpeg();

        if(!ffmpeg.loaded) await ffmpeg.load({
            coreURL: 'node'
        });
    
        await ffmpeg.writeFile('input.ogg', new Uint8Array(await blobToArraBuffer(blob)));
    
        const randomName = Math.random().toString(36).substring(10);

        ffmpeg.on('log', (message) => console.log(message));
    
        ffmpeg.on('progress', (event) => onProgress?.(event.progress * 100));
        
        await ffmpeg.exec([ '-i', `${randomName}.ogg`, ...args, `out-${randomName}.ogg` ]);
    
        const result = await ffmpeg.readFile(`out-${randomName}.ogg`);
    
        ffmpeg.deleteFile(`${randomName}.ogg`);
        ffmpeg.deleteFile(`out-${randomName}.ogg`);
    
        return new Blob([result], { type: 'audio/ogg' });

        return new Blob([]);
    }
}