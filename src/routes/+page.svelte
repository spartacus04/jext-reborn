<script lang="ts">
	import type { PageData } from './$types';

	import {
		AppBar,
		AppShell,
		ProgressRadial,
		getModalStore,
		type ModalComponent,
		Accordion,
		AccordionItem,
		getToastStore
	} from '@skeletonlabs/skeleton';
	import { press } from 'svelte-gestures';
	import { onMount } from 'svelte';

	import {
		MinecraftButton,
		ImportResourcePackModal,
		CreateDiscModal,
		EditDiscModal,
		ResourcePackManager,
		MinecraftLaunchButton,
		OutputModal,
		ErrorPopup
	} from '$lib/components';

	import {
		LoginStore,
		fetchAuthed,
		healthCheck,
		isLoggedIn,
		login,
		logout,
		type Disc,
		JextReader,
		RPChecker,
		importRP,
		discsStore
	} from '$lib/index';

	import { beforeNavigate } from '$app/navigation';

	export let data: PageData;
	const modalStore = getModalStore();
	const toastStore = getToastStore();

	onMount(async () => {
		if (isLoggedIn()) {
			if (data.server.connect) {
				if (
					await new Promise<boolean>((resolve) => {
						modalStore.trigger({
							type: 'confirm',
							title: 'Already logged in',
							body: 'You are already logged in. Do you want to connect to this server?',
							response: (response) => resolve(response)
						});
					})
				) {
					const connected = await login(modalStore, toastStore, ErrorPopup, {
						ip: data.server.ip,
						port: data.server.port
					});

					if (!connected) return;

					const url = new URL(window.location.href);
					url.search = '';

					window.location.href = url.toString();
				}
			}

			if (!(await healthCheck())) {
				logout(true);
			}
		} else if (data.server.connect) {
			const connected = await login(modalStore, toastStore, ErrorPopup, {
				ip: data.server.ip,
				port: data.server.port
			});

			if (!connected) return;

			const url = new URL(window.location.href);
			url.search = '';

			window.location.reload();
		}
	});

	let reload: Promise<void>;

	const importPack = async () => {
		const modalComponent: ModalComponent = { ref: ImportResourcePackModal };

		const files = await new Promise<{ resourcePack: File; discs: string | null } | null>(
			(resolve) => {
				modalStore.trigger({
					type: 'component',
					component: modalComponent,
					title: 'Import a JEXT Resource pack',
					response(r) {
						if (!r) return resolve(null);

						resolve(r);
					}
				});
			}
		);

		if (!files) return;

		reload = (async () => {
			const { discs, resourcePack } = files;

			const discsObj = discs ? JSON.parse(discs) : await JextReader(resourcePack);

			await importRP(discsStore, resourcePack, discsObj);
		})();
	};

	const importFromPlugin = async () => {
		if (!isLoggedIn()) {
			const connected = await login(modalStore, toastStore, ErrorPopup, {
				ip: data.server.ip,
				port: data.server.port
			});

			if (!connected) return;
		}

		reload = (async () => {
			const response = await fetchAuthed('discs/read');

			const rp = await response.blob();

			if ((await RPChecker(rp)) != 'JextRP') {
				alert('The resourcepack is not a JEXT Resourcepack, please import it manually');
				return;
			}

			const discs = await JextReader(rp);

			await importRP(discsStore, rp, discs);
		})();
	};

	const addDisc = async () => {
		const modalComponent: ModalComponent = { ref: CreateDiscModal };

		const discs = await new Promise<Disc[] | null>((resolve) => {
			modalStore.trigger({
				type: 'component',
				component: modalComponent,
				title: 'Create a new disc',
				response(r) {
					if (!r) return resolve(null);

					resolve(r.discs);
				}
			});
		});

		if (!discs) return;

		discsStore.update((discsStore) => [...discsStore, ...discs!]);
	};

	const group: {
		uploaded: Disc[];
		saved: Disc[];
	} = {
		uploaded: [],
		saved: []
	};

	let selected: string[] = [];
	let selectionMode = false;

	const select = (id: string) => {
		if (selected.includes(id)) {
			selected = selected.filter((e) => e != id);
		} else {
			selected = [...selected, id];
		}
	};

	const tap = async (id: string) => {
		if (selectionMode) {
			select(id);
		} else {
			const modalComponent: ModalComponent = {
				ref: EditDiscModal,
				props: { discNamespaces: [id] }
			};

			await new Promise<Disc[] | null>((resolve) => {
				modalStore.trigger({
					type: 'component',
					component: modalComponent,
					title: 'Edit one or more discs',
					response(r) {
						if (!r) return resolve(null);

						resolve(r.discs);
					}
				});
			});
		}
	};

	const editMultiple = async () => {
		const modalComponent: ModalComponent = {
			ref: EditDiscModal,
			props: { discNamespaces: selected }
		};

		await new Promise<Disc[] | null>((resolve) => {
			modalStore.trigger({
				type: 'component',
				component: modalComponent,
				title: 'Edit one or more discs',
				response(r) {
					if (!r) return resolve(null);

					resolve(r.discs);
				}
			});
		});

		selected = [];
	};

	discsStore.subscribe((discs) => {
		discs.forEach((disc, i) => {
			if (disc.uploadData) {
				disc['disc-namespace'] = `${disc.title}${disc.author}${i}`
					.replace(/[^a-zA-Z0-9]/g, '')
					.replaceAll('1', 'one')
					.replaceAll('2', 'two')
					.replaceAll('3', 'three')
					.replaceAll('4', 'four')
					.replaceAll('5', 'five')
					.replaceAll('6', 'six')
					.replaceAll('7', 'seven')
					.replaceAll('8', 'eight')
					.replaceAll('9', 'nine')
					.replaceAll('0', 'zero')
					.toLowerCase();
			}
		});
	});

	$: (group.uploaded = $discsStore.filter((disc) => disc.uploadData)) &&
		(group.saved = $discsStore.filter((disc) => disc.packData));
	$: selectionMode = selected.length > 0;

	const generateResourcePack = async () => {
		const modalComponent: ModalComponent = { ref: OutputModal };

		modalStore.trigger({
			type: 'component',
			component: modalComponent,
			title: 'Output discs',
			backdropClasses: 'p-[0px!important]'
		});
	};

	beforeNavigate(({ cancel }) => {
		if ($discsStore.length == 0) return;

		if (!confirm('Leave without saving?')) {
			return cancel();
		}

		$discsStore = [];
	});
</script>

<svelte:head>
	<title>JEXT Resourcepack Manager</title>
	<meta
		name="description"
		content="Web GUI for the Jext Reborn Minecraft plugin. Here you can manage the resourcepack and music discs with ease!"
	/>
</svelte:head>

<AppShell>
	<svelte:fragment slot="header">
		<AppBar
			regionRowMain="grid-cols-[1fr] sm:grid-cols-[auto_1fr_auto]"
			slotTrail="-mt-6 justify-self-center"
			background="bg-[#202020]"
		>
			<svelte:fragment slot="lead">
				<div>
					<h1 class="text-2xl font-bold">Resource pack configurator</h1>
					<p class="text-sm">Here you can manage custom discs and the resource pack</p>
				</div>
			</svelte:fragment>
			<svelte:fragment slot="trail">
				{#if $LoginStore != null}
					<button
						class="btn variant-filled hover:bg-blue-500 hover:text-white"
						on:click={() => logout(true)}
					>
						Log out
					</button>
				{:else}
					<button
						class="btn variant-filled hover:bg-blue-500 hover:text-white"
						on:click={() => login(modalStore, toastStore, ErrorPopup, { ip: null, port: null })}
					>
						Connect
					</button>
				{/if}
			</svelte:fragment>
		</AppBar>
	</svelte:fragment>
	{#await reload}
		<div class="h-full w-full flex items-center justify-center">
			<ProgressRadial />
		</div>
	{:then _}
		{#if $discsStore.length == 0}
			<div class="h-full w-full flex items-center justify-center flex-col gap-2">
				<h1 class="h1 font-minecraft">Wow, so empty!</h1>
				<div class="flex flex-col sm:flex-row items-center gap-2">
					<MinecraftButton on:click={importPack}>Import a JEXT resource pack</MinecraftButton>
					<p class="p font-minecraft">or</p>
					<MinecraftButton on:click={importFromPlugin}>Import from plugin API</MinecraftButton>
					<p class="p font-minecraft">or</p>
					<MinecraftButton on:click={addDisc}>Create a new disc</MinecraftButton>
				</div>
			</div>
		{:else}
			<Accordion>
				<AccordionItem open={true}>
					<svelte:fragment slot="summary">
						<h2 class="h2 font-minecraft">New discs</h2>
					</svelte:fragment>
					<svelte:fragment slot="content">
						<div
							class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 2xl:grid-cols-7 gap-4 text-ellipsis"
						>
							{#key selected}
								{#each group.uploaded as disc}
									{#if disc.uploadData}
										{#if selected.includes(disc['disc-namespace'])}
											<button
												class="card p-4 rounded-lg card-hover cursor-pointer [&>*]:pointer-events-none variant-filled-tertiary"
												use:press={{ timeframe: 500, triggerBeforeFinished: true }}
												on:press={() => select(disc['disc-namespace'])}
												on:click={() => tap(disc['disc-namespace'])}
											>
												<img
													src={URL.createObjectURL(disc.uploadData.uploadedTexture)}
													alt=""
													class="w-full aspect-square"
												/>
												<h3
													class="h3 font-minecraft text-ellipsis w-[calc(100%)] whitespace-nowrap overflow-hidden"
												>
													{disc.title}
												</h3>
												<p
													class="p font-minecraft text-ellipsis w-[calc(100%)] whitespace-nowrap overflow-hidden text-mc-light-gray"
												>
													{disc['disc-namespace']}
												</p>
											</button>
										{:else}
											<button
												class="card p-4 rounded-lg card-hover cursor-pointer [&>*]:pointer-events-none"
												use:press={{ timeframe: 500, triggerBeforeFinished: true }}
												on:press={() => select(disc['disc-namespace'])}
												on:click={() => tap(disc['disc-namespace'])}
											>
												<img
													src={URL.createObjectURL(disc.uploadData.uploadedTexture)}
													alt=""
													class="w-full aspect-square"
												/>
												<h3
													class="h3 font-minecraft text-ellipsis w-[calc(100%)] whitespace-nowrap overflow-hidden"
												>
													{disc.title}
												</h3>
												<p
													class="p font-minecraft text-ellipsis w-[calc(100%)] whitespace-nowrap overflow-hidden text-mc-light-gray"
												>
													{disc['disc-namespace']}
												</p>
											</button>
										{/if}
									{/if}
								{/each}
							{/key}

							{#if selectionMode}
								<button
									class="card p-4 rounded-lg flex items-center justify-center card-hover cursor-pointer"
									on:click={editMultiple}
								>
									<svg
										xmlns="http://www.w3.org/2000/svg"
										viewBox="0 0 24 24"
										fill="none"
										stroke="currentColor"
										stroke-width="2"
										stroke-linecap="round"
										stroke-linejoin="round"
										class="w-[50%] lucide lucide-pencil-ruler"
										><path d="m15 5 4 4" /><path
											d="M13 7 8.7 2.7a2.41 2.41 0 0 0-3.4 0L2.7 5.3a2.41 2.41 0 0 0 0 3.4L7 13"
										/><path d="m8 6 2-2" /><path
											d="m2 22 5.5-1.5L21.17 6.83a2.82 2.82 0 0 0-4-4L3.5 16.5Z"
										/><path d="m18 16 2-2" /><path
											d="m17 11 4.3 4.3c.94.94.94 2.46 0 3.4l-2.6 2.6c-.94.94-2.46.94-3.4 0L11 17"
										/></svg
									>
								</button>
							{/if}

							<button
								class="card p-4 rounded-lg flex items-center justify-center card-hover cursor-pointer"
								on:click={addDisc}
							>
								<svg
									xmlns="http://www.w3.org/2000/svg"
									class="w-[50%] aspect-square text-[d3d3d3]"
									viewBox="0 0 24 24"
									fill="none"
									stroke="currentColor"
									stroke-width="2"
									stroke-linecap="round"
									stroke-linejoin="round"><path d="M5 12h14" /><path d="M12 5v14" /></svg
								>
							</button>
						</div>
					</svelte:fragment>
				</AccordionItem>
				<AccordionItem open={group.saved.length > 0}>
					<svelte:fragment slot="summary">
						<h2 class="h2 font-minecraft">Saved discs</h2>
					</svelte:fragment>
					<svelte:fragment slot="content">
						<div
							class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 2xl:grid-cols-7 gap-4 text-ellipsis"
						>
							{#key selected}
								{#each group.saved as disc}
									{#if disc.packData}
										{#if selected.includes(disc['disc-namespace'])}
											<button
												class="card p-4 rounded-lg card-hover cursor-pointer [&>*]:pointer-events-none variant-filled-tertiary"
												use:press={{ timeframe: 500, triggerBeforeFinished: true }}
												on:press={() => select(disc['disc-namespace'])}
												on:click={() => tap(disc['disc-namespace'])}
											>
												<img
													src={URL.createObjectURL(disc.packData.texture)}
													alt=""
													class="w-full aspect-square"
												/>
												<h3
													class="h3 font-minecraft text-ellipsis w-[calc(100%)] whitespace-nowrap overflow-hidden"
												>
													{disc.title}
												</h3>
												<p
													class="p font-minecraft text-ellipsis w-[calc(100%)] whitespace-nowrap overflow-hidden text-mc-light-gray"
												>
													{disc['disc-namespace']}
												</p>
											</button>
										{:else}
											<button
												class="card p-4 rounded-lg card-hover cursor-pointer [&>*]:pointer-events-none"
												use:press={{ timeframe: 500, triggerBeforeFinished: true }}
												on:press={() => select(disc['disc-namespace'])}
												on:click={() => tap(disc['disc-namespace'])}
											>
												<img
													src={URL.createObjectURL(disc.packData.texture)}
													alt=""
													class="w-full aspect-square"
												/>
												<h3
													class="h3 font-minecraft text-ellipsis w-[calc(100%)] whitespace-nowrap overflow-hidden"
												>
													{disc.title}
												</h3>
												<p
													class="p font-minecraft text-ellipsis w-[calc(100%)] whitespace-nowrap overflow-hidden text-mc-light-gray"
												>
													{disc['disc-namespace']}
												</p>
											</button>
										{/if}
									{/if}
								{/each}
							{/key}

							{#if selectionMode}
								<button
									class="card p-4 rounded-lg flex items-center justify-center card-hover cursor-pointer"
									on:click={editMultiple}
								>
									<svg
										xmlns="http://www.w3.org/2000/svg"
										viewBox="0 0 24 24"
										fill="none"
										stroke="currentColor"
										stroke-width="2"
										stroke-linecap="round"
										stroke-linejoin="round"
										class="w-[50%] lucide lucide-pencil-ruler"
										><path d="m15 5 4 4" /><path
											d="M13 7 8.7 2.7a2.41 2.41 0 0 0-3.4 0L2.7 5.3a2.41 2.41 0 0 0 0 3.4L7 13"
										/><path d="m8 6 2-2" /><path
											d="m2 22 5.5-1.5L21.17 6.83a2.82 2.82 0 0 0-4-4L3.5 16.5Z"
										/><path d="m18 16 2-2" /><path
											d="m17 11 4.3 4.3c.94.94.94 2.46 0 3.4l-2.6 2.6c-.94.94-2.46.94-3.4 0L11 17"
										/></svg
									>
								</button>
							{/if}
						</div>
					</svelte:fragment>
				</AccordionItem>
				<AccordionItem open={true}>
					<svelte:fragment slot="summary">
						<h2 class="h2 font-minecraft">Resourcepack settings</h2>
					</svelte:fragment>
					<svelte:fragment slot="content">
						<ResourcePackManager />
					</svelte:fragment>
				</AccordionItem>
			</Accordion>
		{/if}
	{/await}

	<svelte:fragment slot="footer">
		{#if $discsStore.length > 0}
			<div class="flex w-full items-center justify-center p-4 bg-[#151515]">
				<MinecraftLaunchButton highlight={true} on:click={generateResourcePack}>
					<b class="font-minecraft-launcher text-4xl p-0 m-0 ease-in-out duration-500 select-none"
						>GENERATE</b
					>
				</MinecraftLaunchButton>
			</div>
		{/if}
	</svelte:fragment>
</AppShell>
