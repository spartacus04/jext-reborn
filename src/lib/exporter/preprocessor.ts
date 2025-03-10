import { discsStore, isMusicDisc } from '$lib/discs/discManager';
import { get } from 'svelte/store';
import { removeStep, updateSteps } from './exporterLine';
import { convertAudio } from './ffmpeg';
import type { MusicDisc } from '$lib/discs/musicDisc';
import type { FFmpeg } from '@ffmpeg/ffmpeg';

export const preProcessDiscs = async (ffmpeg: FFmpeg | null) => {
	const discs = get(discsStore);

	for (let i = 0; i < discs.length; i++) {
		updateSteps(1, `Processing ${discs[i].namespace}`, i, discs.length);
		const usedModelDatas = discs.map((d) => d.modelData).filter((d) => d !== -1);

		discs[i].modelData = assignModelData(usedModelDatas);

		discs[i].setDiscTexture(await processImage(discs[i].discTexture));
		discs[i].setFragmentTexture(await processImage(discs[i].fragmentTexture));

		if (isMusicDisc(discs[i])) {
			if (!(discs[i] as MusicDisc).cachedFinalAudioFile) {
				if((discs[i] as MusicDisc).disableTranscoding || localStorage.getItem('disable-audio-transcoding') === 'true') {
					(discs[i] as MusicDisc).cachedFinalAudioFile = (discs[i] as MusicDisc).audioFile;
				} else {
					const track = await convertAudio(discs[i] as MusicDisc, ffmpeg);
					(discs[i] as MusicDisc).cachedFinalAudioFile = track;
				}
			}
		}

		removeStep(1);
	}

	discsStore.set(discs);
};

const assignModelData = (modelDatas: number[]) => {
	let modelData = 1;

	while (modelDatas.includes(modelData)) {
		modelData++;
	}

	return modelData;
};

const processImage = async (blob: Blob) => {
	return await new Promise<Blob>((resolve) => {
		const img = new Image();
		img.src = URL.createObjectURL(blob);

		const canvas = document.createElement('canvas');
		const ctx = canvas.getContext('2d')!;

		img.onload = () => {
			const size = Math.min(img.width, img.height);

			canvas.width = size;
			canvas.height = size;

			ctx.drawImage(img, 0, 0, size, size, 0, 0, size, size);

			canvas.toBlob((blob) => {
				resolve(blob!);
			}, 'image/png');
		};
	});
};
