import adapter from '@sveltejs/adapter-static';
import { vitePreprocess } from '@sveltejs/vite-plugin-svelte';

/** @type {import('@sveltejs/kit').Config} */
const config = {
	extensions: ['.svelte'],
	preprocess: [vitePreprocess()],
	kit: {
		adapter: adapter(),
		paths: {
			base: process.env.TAURI_PLATFORM == undefined ? '/jext-reborn' : ''
		}
	}
};

export default config;
