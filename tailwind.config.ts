import { join } from 'path'
import type { Config } from 'tailwindcss'
import forms from '@tailwindcss/forms';
import typography from '@tailwindcss/typography';
import { skeleton } from '@skeletonlabs/tw-plugin';
import { minecraft } from './src/minecraft'

export default {
	darkMode: 'class',
	content: ['./src/**/*.{html,js,svelte,ts}', join(require.resolve('@skeletonlabs/skeleton'), '../**/*.{html,js,svelte,ts}')],
	theme: {
		extend: {
			colors: {
				'mc-light-gray': '#d3d3d3',
				'mc-light-green': '#55ff55',
				'mc-aqua': '#55ffff'
			}
		},
		fontFamily: {
			'cascadia': 'Cascadia Code',
			'minecraft': 'Minecraft Regular',
			'minecraft-launcher': 'Minecraft Launcher',
		}
	},
	plugins: [
		forms,
		typography,
		skeleton({
			themes: {
				custom: [
					minecraft,
				],
			},
		}),
	],
} satisfies Config;
