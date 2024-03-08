<script lang="ts">
	import { LoginStore, healthCheck, isLoggedIn, login, logout } from "$lib/login";
	import type { PageData } from "./$types";
	import { onMount } from "svelte";
	import { AppBar, AppShell, ProgressRadial, getModalStore, type ModalComponent, Accordion, AccordionItem } from "@skeletonlabs/skeleton"; 
	import type { Disc } from "$lib/types";
	import { writable } from "svelte/store";
	import MinecraftButton from "$lib/components/MinecraftButton.svelte";
	import ImportResourcePackModal from "$lib/components/ImportResourcePackModal.svelte";
	import { JextReader } from "$lib/resourcepack/utils";
	import { importRP } from "$lib/resourcepack/importer";
	import CreateDiscModal from "$lib/components/CreateDiscModal.svelte";
	import { press } from "svelte-gestures";

	export let data: PageData;
	const modalStore = getModalStore();
	
	const discsStore = writable<Disc[]>([])

	onMount(async () => {
		if(isLoggedIn()) {
			if(data.server.connect) {
				if(await new Promise<Boolean>((resolve) => {modalStore.trigger({
					type: 'confirm',
					title: 'Already logged in',
					body: 'You are already logged in. Do you want to connect to this server?',
					response: (response) => resolve(response)
				})})) {
					const connected = await login(modalStore, {
						ip: data.server.ip,
						port: data.server.port,
					})

					if(!connected) return;

					const url = new URL(window.location.href);
					url.search = ''

					window.location.href = url.toString()
				}
			}

            if(!(await healthCheck())) {
                logout(true)
            }
		} else if(data.server.connect) {
            const connected = await login(modalStore, {
                ip: data.server.ip,
                port: data.server.port,
            })

            if(!connected) return;

			const url = new URL(window.location.href);
			url.search = ''

            window.location.reload()
        }
	});

	let reload: Promise<void>;

	const importPack = async () => {
		const modalComponent: ModalComponent = { ref: ImportResourcePackModal };

		const files = await new Promise<{resourcePack: File, discs: string|null}|null>((resolve) => {
			modalStore.trigger({
				type: 'component',
				component: modalComponent,
				title: 'Import a JEXT Resource pack',
				response(r) {
					if(!r) return resolve(null);

					resolve(r);
				},
			})
		})

		if(!files) return;

		reload = (async () => {
			const { discs, resourcePack } = files;

			const discsObj = discs ? JSON.parse(discs) : await JextReader(resourcePack);

			await importRP(discsStore, resourcePack, discsObj);

			console.log($discsStore);
		})()
	}

	const addDisc = async () => {
		const modalComponent: ModalComponent = { ref: CreateDiscModal };

		const discs = await new Promise<Disc[]|null>((resolve) => {
			modalStore.trigger({
				type: 'component',
				component: modalComponent,
				title: 'Create a new disc',
				response(r) {
					if(!r) return resolve(null);

					resolve(r.discs);
				},
			})
		})

		if(!discs) return;

		discsStore.update(discsStore => [...discsStore, ...discs!]);
	}

	const group: {
		uploaded: Disc[],
		saved: Disc[],
	} = {
		uploaded: [],
		saved: [],
	};

	let selected: string[] = [];
	let selectionMode = false;

	const select = (id: string) => {
		if(selected.includes(id)) {
			selected = selected.filter(e => e != id);
		} else {
			selected = [...selected, id]
		}
	}

	const tap = (id: string) => {
		if(selectionMode) {
			select(id);
		} else {
			// TODO: implement disc editor
		}
	}

	$: (group.uploaded = $discsStore.filter(disc => disc.uploadData)) && (group.saved = $discsStore.filter(disc => disc.packData));
	$: selectionMode = selected.length > 0;
</script>

<AppShell>
	<svelte:fragment slot="header">
		<AppBar regionRowMain="grid-cols-[1fr] sm:grid-cols-[auto_1fr_auto]" slotTrail="-mt-6  justify-self-center">
			<svelte:fragment slot="lead">
				<div>
					<h1 class="text-2xl font-bold">Resource pack configurator</h1>
					<p class="text-sm">Here you can manage custom discs and the resource pack</p>
				</div>
			</svelte:fragment>
			<svelte:fragment slot="trail">
				{#if $LoginStore != null}
					<button class="btn variant-filled hover:bg-blue-500 hover:text-white" on:click={() => logout(true)}>
						Log out
					</button>
				{:else}
					<button class="btn variant-filled hover:bg-blue-500 hover:text-white" on:click={() => login(modalStore, { ip: null, port: null})}>
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
					<MinecraftButton on:click={addDisc}>Create a new disc</MinecraftButton>
				</div>
			</div>
		{:else}
			<Accordion>
				<AccordionItem open={group.uploaded.length > 0}>
					<svelte:fragment slot="summary">
						<h2 class="h2 font-minecraft">New discs</h2>
					</svelte:fragment>
					<svelte:fragment slot="content">
						<div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 2xl:grid-cols-7 gap-4 text-ellipsis">
							{#key selected}
								{#each group.uploaded as disc}
									{#if disc.uploadData}
										{#if selected.includes(disc["disc-namespace"])}
											<button class="card p-4 rounded-lg card-hover cursor-pointer [&>*]:pointer-events-none variant-filled-tertiary" use:press={{ timeframe: 500, triggerBeforeFinished: true }} on:press={() => select(disc["disc-namespace"])} on:click={() => tap(disc["disc-namespace"])}>
												<img src={URL.createObjectURL(disc.uploadData.uploadedTexture)} alt="" class="w-full aspect-square">
												<h3 class="h3 font-minecraft text-ellipsis w-[calc(100%)] whitespace-nowrap overflow-hidden">{disc.name}</h3>
												<p class="p font-minecraft text-ellipsis w-[calc(100%)] whitespace-nowrap overflow-hidden text-mc-light-gray">{disc["disc-namespace"]}</p>
											</button>
										{:else}
											<button class="card p-4 rounded-lg card-hover cursor-pointer [&>*]:pointer-events-none" use:press={{ timeframe: 500, triggerBeforeFinished: true }} on:press={() => select(disc["disc-namespace"])} on:click={() => tap(disc["disc-namespace"])}>
												<img src={URL.createObjectURL(disc.uploadData.uploadedTexture)} alt="" class="w-full aspect-square">
												<h3 class="h3 font-minecraft text-ellipsis w-[calc(100%)] whitespace-nowrap overflow-hidden">{disc.name}</h3>
												<p class="p font-minecraft text-ellipsis w-[calc(100%)] whitespace-nowrap overflow-hidden text-mc-light-gray">{disc["disc-namespace"]}</p>
											</button>
										{/if}
									{/if}

								{/each}
							{/key}
							<button class="card p-4 rounded-lg flex items-center justify-center card-hover cursor-pointer" on:click={addDisc}>
								<svg xmlns="http://www.w3.org/2000/svg" class="w-[50%] aspect-square text-[d3d3d3]" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M5 12h14"/><path d="M12 5v14"/></svg>
							</button>
						</div>
					</svelte:fragment>
				</AccordionItem>
				<AccordionItem open={group.saved.length > 0}>
					<svelte:fragment slot="summary">
						<h2 class="h2 font-minecraft">Saved discs</h2>
					</svelte:fragment>
					<svelte:fragment slot="content">
						<div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 2xl:grid-cols-7 gap-4">
							{#each group.saved as disc}
								<div class="bg-gray-100 p-4 rounded-lg">
									<h3 class="h3 font-minecraft">{disc.name}</h3>
									<p class="p">{disc["disc-namespace"]}</p>
								</div>
							{/each}
						</div>
					</svelte:fragment>
				</AccordionItem>
			</Accordion>

			<div class="flex justify-center gap-4">
				<MinecraftButton on:click={importPack}>Import a JEXT resource pack</MinecraftButton>
				<MinecraftButton on:click={addDisc}>Create a new disc</MinecraftButton>
			</div>
		{/if}
	{/await}

	<svelte:fragment slot="footer">
		{#if $discsStore.length > 0}
			footer
		{/if}
	</svelte:fragment>
</AppShell>


<!--
	se non sono connesso mostrare tasto di connessione in alto, permettere di importare un file zip (e eventualmente il discs.json) o di creare un progetto vuoto, e infine permettere il download
	se sono connesso al server i dischi sono caricati automaticamente e il tasto di connessione diventa di logout. il tasto di scaricamento diventa un tasto di applicazione.render

	nello scaricamento aggiungo rp di geyser, mentre nell'applicazione prima verifico se geyser è presente e in caso applico una nuova rp per geyser

	aggiungo un tasto per mescolare piu pacchetti di risorse in ordine di priorità (e un tasto per rimuovere i pacchetti di risorse aggiuntivi)
-->