/** @type {import('tailwindcss').Config} */
export default {
	content: ['./src/**/*.{html,js,svelte,ts}'],
	theme: {
		extend: {
			screens: {
				'xs': '380px',
			},
			colors: {
				surface: '#303030',
				'surface-background': '#1E1E1E',
				'surface-background-variant': '#353535',
				'surface-separator': '#393939'
			},
			fontFamily: {
				cascadia: 'Cascadia Code',
				minecraft: 'Minecraft Regular',
				'minecraft-launcher': 'Minecraft Launcher',
				'minecraft-title': 'Minecraft Title'
			}
		}
	},
	plugins: []
};
