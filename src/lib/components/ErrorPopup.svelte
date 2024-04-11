<script lang="ts">
	import { connectionless, warning } from '$lib/assets';

	import { Accordion, AccordionItem, CodeBlock } from '@skeletonlabs/skeleton';

	import { saveAs } from '$lib';

	export let error: Error;

	const isBrowser = !window.__TAURI__;

	const exportLogs = () => {
		const blob = new Blob(
			[
				JSON.stringify(
					console.logs,
					(_, value) => {
						if (value instanceof Error) {
							return {
								name: value.name,
								message: value.message,
								stack: value.stack,
								cause: value.cause
							};
						}

						return value;
					},
					4
				)
			],
			{ type: 'text/plain' }
		);

		saveAs(blob, 'jext-logs.txt');
	};

	const getDesktopAppDownload = (async () => {
		const response = await fetch(
			'https://api.github.com/repos/spartacus04/jext-reborn/actions/workflows/build-tauri.yml/runs?status=success&per_page=1'
		);

		const json = await response.json();

		if (json.total_count == 0) return;

		return json.workflow_runs[0].html_url;
	})();
</script>

<main class="bg-surface-500 rounded-md p-2 h-[50%] overflow-y-auto w-[90%] sm:w-[80%] lg:w-[50%]">
	<h2 class="h2">Error connecting to the JEXT server</h2>

	<p>There was an error connecting to the JEXT server. Please try again later.</p>

	{#if isBrowser}
		<div class="bg-orange-900 rounded-lg mt-2">
			<Accordion>
				<AccordionItem>
					<svelte:fragment slot="lead">
						<img src={connectionless} alt="warning" />
					</svelte:fragment>
					<svelte:fragment slot="summary">My requests keep failing</svelte:fragment>
					<svelte:fragment slot="content">
						<p>
							Due to the nature of HTTPS, some requests may fail. This is because the Jext server is
							running on HTTP and the browser is blocking the request.<br /><br />If you are using
							chrome you can enable unsafe content for the website. Just follow
							<a
								href="https://support.google.com/chrome/answer/114662"
								target="_blank"
								rel="noopener noreferrer"
								class="underline">this</a
							>
							guide and enable unsafe content.
							<br /><br />
							Alternatively, you can download the JEXT Desktop App and run it on your computer. This
							will allow you to connect to the server without any issues.
						</p>
						{#await getDesktopAppDownload}
							<p></p>
						{:then url}
							{#if url != null}
								(<a href={url} target="_blank" rel="noopener noreferrer" class="underline"
									>Download the desktop app here</a
								>)
							{/if}
						{/await}
					</svelte:fragment>
				</AccordionItem>
			</Accordion>
		</div>
	{/if}

	<CodeBlock
		class="border border-mc-light-gray rounded-lg m-4"
		language="javascript"
		rounded="true"
		code={error.stack}
	/>

	<div class="flex w-full justify-around">
		<button class="btn variant-filled-primary" on:click={exportLogs}>Export full logs file</button>
		<a
			class="btn variant-filled-primary"
			href="https://support.google.com/admanager/answer/10358597?hl=en"
			target="_blank"
			rel="noopener noreferrer">Export .har file</a
		>
	</div>
</main>
