<script lang="ts">
	import { base } from '$app/paths';
	import { Searchbar, DocBubble } from '$lib/components/docs';

	let query = '';

	const pages = [
		{
			title: 'Quick Start',
			description: 'Get started with JukeboxExtendedReborn in a few simple steps.',
			url: '/docs/quick-start',
			category: 'For Server Owners'
		},
		{
			title: 'Configuration',
			description: 'Learn how to configure Jext Reborn to your liking.',
			url: '/docs/configuration',
			category: 'For Server Owners'
		},
		{
			title: 'Commands & permissions',
			description: 'A list of all commands and permissions available in Jext Reborn.',
			url: '/docs/commands-permissions',
			category: 'For Server Owners'
		},
		{
			title: 'Disc manager tips & tricks',
			description: 'Learn how to use the disc manager to its full potential.',
			url: '/docs/disc-manager-tips',
			category: 'For Server Owners'
		},
		{
			title: 'Desktop app & remote control',
			description: 'Control both the resource pack and the plugin from the desktop app.',
			url: '/docs/desktop-app',
			category: 'For Server Owners'
		},
		{
			title: 'Bulk import',
			description: 'Import a large amount of music at once.',
			url: '/docs/bulk-import',
			category: 'For Server Owners'
		},
		{
			title: 'Manual resource pack building',
			description: 'Manually set up the music resource pack.',
			url: '/docs/manual-setup',
			category: 'For Developers'
		},
		{
			title: 'Permission integrations',
			description: 'Integrate Jext Reborn with your permission plugins.',
			url: '/docs/permission-integrations',
			category: 'For Developers'
		},
		{
			title: 'Custom disc sources',
			description: 'Add custom disc sources to Jext Reborn.',
			url: '/docs/custom-disc-sources',
			category: 'For Developers'
		},
		{
			title: 'REST API',
			description: 'Use the Jext Reborn REST API to interact with the plugin.',
			url: '/docs/api',
			category: 'For Developers'
		},
		{
			title: 'Stable javadocs',
			description: 'View the stable Jext Reborn javadocs.',
			url: '/docs/stable-javadocs',
			category: 'For Developers'
		},
		{
			title: 'Development javadocs',
			description: 'View the development Jext Reborn javadocs.',
			url: '/docs/development-javadocs',
			category: 'For Developers'
		}
	] satisfies Page[];

	interface Page {
		title: string;
		description: string;
		url: string;
		category: string;
	}

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

<svelte:head>
	<title>JEXT | Documentation</title>
	<meta name="description" content="Documentation for JukeboxExtendedReborn." />
</svelte:head>

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
