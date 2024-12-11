import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';

export default defineConfig({
	plugins: [sveltekit()],
	build: {
		sourcemap: true
	},
	base: '/jext-reborn/',
	optimizeDeps: {
		exclude: ['@ffmpeg/ffmpeg', '@ffmpeg/util', '@ffmpeg/core']
	}
});
