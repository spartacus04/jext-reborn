<script lang="ts">
	import { onMount } from 'svelte';

	onMount(() => {
		const { contentWindow } = document.querySelector('iframe');

		const watchLoad = () => {
			const interval = setInterval(() => {
				const elements = contentWindow.document.querySelectorAll('a');

				if(elements.length <= 0) return;

				clearInterval(interval);

				elements.forEach(a => {
					if(a.href.includes('github.com') || a.href.includes('kotlinlang.org')) {
						a.target = '_blank';
					}
					else {
						a.onclick = (e) => {
							e.preventDefault();
							contentWindow.location.replace(a.href);
						};
					}
				});
			}, 100);
		};

		contentWindow.addEventListener('load', watchLoad);

		contentWindow.onbeforeunload = () => {
			contentWindow.removeEventListener('load', watchLoad);
			watchLoad();
		};
	});
</script>

<iframe src="./docs/index.html" frameborder="0" title="docs" />

<style lang="scss">
	iframe {
		height: 100%;
		width: 100%;
	}
</style>