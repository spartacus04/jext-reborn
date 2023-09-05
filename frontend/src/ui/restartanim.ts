export const restartAnim = (e: HTMLImageElement, src: string) => {
	e.src = 'data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==';
	setTimeout(() => {
		e.src = src;
	}, 1);
};