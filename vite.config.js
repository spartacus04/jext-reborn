import { defineConfig } from 'vite';
import { ViteEjsPlugin } from 'vite-plugin-ejs';
import dungeons from './dungeons.json';
import autoprefixer from 'autoprefixer';
import { ViteMinifyPlugin } from 'vite-plugin-minify';

export default defineConfig({
	build: {
		outDir: 'docs',
	},
	base: '/jext-reborn/',

	plugins: [
		ViteEjsPlugin({
			dungeons,
		}),
		ViteMinifyPlugin({
			minifyJS: true,
			minifyCSS: true,
		}),
	],
	css: {
		postcss: {
			plugins: [
				autoprefixer,
			],
		},
	},
});