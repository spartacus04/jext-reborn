import Worker from '@/worker?worker';

export const encodeDisc = async (blob: Blob, params = { 'normalize': false, 'mono': true, 'preset': 'none' }) : Promise<Blob> => {
	if(import.meta.env.DEV) return blob;

	const args: Array<string|number> = ['-vn', '-acodec', 'libvorbis'];
	const qPresets: {[id: string]: Array<string>} = {};

	/* hq uses settings similar to those used to encode otherside,
	* pigstep, and relic: sample rate of 48kHz, quality preset 7
	* (VBR between 224kbits/s and 256kbits/s)
	*/
	qPresets['hq'] = ['-ar', '48000', '-q', '7' ];
	/* mq uses settings similar to those used to encode the original 13
	* music discs: sample rate of 48kHz, quality preset 7 (VBR between
	* 224kbits/s and 256kbits/s)
	*/
	qPresets['mq'] = ['-ar', '44100', '-q', '1' ];

	if (params['normalize']) {
		args.push('-af');
		args.push('loudnorm');
	}

	if (params['mono']) {
		args.push('-ac');
		args.push('1');
	}

	if (params['preset'] !== 'none') {
		qPresets[params['preset']].forEach((a: string) => {
			args.push(a);
		});
	}

	return await new Promise((resolve) => {
		const reader = new FileReader();

		reader.readAsArrayBuffer(blob);

		reader.onload = async () => {
			const arrayBuffer = <ArrayBuffer>reader.result;

			const worker = new Worker();

			worker.onmessage = (e) => {
				resolve(new Blob([e.data], { type: 'audio/ogg' }));
				worker.terminate();
			};

			worker.postMessage({
				audio: arrayBuffer,
				args: args,
			});
		};
	});
};

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

			const worker = new Worker();

			worker.onmessage = (e) => {
				resolve(new Blob([e.data], { type: 'audio/ogg' }));
				worker.terminate();
			};

			worker.postMessage({
				audio: arrayBuffer,
				args: ['-vn', '-acodec', 'libvorbis'],
			});
		};
	});
};
