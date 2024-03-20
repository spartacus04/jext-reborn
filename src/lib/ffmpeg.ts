import type { FFmpegData } from "./types"
import { FFmpeg } from "@ffmpeg/ffmpeg";
import { arrayBufferToBase64, base64ToArrayBuffer, blobToArraBuffer } from "./utils";
import { invoke } from '@tauri-apps/api/tauri'
import { get, writable } from "svelte/store";
import { listen } from "@tauri-apps/api/event";
import { getDuration } from "./resourcepack/utils";

const baseURL = 'https://unpkg.com/@ffmpeg/core@0.12.6/dist/esm';
const baseMTURL = 'https://unpkg.com/@ffmpeg/core-mt@0.12.6/dist/esm';

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
        const duration = await getDuration(blob);

        const unregister = await listen<string>('ffmpeg-progress', async event => {
            const [hours, minutes, seconds] = event.payload.split(':').map(Number);

            onProgress?.((hours * 3600 + minutes * 60 + seconds) / duration * 100);
        })

        const result = <string>(await invoke('ffmpeg', {
            input: arrayBufferToBase64(await blob.arrayBuffer()),
            args
        }))

        unregister();

        if(result === "") return null;

        return new Blob([base64ToArrayBuffer(result)], { type: 'audio/ogg' });
    } else {
        const ffmpeg = new FFmpeg();
        
        if(!ffmpeg.loaded) await ffmpeg.load(crossOriginIsolated ? {
            coreURL: `${baseMTURL}/ffmpeg-core.js`,
            wasmURL: `${baseMTURL}/ffmpeg-core.wasm`,
            workerURL: `${baseMTURL}/ffmpeg-core.worker.js`
        } : {
            coreURL: `${baseURL}/ffmpeg-core.js`,
            wasmURL: `${baseURL}/ffmpeg-core.wasm`,
            workerURL: `${baseURL}/ffmpeg-core.worker.js`
        });
        
        await ffmpeg.writeFile('input.ogg', new Uint8Array(await blobToArraBuffer(blob)));
        
        ffmpeg.on('progress', (event) => onProgress?.(event.progress * 100));
        
        await ffmpeg.exec([ '-i', `input.ogg`, ...args, `output.ogg` ]);
    
        const result = await ffmpeg.readFile(`output.ogg`);
    
        ffmpeg.deleteFile(`input.ogg`);
        ffmpeg.deleteFile(`output.ogg`);
    
        return new Blob([result], { type: 'audio/ogg' });
    }
}