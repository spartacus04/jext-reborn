<script lang="ts">
	import { base } from '$app/paths';
	import { Searchbar, DocBubble } from '$lib/components/docs';
	import { pages, type Page } from '$lib/constants';

	let query = '';

	const categories = pages
		.map((page) => page.category)
		.filter((value, index, self) => self.indexOf(value) === index);

	const filter = (page: Page) => {
		if (query.trim() === '') return true;
		return (
			page.title.toLowerCase().includes(query.toLowerCase()) ||
			page.description.toLowerCase().includes(query.toLowerCase())
		);
	};
</script>

<div class="flex flex-col w-full h-full">
	<div class="flex flex-col bg-[#191919] w-full py-4 px-8">
		<b class="text-[#aeaeae] text-xs">SEARCH</b>
		<Searchbar bind:query />
	</div>
	<div class="flex flex-col flex-grow bg-[#131313] px-8 md:px-28 overflow-y-auto pb-8">
		{#each categories as category}
			{#key query}
				{#if pages.filter((page) => page.category === category).filter(filter).length > 0}
					<h2 class="text-white text-lg px-8 py-4">{category}</h2>

					<div class="grid lg:grid-cols-3 gap-8">
						{#each pages.filter((page) => page.category === category).filter(filter) as page}
							<DocBubble title={page.title} description={page.description} url="{base}{page.url}" />
						{/each}
					</div>
				{/if}
			{/key}
		{/each}

		{#key query}
			{#if pages.filter(filter).length === 0}
				<p class="text-white text-center mt-8">No results found.</p>
			{/if}
		{/key}
	</div>
</div>
