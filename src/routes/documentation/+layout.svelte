<script lang="ts">
    import { Accordion, AccordionItem } from "@skeletonlabs/skeleton";
    import spigotmc from '$lib/assets/spigotmc.svg';
    import java from '$lib/assets/java.svg';
    import { slide } from 'svelte/transition';
	import { onMount } from "svelte";
	import { afterNavigate } from "$app/navigation";

    let indexOpen = false;
    let marginToggled = false;
    let main: HTMLElement;
    let first = true;

    onMount(() => {
        marginToggled = main.parentElement!.clientWidth < main?.parentElement!.scrollWidth;

        console.log(window.location.pathname.split('/').pop());

        indexOpen = window.location.pathname.split('/').pop() === 'documentation' || window.location.pathname.split('/').pop() === '';
    })

    afterNavigate(async () => {
        indexOpen = false;

        if(first && window.location.pathname.split('/').pop() === 'documentation') {
            first = false;
            indexOpen = true;
        }

        setTimeout(() => {
            marginToggled = main.parentElement!.clientWidth < main?.parentElement!.scrollWidth;
        }, 1000)
    })

    const toggleIndex = () => indexOpen = !indexOpen;

</script>

<div class="flex flex-col items-center p-2 bg-primary-500 rounded-bl-lg border-white border-l border-b fixed z-10 right-0 h-fit {marginToggled ? "mr-2" : ""}">
    {#if indexOpen}
        <div class="flex flex-col items-center" transition:slide>
            <h1 class="h2">Documentation</h1>
            <hr class="border-b border-white w-full m-2">
            <Accordion>
                <a href="/documentation" class="[&>*]:pointer-events-none">
                    <div id="AccordionAnchor" class="px-4 py-2">
                        <p class="ml-10 color text-white">Readme</p>
                    </div>
                </a>
                <AccordionItem>
                    <svelte:fragment slot="lead">
                        <img src={spigotmc} alt="spigot icon" class="w-6">
                    </svelte:fragment>
                    <svelte:fragment slot="summary">For server owners</svelte:fragment>
                    <svelte:fragment slot="content">
                        <Accordion>
                            <a href="/documentation/quickstart" class="[&>*]:pointer-events-none">
                                <div id="AccordionAnchor" class="px-4 py-2">
                                    <p class="ml-10 color text-white">Quickstart</p>
                                </div>
                            </a>
                        </Accordion>
                        <Accordion>
                            <a href="/documentation/config" class="[&>*]:pointer-events-none">
                                <div id="AccordionAnchor" class="px-4 py-2">
                                    <p class="ml-10 color text-white">Config documentation</p>
                                </div>
                            </a>
                        </Accordion>
                        <Accordion>
                            <a href="/documentation/resourcepack" class="[&>*]:pointer-events-none">
                                <div id="AccordionAnchor" class="px-4 py-2">
                                    <p class="ml-10 color text-white">Manual resourcepack config</p>
                                </div>
                            </a>
                        </Accordion>
                    </svelte:fragment>
                </AccordionItem>
                <AccordionItem>
                    <svelte:fragment slot="lead">
                        <img src={java} alt="java icon" class="w-6">
                    </svelte:fragment>
                    <svelte:fragment slot="summary">For developers</svelte:fragment>
                    <svelte:fragment slot="content">
                        <Accordion>
                            <AccordionItem>
                                <svelte:fragment slot="lead"><p class="w-6" /></svelte:fragment>
                                <svelte:fragment slot="summary">Javadocs</svelte:fragment>
                                <svelte:fragment slot="content">
                                    <Accordion>
                                        <a href="/javadocs/stable" class="[&>*]:pointer-events-none" target="_blank">
                                            <div id="AccordionAnchor" class="px-4 py-2">
                                                <p class="ml-10 color text-white">Stable</p>
                                            </div>
                                        </a>
                                        <a href="/javadocs/dev" class="[&>*]:pointer-events-none" target="_blank">
                                            <div id="AccordionAnchor" class="px-4 py-2">
                                                <p class="ml-10 color text-white">Development</p>
                                            </div>
                                        </a>
                                    </Accordion>
                                </svelte:fragment>
                            </AccordionItem>
                        </Accordion>
                    </svelte:fragment>
                </AccordionItem>
            </Accordion>
        </div>
    {/if}
    <button on:click={toggleIndex} class="w-full">
        {#if indexOpen} 
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512" class="fill-white h-8 cursor-pointer rotate-180 w-full">
                <path d="M201.4 374.6c12.5 12.5 32.8 12.5 45.3 0l160-160c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0L224 306.7 86.6 169.4c-12.5-12.5-32.8-12.5-45.3 0s-12.5 32.8 0 45.3l160 160z"></path>
            </svg>
        {:else}
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512" class="fill-white h-5 cursor-pointer w-full">
                <path d="M201.4 374.6c12.5 12.5 32.8 12.5 45.3 0l160-160c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0L224 306.7 86.6 169.4c-12.5-12.5-32.8-12.5-45.3 0s-12.5 32.8 0 45.3l160 160z"></path>
            </svg>
        {/if}
    </button>
</div> 

<svelte:head>
	<title>JEXT Documentation</title>
	<meta name="description" content="Web GUI for the Jext Reborn Minecraft plugin. Here you can check the documentation with ease">
</svelte:head>

<main class="p-5" bind:this={main}>
    <slot />
</main>