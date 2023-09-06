<script lang="ts">
	import { onMount } from 'svelte';

	onMount(() => {
		const { contentWindow } = document.querySelector('iframe');

		contentWindow.addEventListener('click', e => {
			const target = e.target as HTMLAnchorElement;
			const parent = target.parentElement as HTMLAnchorElement;

			if (target.tagName === 'A' && target.href.includes('github.com')) {
				e.preventDefault();
				window.open(target.href, '_blank');
			}
			else if(parent && parent.tagName === 'A' && parent.href.includes('github.com')) {
				e.preventDefault();
				window.open(parent.href, '_blank');
			}
		});
	});
</script>

<iframe src="./docs/index.html" frameborder="0" title="docs" />

<style lang="scss">
	iframe {
		height: 100%;
		width: 100%;
	}
</style>