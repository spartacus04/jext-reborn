export const blobToArraBuffer = (blob: Blob): Promise<ArrayBuffer> => {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = () => resolve(reader.result as ArrayBuffer);
        reader.onerror = reject;
        reader.readAsArrayBuffer(blob);
    });
}

export const arrayBufferToBase64 = (buffer: ArrayBuffer) => {
    let binary = '';
    const bytes = new Uint8Array( buffer );
    bytes.forEach((byte) => binary += String.fromCharCode(byte));
    return window.btoa( binary );
}

export const base64ToArrayBuffer = (base64: string) => {
    const binary_string = atob(base64);
    const len = binary_string.length;
    const bytes = new Uint8Array(len);
    for (let i = 0; i < len; i++) {
        bytes[i] = binary_string.charCodeAt(i);
    }
    return bytes.buffer;
}

export const saveAs = (blob: Blob|undefined, filename: string) => {
	const link = document.createElement('a');
	link.href = URL.createObjectURL(blob!);
	link.download = filename;
	link.click();
};

export const getVersionFromTime = () => {
    const now = new Date();
    const oneDay = 1000 * 60 * 60 * 24;
    
    // day of year
    const startDay = new Date(now.getFullYear(), 0, 0);
    const diffDay = now.getTime() - startDay.getTime() + (startDay.getTimezoneOffset() - now.getTimezoneOffset()) * 60 * 1000;
    const day = Math.floor(diffDay / oneDay);

    // seconds of day
    const startSeconds = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0);
    const diffSeconds = now.getTime() - startSeconds.getTime();
    const seconds = Math.floor(diffSeconds / 1000);

    return [now.getFullYear(), day, seconds];
}