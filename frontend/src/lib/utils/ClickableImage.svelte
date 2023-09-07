<script lang="ts">
	import { inputFile } from '@/ui/inputfile';
	import { spinner } from '@/assets';
	import { onDestroy } from 'svelte';

	export let imageUrl: string = null;
	export let image: Blob = null;
	export let processor: (img: Blob) => Promise<Blob> = null;

	let url : string;

	if(imageUrl && !image) {
		url = spinner;
	}
	else {
		url = URL.createObjectURL(image);
	}

	const setImage = async (files : File[]) => {
		if(processor) {
			image = await processor(files[0]);
		}
		else {
			image = files[0];
		}

		URL.revokeObjectURL(url);
		url = URL.createObjectURL(image);
	};

	const fetchImage = async () => {
		const response = await fetch(imageUrl);
		const blob = await response.blob();

		image = blob;

		URL.revokeObjectURL(url);
		url = URL.createObjectURL(image);
	};

	onDestroy(() => URL.revokeObjectURL(url));
</script>

{#if !image}
	{#await fetchImage()}
		<img src={spinner} alt="Loading..." />
	{/await}
{:else}
	<img src={url} alt="Clickable" use:inputFile={{
		accept: 'image/*',
		cb: setImage,
		multiple: false,
		drag: true,
	}}>
{/if}

<style lang="scss">
	img {
		cursor: pointer;
		border: 1px solid transparent;
		aspect-ratio: 1;
		height: 64px;

		&:hover {
			border: 1px solid white;
		}
	}
</style>