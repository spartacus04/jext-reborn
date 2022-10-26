import { defineConfig } from 'vite';
import { ViteEjsPlugin } from 'vite-plugin-ejs';
import dungeons from './dungeons.json'

export default defineConfig({
	build: {
		outDir: 'docs',
	},
	base: '/jext-reborn/',
	plugins: [
		ViteEjsPlugin({
			dungeons
		}),
	],
});