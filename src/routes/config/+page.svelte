<script lang="ts">
	import LauncherCheckbox from "$lib/components/inputs/LauncherCheckbox.svelte";
	import LauncherCombobox from "$lib/components/inputs/LauncherCombobox.svelte";
	import LauncherNumbox from "$lib/components/inputs/LauncherNumbox.svelte";
	import LauncherTextbox from "$lib/components/inputs/LauncherTextbox.svelte";
    import { pluginConnectorStore, type ConfigNode } from "$lib/pluginAccess/pluginConnector";
    import EditDungeonConfigModal from "$lib/components/modals/EditDungeonConfigModal.svelte";
	import { get } from "svelte/store";
	import { baseElement } from "$lib/state";
	import LauncherButton from "$lib/components/buttons/LauncherButton.svelte";
	import { cAlert } from "$lib/utils";
	import { goto } from "$app/navigation";
    
    let currentConfig: any = {}

    const getConfig = async () => {
        const results = await $pluginConnectorStore!.getConfig()

        results.forEach(node => {
            currentConfig[node.id] = node.value;
        });

        return results;
    }

    const openDungeonSelector = async (target: string) => {
        const values = { ...currentConfig[target]};

        currentConfig[target] = await new Promise<{ [key: string]: number } | null>((resolve) => {
            const editDungeonModal = new EditDungeonConfigModal({
                target: get(baseElement)!,
                props: {
                    onFinish: (changes) => {
                        editDungeonModal.$destroy();
                        resolve(changes);
                    },
                    dungeons: values,
                    defaultValue: target == 'disc-loottables-limit' ? 2 : 3
                }
            });

            editDungeonModal.openModal();
        });
    }

    const reset = (config: ConfigNode[]) => {
        config.forEach(configNode => {
            currentConfig[configNode.id] = configNode.default;
        });
    };

    const apply = async () => {
        await $pluginConnectorStore!.applyConfig(currentConfig);

        cAlert("Config applied successfully!");
    }

    $: $pluginConnectorStore ?? goto('../')
</script>

<svelte:head>
	<title>JEXT | Companion App</title>
	<meta
		name="description"
		content="Web GUI for the Jext Reborn Minecraft plugin. Here you can manage the resourcepack and music discs with ease!"
	/>
</svelte:head>

{#await getConfig()}
    <div class="flex w-full h-full items-center justify-center">
        <h1 class="font-minecraft text-white text-2xl">Fetching config...</h1>
    </div>
{:then config}
    <div class="flex h-full flex-col w-full">
        <div class="flex flex-row bg-surface-background w-full py-4 px-8">
            <b class="text-white text-xs uppercase">CONFIG MANAGER</b>
        </div>
        <div class="overflow-y-auto overflow-x-auto">
            {#each config as configNode}
                <div class="first:border-t-0 border-t border-surface-background flex flex-col md:flex-row w-full p-4 gap-4 font-minecraft bg-surface-background-variant flex-1 items-center md:items-start">
                    <div id="shadow" class="flex flex-col w-full">
                        <b class="text-white text-lg">{configNode.name}</b>
                        <p class="text-white text-sm">{configNode.description}</p>
                    </div>

                    <div class="justify-self-end w-80" title={configNode.name}>
                        {#if typeof configNode.default === 'boolean'}
                            <LauncherCheckbox bind:value={currentConfig[configNode.id]} label={configNode.name} />
                        {:else if typeof configNode.default === 'number'}
                            <LauncherNumbox min={0} max={65536} placeholder="Num" bind:value={currentConfig[configNode.id]} />
                        {:else if typeof configNode.default === 'object'}
                            <LauncherButton text={configNode.name} type="primary" classes="w-full text-white" on:click={() => openDungeonSelector(configNode.id)} />
                        {:else if configNode.enumValues && Array.isArray(configNode.enumValues)}
                            <LauncherCombobox bind:value={currentConfig[configNode.id]} options={configNode.enumValues} />
                        {:else}
                            <LauncherTextbox placeholder={configNode.name} bind:value={currentConfig[configNode.id]} />
                        {/if}
                    </div>
                </div>
            {/each}

            <div class="flex items-center justify-center flex-col md:flex-row gap-4 [&>*]:w-40 text-white p-4">
                <LauncherButton text="Apply" type="primary" on:click={apply} />
                <LauncherButton text="Reset" type="danger" on:click={() => reset(config)} />
            </div>
        </div>
    </div>
{/await}

<style>
    #shadow {
        text-shadow: 0 0 5px rgba(0, 0, 0, 0.5);
    }
</style>