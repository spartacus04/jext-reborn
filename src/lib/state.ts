import '@tauri-apps/api';

export const isTauri = window.__TAURI__ !== undefined;

export const os = (() => {
	if (navigator.userAgent.includes('Android')) {
		return 'android';
	} else if (
		navigator.userAgent.includes('iPad') ||
		navigator.userAgent.includes('iPhone') ||
		navigator.userAgent.includes('iPod')
	) {
		return 'ios';
	} else if (navigator.userAgent.includes('Windows')) {
		return 'windows';
	} else if (navigator.userAgent.includes('Linux')) {
		return 'linux';
	} else if (navigator.userAgent.includes('Mac')) {
		return 'macos';
	}

	return 'unknown';
})();
