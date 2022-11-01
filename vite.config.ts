import { defineConfig } from 'vite';
import { svelte } from '@sveltejs/vite-plugin-svelte';
import autoprefixer from 'autoprefixer';

export default defineConfig({
	build: {
		outDir: 'docs',
	},
	base: '/jext-reborn/',

	plugins: [svelte()],
	css: {
		postcss: {
			plugins: [
				autoprefixer,
			],
		},
	},
});
