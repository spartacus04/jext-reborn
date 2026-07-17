import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';
import path from 'path';

export default defineConfig({
	plugins: [sveltekit()],
	build: {
		sourcemap: true
	},
	base: '/jext-reborn/',
	optimizeDeps: {
		exclude: [
			'@ffmpeg/ffmpeg',
			'@ffmpeg/util',
			'@ffmpeg/core',
			'svelte',
			'svelte/internal',
			'svelte/store',
			'svelte/transition',
			'svelte/animate',
			'svelte/motion',
			'svelte/easing'
		]
	},
	resolve: {
		alias: [
			{ find: /^svelte$/, replacement: path.resolve(__dirname, 'node_modules/svelte/src/runtime/index.js') }
		]
	}
});
