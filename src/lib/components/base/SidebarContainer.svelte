<script lang="ts">
	import SidebarEntryLink from './SidebarEntryLink.svelte';
	import {
		cog,
		default_disc,
		github,
		knowledge_book,
		login,
		settings_icon,
		windows,
		macos,
		linux,
		android
	} from '$lib/assets';
	import { isTauri, os } from '$lib/state';
	import SidebarEntryButton from './SidebarEntryButton.svelte';
	import { fly, fade } from 'svelte/transition';
	import DesktopAppModal from '../modals/DesktopAppModal.svelte';

	const osIcon = (() => {
		switch (os) {
			case 'windows':
				return windows;
			case 'macos':
				return macos;
			case 'linux':
				return linux;
			case 'android':
				return android;
			default:
				return null;
		}
	})();

	const close = () => (isOpen = false);

	export let isOpen: boolean = true;

	let openDesktopAppModal: () => void;
</script>

<!-- desktop app popup -->
<DesktopAppModal bind:openModal={openDesktopAppModal} />

<div class="flex h-full w-full">
	{#if isOpen}
		<div
			class="z-10 min-w-64 w-64 bg-surface-background h-full flex flex-col fixed md:relative"
			transition:fly={{ x: '-16rem' }}
		>
			<!-- Top sidebar entries -->
			<SidebarEntryLink href="/" icon={default_disc} title="Disc manager" />
			{#if isTauri}
				<SidebarEntryLink href="/config" icon={cog} title="Config manager" />
			{/if}
			<SidebarEntryLink href="/docs" icon={knowledge_book} title="Documentation" />

			<div class="flex-grow" />

			<!-- Bottom sidebar entries -->
			{#if isTauri}
				<SidebarEntryButton icon={login} title="Connect" />
			{:else if osIcon}
				<SidebarEntryButton icon={osIcon} title="Desktop App" on:click={openDesktopAppModal} />
			{/if}
			<SidebarEntryLink
				href="https://github.com/spartacus04/jext-reborn"
				icon={github}
				title="Project page"
				external={true}
			/>
			<SidebarEntryButton icon={settings_icon} title="Web UI settings" />
		</div>

		<button
			class="fixed inset-0 bg-black bg-opacity-50 z-0 md:hidden"
			on:click={close}
			transition:fade
		/>
	{/if}
	<slot />
</div>
