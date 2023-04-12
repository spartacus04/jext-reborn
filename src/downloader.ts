export const downloadSong = async (link: string): Promise<File> => {
	if (link.match(/^(http(s)?:\/\/)?((w){3}.)?youtu(be|.be)?(\.com)?\/.+/)) {
		return await youtubeDownloader(link);
	}

	return null;
};

const youtubeDownloader = async (link: string): Promise<File> => {
	const { audioUrl, name } = await getAudioUrl(link);

	return (await fetch(audioUrl)).blob().then((blob) => new File([blob], name, { type: 'audio/ogg' }));
};

const getAudioUrl = async (link: string): Promise<{ audioUrl: string, name: string }> => {

	// video url
	const id = link
		.replace(/(>|<)/gi, '')
		.split(/(vi\/|v=|\/v\/|youtu\.be\/|\/embed\/)/)[2]
		.split(/[^0-9a-z_-]/i)[0];

	const queryParams = new URLSearchParams({
		videoId: id,
		reason: 'AGE_VERIFICATION_REQUIRED',
	}).toString();

	const proxyUrl = `https://youtube-proxy.zerody.one/getPlayer?${queryParams}`;

	const data = <any>await (await fetch(proxyUrl)).json();


	const audios: any[] = data.streamingData.adaptiveFormats.filter(
		(e: any) => e.mimeType.startsWith('audio') && e.mimeType.includes('opus'),
	);

	const audioUrl = audios[0].url;

	// video name
	const name = data.videoDetails.title;

	return {
		audioUrl,
		name,
	};
};