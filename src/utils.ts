import { createFFmpeg } from '@ffmpeg/ffmpeg';

const ffmpeg = createFFmpeg({
    corePath: '../ffmpeg-core/ffmpeg-core.js',
    mainName: 'main',
});

export const convertToOgg = async (file: File) : Promise<Blob> => {
    if(file.type == 'audio/ogg' || import.meta.env.DEV) {
        return new Blob([file], { type: 'audio/ogg' });
    }

    return await new Promise((resolve) => {
        const reader = new FileReader();
        const blob = new Blob([file]);

        reader.readAsArrayBuffer(blob);

        reader.onload = async () => {
            const arrayBuffer = <ArrayBuffer>reader.result;
            await ffmpeg.load();
            ffmpeg.FS('writeFile', 'audio', new Uint8Array(arrayBuffer));
            await ffmpeg.run('-i', 'audio', '-acodec', 'libvorbis', '/output.ogg');

            const output = ffmpeg.FS('readFile', '/output.ogg');
            ffmpeg.exit();

            resolve(new Blob([output], { type: 'audio/ogg' }));
        };
    });
};

export const stereoToMono = async (blob: Blob) : Promise<Blob> => {
    if(import.meta.env.DEV) return blob;

    return await new Promise((resolve) => {
        const reader = new FileReader();

        reader.readAsArrayBuffer(blob);

        reader.onload = async () => {
            const arrayBuffer = <ArrayBuffer>reader.result;
            await ffmpeg.load();
            ffmpeg.FS('writeFile', 'audio', new Uint8Array(arrayBuffer));
            await ffmpeg.run('-i', 'audio', '-ac', '1', '/output.ogg');

            const output = ffmpeg.FS('readFile', '/output.ogg');
            ffmpeg.exit();

            resolve(new Blob([output], { type: 'audio/ogg' }));
        };
    });
};

export const resizeImageBlob = async (blob: Blob, width: number, height: number) : Promise<Blob> => {
    return await new Promise((resolve) => {
        const image = new Image();
        image.src = URL.createObjectURL(blob);

        image.onload = () => {
            const canvas = document.createElement('canvas');
            canvas.width = width;
            canvas.height = height;

            const ctx = canvas.getContext('2d');

			ctx!.drawImage(image, 0, 0, width, height);

			const dataURL = canvas.toDataURL('image/png');

			resolve(dataURLToBlob(dataURL));
        };
    });
};

const dataURLToBlob = (dataURL: string): Blob | PromiseLike<Blob> => {
    return new Promise((resolve) => {
        const arrayBuffer = dataURLToArrayBuffer(dataURL);
        resolve(new Blob([arrayBuffer], { type: 'image/png' }));
    });
};

const dataURLToArrayBuffer = (dataURL: string) : ArrayBuffer => {
    const base64 = dataURL.split(',')[1];
    const binary = window.atob(base64);
    const arrayBuffer = new ArrayBuffer(binary.length);
    const uint8Array = new Uint8Array(arrayBuffer);

    for (let i = 0; i < binary.length; i++) {
        uint8Array[i] = binary.charCodeAt(i);
    }

    return arrayBuffer;
};

export const saveAs = (blob: Blob, filename: string) => {
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = filename;
    link.click();
};
