declare let createFFmpegCore;

// FFMPEG class - which was former createFFmpeg in ffmpeg/ffmpeg

export class FFmpeg {
	private core = null;
	private ffmpeg = null;
	private runResolve = null;
	private running = false;
	private settings;
	private duration = 0;
	private ratio = 0;
	constructor(settings) {
		this.settings = settings;
	}

	async load() {
		this.log('info', 'load ffmpeg-core');
		if (this.core === null) {
			this.log('info', 'loading ffmpeg-core');

			/*
       * In node environment, all paths are undefined as there
       * is no need to set them.
       */
			const res = await this.getCreateFFmpegCore(this.settings);
			this.core = await res.createFFmpegCore({
				/*
         * Assign mainScriptUrlOrBlob fixes chrome extension web worker issue
         * as there is no document.currentScript in the context of content_scripts
         */
				mainScriptUrlOrBlob: res.corePath,
				printErr: (message) => this.parseMessage({ type: 'fferr', message }),
				print: (message) => this.parseMessage({ type: 'ffout', message }),
				/*
         * locateFile overrides paths of files that is loaded by main script (ffmpeg-core.js).
         * It is critical for browser environment and we override both wasm and worker paths
         * as we are using blob URL instead of original URL to avoid cross origin issues.
         */
				locateFile: (path, prefix) => {
					if (typeof res.wasmPath !== 'undefined'
            && path.endsWith('ffmpeg-core.wasm')) {
						return res.wasmPath;
					}
					if (typeof res.workerPath !== 'undefined'
            && path.endsWith('ffmpeg-core.worker.js')) {
						return res.workerPath;
					}
					return prefix + path;
				},
			});
			this.ffmpeg = this.core.cwrap('main', 'number', ['number', 'number']);
			this.log('info', 'ffmpeg-core loaded');

		}
		else {
			throw Error('ffmpeg.wasm was loaded, you should not load it again, use ffmpeg.isLoaded() to check next time.');
		}
	}


	public writeFile(fileName: string, buffer: Uint8Array) {
		if (this.core === null) {
			throw NO_LOAD;
		}
		else {
			let ret = null;
			try {
				ret = this.core.FS.writeFile(...[fileName, buffer]);
			}
			catch (e) {
				throw Error('Oops, something went wrong in FS operation.');
			}
			return ret;
		}
	}
	public readFile(fsFileName: string) {
		if (this.core === null) {
			throw NO_LOAD;
		}
		else {
			let ret = null;
			try {
				ret = this.core.FS.readFile(...[fsFileName]);
			}
			catch (e) {
				throw Error(`ffmpeg.FS('readFile', '${fsFileName}') error. Check if the path exists`);
			}
			return ret;
		}
	}

	public unlink(fsFileName: string) {
		if (this.core === null) {
			throw NO_LOAD;
		}
		else {
			let ret = null;
			try {
				ret = this.core.FS.unlink(...[fsFileName]);
			}
			catch (e) {
				throw Error(`ffmpeg.FS('unlink', '${fsFileName}') error. Check if the path exists`);
			}
			return ret;
		}
	}

	async run(..._args) {
		this.log('info', `run ffmpeg command: ${_args.join(' ')}`);
		if (this.core === null) {
			throw NO_LOAD;
		}
		else if (this.running) {
			throw Error('ffmpeg.wasm can only run one command at a time');
		}
		else {
			this.running = true;
			return new Promise((resolve) => {
				const args = [...defaultArgs, ..._args].filter((s) => s.length !== 0);
				this.runResolve = resolve;
				this.ffmpeg(...FFmpeg.parseArgs(this.core, args));
			});
		}

	}

	exit() {
		if (this.core === null) {
			throw NO_LOAD;
		}
		else {
			this.running = false;
			this.core.exit(1);
			this.core = null;
			this.ffmpeg = null;
			this.runResolve = null;
		}
	}


	get isLoaded(): boolean {
		return this.core !== null;
	}

	private parseMessage({ type, message }) {
		this.log(type, message);
		this.parseProgress(message, this.settings.progress);
		this.detectCompletion(message);
	}

	private detectCompletion(message) {
		if (message === 'FFMPEG_END' && this.runResolve !== null) {
			this.runResolve();
			this.runResolve = null;
			this.running = false;
		}
	}


	private static parseArgs(Core, args) {
		const argsPtr = Core._malloc(args.length * Uint32Array.BYTES_PER_ELEMENT);
		args.forEach((s, idx) => {
			const buf = Core._malloc(s.length + 1);
			Core.writeAsciiToMemory(s, buf);
			Core.setValue(argsPtr + (Uint32Array.BYTES_PER_ELEMENT * idx), buf, 'i32');
		});
		return [args.length, argsPtr];
	}


	private ts2sec(ts) {
		const [h, m, s] = ts.split(':');
		return (parseFloat(h) * 60 * 60) + (parseFloat(m) * 60) + parseFloat(s);
	}

	private parseProgress(message, progress) {
		if (typeof message === 'string') {

			if (message.startsWith('  Duration')) {
				const ts = message.split(', ')[0].split(': ')[1];
				const d = this.ts2sec(ts);
				progress({ duration: d, ratio: this.ratio });
				if (this.duration === 0 || this.duration > d) {
					this.duration = d;
				}
			}
			else if (message.startsWith('frame') || message.startsWith('size')) {
				const ts = message.split('time=')[1].split(' ')[0];
				const t = this.ts2sec(ts);
				this.ratio = t / this.duration;
				progress({ ratio: this.ratio, time: t });
			}
			else if (message.startsWith('video:')) {
				progress({ ratio: 1 });
				this.duration = 0;
			}
		}
	}

	private log(type, message) {
		if (this.settings.logger) {this.settings.logger({ type, message });}

		if (this.settings.log) {console.log(type, message);}
	}

	async toBlobURL(url, mimeType) {
		this.log('info', `fetch ${url}`);
		const buf = await (await fetch(url)).arrayBuffer();
		this.log('info', `${url} file size = ${buf.byteLength} bytes`);
		const blob = new Blob([buf], { type: mimeType });
		const blobURL = URL.createObjectURL(blob);
		this.log('info', `${url} blob URL = ${blobURL}`);
		return blobURL;
	}

	async getCreateFFmpegCore({ corePath: _corePath }): Promise<{
    createFFmpegCore: any,
    corePath: string,
    wasmPath: string,
    workerPath: string,
  }> {
		if (typeof _corePath !== 'string') {
			throw Error('corePath should be a string!');
		}

		// const coreRemotePath = self.location.host +_corePath
		const coreRemotePath = self.location.origin + _corePath;
		const corePath = await this.toBlobURL(
			coreRemotePath,
			'application/javascript',
		);
		const wasmPath = await this.toBlobURL(
			coreRemotePath.replace('ffmpeg-core.js', 'ffmpeg-core.wasm'),
			'application/wasm',
		);
		const workerPath = await this.toBlobURL(
			coreRemotePath.replace('ffmpeg-core.js', 'ffmpeg-core.worker.js'),
			'application/javascript',
		);

		if (typeof createFFmpegCore === 'undefined') {
			return new Promise((resolve) => {
				globalThis.importScripts(corePath);
				if (typeof createFFmpegCore === 'undefined') {
					throw Error('CREATE_FFMPEG_CORE_IS_NOT_DEFINED');
				}
				this.log('info', 'ffmpeg-core.js script loaded');
				resolve({
					createFFmpegCore,
					corePath,
					wasmPath,
					workerPath,
				});
			});
		}
		this.log('info', 'ffmpeg-core.js script is loaded already');
		return Promise.resolve({
			createFFmpegCore,
			corePath,
			wasmPath,
			workerPath,
		});
	}
}


const NO_LOAD = Error('ffmpeg.wasm is not ready, make sure you have completed load().');
const defaultArgs = [
	/* args[0] is always the binary path */
	'./ffmpeg',
	/* Disable interaction mode */
	'-nostdin',
	/* Force to override output file */
	'-y',
];


const audio_processes : string[] = [];

const ffmpeg = new FFmpeg({
	corePath: '/jext-reborn/ffmpeg-core/ffmpeg-core.js',
});


onmessage = async (e) => {
	const { audio, args } : { audio: ArrayBuffer, args: string[] } = e.data;

	if(!ffmpeg.isLoaded) await ffmpeg.load();

	const audio_process = crypto.randomUUID();
	audio_processes.push(audio_process);

	ffmpeg.writeFile(audio_process, new Uint8Array(audio));

	await ffmpeg.run('-i', audio_process, ...args, `/${audio_process}.ogg`);

	const output = ffmpeg.readFile(`/${audio_process}.ogg`);

	ffmpeg.unlink(`/${audio_process}.ogg`);
	ffmpeg.unlink(`${audio_process}`);

	audio_processes.splice(audio_processes.indexOf(audio_process), 1);

	postMessage(output);
};