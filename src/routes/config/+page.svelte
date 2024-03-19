<script lang="ts">
	import { fetchAuthed, isLoggedIn, login, healthCheck, logout, LoginStore } from "$lib/login";
	import type { PageData } from "./$types";
	import { onMount } from "svelte";
	import { AppBar, AppShell, ProgressRadial, getModalStore } from "@skeletonlabs/skeleton";
    import type { ConfigNode as CfgNode } from "$lib/types";
	import { get, writable } from "svelte/store";
    import ConfigNode from "$lib/components/ConfigNode.svelte";
    import dark_dirt_background from "$lib/assets/dark_dirt_background.png";
	import MinecraftLaunchButton from "$lib/components/MinecraftLaunchButton.svelte";

	export let data: PageData;
	const modalStore = getModalStore();

    const configStore = writable<CfgNode<boolean|number|string|{[key : string] : number}>[]|null>(null);
    let configSnapshot: any[] = [];

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
		} else {
            const connected = await login(modalStore, {
                ip: data.server.ip,
                port: data.server.port,
            })

            if(!connected) return;

            window.location.reload()
        }
	})

    const fetchPageSettingsPromise = new Promise<CfgNode<boolean|number|string|{[key : string] : number}>[]|null>((resolve) => {
        if(isLoggedIn()) {
            fetchAuthed('config/read').then(async (response) => {
                const json = await response.json();

                resolve(json)
            })
        } else {
            resolve(null)
        }
    })

    const fetchPageSettings = async () => {
        configStore.set(await fetchPageSettingsPromise)
        
        configSnapshot = [];

        if(get(configStore) == null) return get(configStore);

        get(configStore)!.forEach((node) => {
            configSnapshot.push({
                id: node.id,
                value: node.value
            })
        })

        return get(configStore)
    }

    const reload = async () => await fetchPageSettings();

    const applySettings = async () => {
        const settings = get(configStore)!;

        const data : {[key: string] : any} = {};

        for(const node of settings) {
            data[node.id] = node.value;
        }

        const response = await fetchAuthed('config/apply', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })

        if(response.status !== 200) {
            modalStore.trigger({
                type: 'alert',
                title: 'Failed to apply settings',
                body: 'The settings could not be applied. Check the server logs for more information',
                buttonTextCancel: 'Ok'
            })

            return;
        } else {
            modalStore.trigger({
                type: 'alert',
                title: 'Settings applied',
                buttonTextCancel: 'Ok',
                body: 'The settings have been applied successfully.'
            })

            await reload();
        }
    }

    $: canApply = $configStore != null && $configStore.some((node) => {
        return JSON.stringify(node.value) !== JSON.stringify(configSnapshot!.find((snapshotNode) => snapshotNode.id == node.id)!.value)
    })
</script>


{#await fetchPageSettings()}
    <div class="h-full w-full flex items-center justify-center">
        <ProgressRadial />
    </div>
{:then _}
    {#if $configStore == null}
        <div class="flex flex-col items-center justify-center h-screen">
            <h1 class="text-4xl font-bold">You are not connected to the plugin</h1>
            <p class="text-xl">Please connect to continue</p>

            <button on:click={
                async () => {
                    const connected = await login(modalStore, {
                        ip: data.server.ip,
                        port: data.server.port,
                    })

                    if(!connected) return;

                    const url = new URL(window.location.href);
                    url.search = ''

                    window.location.href = url.toString()
                }
            } class="btn variant-filled">Connect</button> 
        </div>
    {:else}
        <AppShell>
            <svelte:fragment slot="header">
                <AppBar regionRowMain="grid-cols-[1fr] sm:grid-cols-[auto_1fr_auto]" slotTrail="-mt-6  justify-self-center">
                    <svelte:fragment slot="lead">
                        <div>
                            <h1 class="text-2xl font-bold">Plugin configurator</h1>
                            <p class="text-sm">Here you can change the settings of the JEXT-plugin</p>
                        </div>
                    </svelte:fragment>
                    <svelte:fragment slot="trail">
                        <button class="btn variant-filled hover:bg-blue-500 hover:text-white" on:click={() => logout(true)}>
                            Log out
                        </button>
                    </svelte:fragment>
                </AppBar>
            </svelte:fragment>
            <div style:background-image="url({dark_dirt_background})" class="bg-[length:64px_64px] flex flex-col gap-4 py-2">
                {#each $configStore as configNode}
                    <ConfigNode bind:node={configNode} />
                {/each}
            </div>
            <svelte:fragment slot="footer">
                <div class="flex justify-center w-full bg-surface-500 p-2">
                    <MinecraftLaunchButton on:click={applySettings} highlight={true} bind:enabled={canApply}>Apply</MinecraftLaunchButton>
                </div>
            </svelte:fragment>
        </AppShell>
    {/if}
{/await}

