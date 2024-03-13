<script lang="ts">
	import { popup, type PopupSettings } from '@skeletonlabs/skeleton';
    import MCText from 'minecraft-text-js';

    export let split: string[] = [];
    export let text = '';
    export let firstline = '';
    let output = '';

    let previewDiv: HTMLDivElement;

    // Workaround for library handling & when not necessary and not handling new line
    $: (output = MCText.toHTML(text.replaceAll('&', 'amp;')
            .replaceAll('<div>', '\\n')
            .replaceAll('</div>', '')
            .replaceAll('<br>', '')
        ).replaceAll('amp;', '&')) &&
        setTimeout(() => MCText.refeashObfuscate(previewDiv), 1);
    
    $: split = text.replaceAll('</div>', '')
        .replaceAll('<br>', '')
        .split('<div>')

    const popupHover: PopupSettings = {
        event: 'focus-blur',
        target: 'colorPopup',
        placement: 'bottom'
    };

    const pasteHandler = (e: ClipboardEvent|Event) => {
        e.preventDefault();
        const text = ((e as any).originalEvent || e as ClipboardEvent).clipboardData?.getData('text/plain') || '';
        document.execCommand('insertText', false, text);
    }

    const insertSymbol = (symbol: string) => {
        document.execCommand('insertText', false, `§${symbol}`);
    }
</script>

<div class="flex h-min flex-col border-mc-light-gray border rounded-md">
    <div class="h-8 m-2 mb-0 pb-2 border-b border-mc-light-gray">
        <button class="[&>*]:pointer-events-none mr-4" use:popup={popupHover}>
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-palette"><circle cx="13.5" cy="6.5" r=".5" fill="currentColor"/><circle cx="17.5" cy="10.5" r=".5" fill="currentColor"/><circle cx="8.5" cy="7.5" r=".5" fill="currentColor"/><circle cx="6.5" cy="12.5" r=".5" fill="currentColor"/><path d="M12 2C6.5 2 2 6.5 2 12s4.5 10 10 10c.926 0 1.648-.746 1.648-1.688 0-.437-.18-.835-.437-1.125-.29-.289-.438-.652-.438-1.125a1.64 1.64 0 0 1 1.668-1.668h1.996c3.051 0 5.555-2.503 5.555-5.554C21.965 6.012 17.461 2 12 2z"/></svg>
        </button>

        <button class="[&>*]:pointer-events-none" on:click={() => insertSymbol('l')}>
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-bold"><path d="M14 12a4 4 0 0 0 0-8H6v8"/><path d="M15 20a4 4 0 0 0 0-8H6v8Z"/></svg>
        </button>
        <button class="[&>*]:pointer-events-none" on:click={() => insertSymbol('o')}>
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-italic"><line x1="19" x2="10" y1="4" y2="4"/><line x1="14" x2="5" y1="20" y2="20"/><line x1="15" x2="9" y1="4" y2="20"/></svg>
        </button>
        <button class="[&>*]:pointer-events-none" on:click={() => insertSymbol('n')}>
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-underline"><path d="M6 4v6a6 6 0 0 0 12 0V4"/><line x1="4" x2="20" y1="20" y2="20"/></svg>
        </button>
        <button class="[&>*]:pointer-events-none" on:click={() => insertSymbol('m')}>
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-strikethrough"><path d="M16 4H9a3 3 0 0 0-2.83 4"/><path d="M14 12a4 4 0 0 1 0 8H6"/><line x1="4" x2="20" y1="12" y2="12"/></svg>
        </button>
        <button class="[&>*]:pointer-events-none" on:click={() => insertSymbol('k')}>
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-text-select"><path d="M5 3a2 2 0 0 0-2 2"/><path d="M19 3a2 2 0 0 1 2 2"/><path d="M21 19a2 2 0 0 1-2 2"/><path d="M5 21a2 2 0 0 1-2-2"/><path d="M9 3h1"/><path d="M9 21h1"/><path d="M14 3h1"/><path d="M14 21h1"/><path d="M3 9v1"/><path d="M21 9v1"/><path d="M3 14v1"/><path d="M21 14v1"/><line x1="7" x2="15" y1="8" y2="8"/><line x1="7" x2="17" y1="12" y2="12"/><line x1="7" x2="13" y1="16" y2="16"/></svg>
        </button>
        <button class="[&>*]:pointer-events-none ml-4" on:click={() => insertSymbol('r')}>
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-ban"><circle cx="12" cy="12" r="10"/><path d="m4.9 4.9 14.2 14.2"/></svg>
        </button>
        
    </div>

    <div class="grid grid-cols-1 sm:grid-cols-2 mb-2">
        <div class="h-full bg-transparent resize-none border-0 shadow-none outline-none border-none m-1 p-2 before:empty:content-[attr(placeholder)] before:empty:focus:content-[''] text-mc-light-gray" contenteditable="true" bind:innerHTML={text} on:paste={pasteHandler} on:input placeholder="Add your description here" />
        <div bind:this={previewDiv} class="font-minecraft p-2 bg-[#0f0110] -mb-2 rounded-b-md sm:rounded-bl-none">
            <p class="text-mc-aqua h5 font-minecraft">Music disc</p>
            {#if firstline != ''}
                <p>{firstline}</p>
            {/if}
            {@html output}
        </div>
    </div>
</div>

<div data-popup="colorPopup" class="variant-filled-primary p-2 rounded-md">
    <div class="grid grid-cols-4 gap-2 [&>*]:w-4 [&>*]:aspect-square [&>*]:cursor-pointer [&>*]:border [&>*]:border-transparent">
        <button class="bg-[#000000] hover:border-white" on:click={() => insertSymbol('0')} />
        <button class="bg-[#0000AA] hover:border-white" on:click={() => insertSymbol('1')} />
        <button class="bg-[#00AA00] hover:border-white" on:click={() => insertSymbol('2')} />
        <button class="bg-[#00AAAA] hover:border-white" on:click={() => insertSymbol('3')} />
        <button class="bg-[#AA0000] hover:border-white" on:click={() => insertSymbol('4')} />
        <button class="bg-[#AA00AA] hover:border-white" on:click={() => insertSymbol('5')} />
        <button class="bg-[#FFAA00] hover:border-white" on:click={() => insertSymbol('6')} />
        <button class="bg-[#AAAAAA] hover:border-white" on:click={() => insertSymbol('7')} />
        <button class="bg-[#555555] hover:border-white" on:click={() => insertSymbol('8')} />
        <button class="bg-[#5555FF] hover:border-white" on:click={() => insertSymbol('9')} />
        <button class="bg-[#55FF55] hover:border-white" on:click={() => insertSymbol('a')} />
        <button class="bg-[#55FFFF] hover:border-white" on:click={() => insertSymbol('b')} />
        <button class="bg-[#FF5555] hover:border-white" on:click={() => insertSymbol('c')} />
        <button class="bg-[#FF55FF] hover:border-white" on:click={() => insertSymbol('d')} />
        <button class="bg-[#FFFF55] hover:border-white" on:click={() => insertSymbol('e')} />
        <button class="bg-[#FFFFFF] hover:border-white" on:click={() => insertSymbol('f')} />
    </div>
</div>