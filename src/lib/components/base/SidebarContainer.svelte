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
		android,
		logout
	} from '$lib/assets';
	import { isTauri, os } from '$lib/state';
	import SidebarEntryButton from './SidebarEntryButton.svelte';
	import { fly, fade } from 'svelte/transition';
	import DesktopAppModal from '../modals/DesktopAppModal.svelte';
	import { base } from '$app/paths';
	import LoginModal from '../modals/LoginModal.svelte';
	import { pluginConnectorStore } from '$lib/pluginAccess/pluginConnector';
	import { goto } from '$app/navigation';

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
	let openLoginModal: () => void;
	let isLoginModalOpen: () => boolean;

	const disconnect = async () => {
		try {
			await $pluginConnectorStore?.disconnect();
		} catch (e: any) {
			console.error(e);
		}

		$pluginConnectorStore = undefined;
	};

	const openModalAndLink = async (modal: () => void, link: string) => {
		modal();

		await new Promise<void>((resolve) => {
			const unregister = setInterval(() => {
				if (!isLoginModalOpen()) {
					clearInterval(unregister);
					resolve();
				}
			}, 10);
		});

		goto(link);
	};
</script>

<!-- desktop app popup -->
<DesktopAppModal bind:openModal={openDesktopAppModal} />
<LoginModal bind:openModal={openLoginModal} bind:isOpen={isLoginModalOpen} />

<div class="flex h-full w-full">
	{#if isOpen}
		<div
			class="z-50 min-w-64 w-64 bg-surface-background h-full flex flex-col fixed md:relative"
			transition:fly={{ x: '-16rem' }}
		>
			<!-- Top sidebar entries -->
			<SidebarEntryLink href="{base}/" icon={default_disc} title="Disc manager" />
			{#if isTauri}
				{#if $pluginConnectorStore != undefined}
					<SidebarEntryLink href="{base}/config" icon={cog} title="Config manager" />
				{:else}
					<SidebarEntryButton
						icon={cog}
						title="Config manager"
						on:click={() => openModalAndLink(openLoginModal, `${base}/config`)}
					/>
				{/if}
				<SidebarEntryLink
					href="https://spartacus04.github.io/jext-reborn/docs"
					icon={knowledge_book}
					title="Documentation"
					external={true}
				/>
			{:else}
				<SidebarEntryLink href="{base}/docs" icon={knowledge_book} title="Documentation" />
			{/if}

			<div class="flex-grow" />

			<!-- Bottom sidebar entries -->
			{#if isTauri}
				{#if $pluginConnectorStore != undefined}
					<SidebarEntryButton icon={logout} title="Disconnect" on:click={disconnect} />
				{:else}
					<SidebarEntryButton icon={login} title="Connect" on:click={openLoginModal} />
				{/if}
			{:else if osIcon}
				<SidebarEntryButton icon={osIcon} title="Desktop App" on:click={openDesktopAppModal} />
			{/if}
			<SidebarEntryLink
				href="https://github.com/spartacus04/jext-reborn"
				icon={github}
				title="Project page"
				external={true}
			/>
			<!-- <SidebarEntryButton icon={settings_icon} title="Web UI settings" /> -->
		</div>

		<button
			class="fixed inset-0 bg-black bg-opacity-50 z-0 md:hidden"
			on:click={close}
			transition:fade
		/>
	{/if}
	<slot />
</div>
