import { defineConfig } from 'vite';
import { svelte } from '@sveltejs/vite-plugin-svelte';
import autoprefixer from 'autoprefixer';
import path from 'node:path';

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

	resolve: {
		alias: [
			{ find: '@', replacement: path.resolve(__dirname, './src') },
			{ find: '@assets', replacement: path.resolve(__dirname, './src/assets') },
			{ find: '@styles', replacement: path.resolve(__dirname, './src/styles') },
			{ find: '@lib', replacement: path.resolve(__dirname, './src/lib') },
			{ find: '@ui', replacement: path.resolve(__dirname, './src/ui') }
		]
	}
});