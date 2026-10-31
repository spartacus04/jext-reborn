<script lang="ts">
	import { onMount } from 'svelte';
	import { default_icon, linux, macos, windows } from '$lib/assets';
	import { os } from '$lib/state';
	import LauncherButton from '../buttons/LauncherButton.svelte';

	type Platform = 'windows' | 'linux' | 'macos' | 'unknown';

	type Artifact = {
		id: number;
		name: string;
		expired: boolean;
	};

	let dialog: HTMLDialogElement;
	let artifacts: Artifact[] = [];
	let runId: number | undefined;
	let loading = true;
	let error = '';
	const platforms: Platform[] = ['windows', 'linux', 'macos', 'unknown'];

	export const open = () => dialog.show();
	export const close = () => dialog.close();
	export const isOpen = () => dialog.open;
	export const toggle = () => (dialog.open ? close() : open());
	export const openModal = () => dialog.showModal();

	const onClick = (e: MouseEvent) => {
		const id = (e.target as HTMLElement).id;

		if (id === 'backdrop') close();
		else e.stopPropagation();
	};

	const getPlatform = (name: string): Platform => {
		if (name.endsWith('.msi') || name.endsWith('.exe')) return 'windows';
		if (name.endsWith('.dmg')) return 'macos';
		if (name.endsWith('.deb') || name.endsWith('.rpm') || name.endsWith('.AppImage')) {
			return 'linux';
		}
		return 'unknown';
	};

	const platformLabel = (platform: Platform) =>
		platform === 'macos' ? 'macOS' : platform[0].toUpperCase() + platform.slice(1);

	const platformIcon = (platform: Platform) => {
		switch (platform) {
			case 'windows':
				return windows;
			case 'linux':
				return linux;
			case 'macos':
				return macos;
			default:
				return undefined;
		}
	};

	const getExtension = (name: string) => {
		const extension = name.split('.').pop();
		return extension ? `.${extension}` : '';
	};

	const platformOrder = (platform: Platform) => {
		const currentPlatform = os === 'android' || os === 'ios' ? 'unknown' : os;
		const order = [currentPlatform, 'windows', 'linux', 'macos', 'unknown'];
		return order.indexOf(platform);
	};

	$: orderedPlatforms = [...platforms].sort((a, b) => platformOrder(a) - platformOrder(b));

	onMount(async () => {
		try {
			const runsResponse = await fetch(
				'https://api.github.com/repos/spartacus04/jext-reborn/actions/workflows/build-tauri.yml/runs?status=completed&per_page=10'
			);
			if (!runsResponse.ok) throw new Error(`GitHub returned status ${runsResponse.status}`);

			const runs = (await runsResponse.json()) as {
				workflow_runs: { id: number; status: string; conclusion: string | null }[];
			};
			const latestRun = runs.workflow_runs.find(
				(run) => run.status === 'completed' && run.conclusion === 'success'
			);
			if (!latestRun) throw new Error('No successful desktop-app build was found.');
			runId = latestRun.id;

			const artifactsResponse = await fetch(
				`https://api.github.com/repos/spartacus04/jext-reborn/actions/runs/${latestRun.id}/artifacts`
			);
			if (!artifactsResponse.ok) {
				throw new Error(`GitHub returned status ${artifactsResponse.status}`);
			}

			const artifactList = (await artifactsResponse.json()) as { artifacts: Artifact[] };
			artifacts = artifactList.artifacts
				.filter((artifact) => !artifact.expired)
				.sort((a, b) => {
					const platformDifference =
						platformOrder(getPlatform(a.name)) - platformOrder(getPlatform(b.name));
					return platformDifference || a.name.localeCompare(b.name);
				});
		} catch (reason) {
			error = reason instanceof Error ? reason.message : 'Could not load downloads.';
		} finally {
			loading = false;
		}
	});
</script>

<!-- svelte-ignore a11y-no-noninteractive-element-interactions a11y-click-events-have-key-events -->
<dialog
	bind:this={dialog}
	id="backdrop"
	on:click={onClick}
	class="bg-surface-background md:bg-transparent p-0 backdrop:opacity-55 backdrop:bg-surface-background"
>
	<div
		class="m-0 md:m-auto md:w-[32rem] h-fit rounded-md bg-surface-background animate-slide-down flex flex-col items-center justify-center md:block"
	>
		<div class="w-full rounded-t-md bg-[#262523] flex items-center justify-center">
			<img src={default_icon} alt="icon" class="w-20 m-4" />
		</div>
		<div class="p-4 font-minecraft text-white">
			<h1 class="font-minecraft-title text-2xl">Download the desktop app</h1>
			<p>By downloading the desktop app you get access to some exclusive features such as:</p>
			<ul class="list-disc ml-4 mb-2">
				<li>Faster generation speed</li>
				<li>Direct download from Youtube/Soundcloud</li>
				<li>Remote disc/configuration management</li>
				<li>Bulk importing</li>
				<li>Community disc packs</li>
				<li>Exports history</li>
			</ul>
			<h2 class="font-minecraft-title text-xl">Latest builds</h2>
			{#if loading}
				<p class="text-sm text-gray-300">Loading downloads...</p>
			{:else if error}
				<p class="text-sm text-red-300">{error}</p>
			{:else if artifacts.length > 0}
				<div class="mt-2 flex flex-col gap-3">
					{#each orderedPlatforms as platform}
						{@const platformArtifacts = artifacts.filter(
							(artifact) => getPlatform(artifact.name) === platform
						)}
						{#if platformArtifacts.length > 0}
							<div class:mb-4={platform === 'macos'}>
								<h3 class="mb-1 text-sm text-gray-300">{platformLabel(platform)}</h3>
								<div class="grid grid-cols-2 gap-2">
									{#each platformArtifacts as artifact (artifact.id)}
										{@const icon = platformIcon(platform)}
										<a
											class="flex min-w-0 items-center gap-2 rounded bg-surface-background-variant p-2 text-sm hover:bg-[#505050]"
											href={`https://github.com/spartacus04/jext-reborn/actions/runs/${runId}/artifacts/${artifact.id}`}
											target="_blank"
											rel="noopener noreferrer"
										>
											{#if icon}
												<img src={icon} alt="" class="h-6 w-6 shrink-0" />
											{/if}
											<span class="truncate">{platformLabel(platform)}</span>
											<span class="text-gray-300">/</span>
											<span class="shrink-0 text-gray-300">{getExtension(artifact.name)}</span>
										</a>
									{/each}
								</div>
							</div>
						{/if}
					{/each}
				</div>
			{:else}
				<p class="text-sm text-gray-300">No downloads are available.</p>
			{/if}
			<hr class="-mx-4 border-surface-separator" />
			<div class="flex pt-4 justify-end">
				<LauncherButton text="Dismiss" on:click={close} />
			</div>
		</div>
	</div>
</dialog>

<style lang="postcss">
	dialog {
		transition:
			display 0.1s allow-discrete,
			overlay 0.1s allow-discrete;

		animation: open 0.1s forwards;
	}

	dialog:not([open]) {
		animation: close 0.1s forwards;
	}

	@keyframes open {
		from {
			opacity: 0;
			transform: translateY(-2%);
		}
		to {
			opacity: 1;
			transform: translateY(0);
		}
	}

	@keyframes close {
		from {
			opacity: 1;
			transform: translateY(0);
		}
		to {
			opacity: 0;
			transform: translateY(-2%);
		}
	}
</style>
