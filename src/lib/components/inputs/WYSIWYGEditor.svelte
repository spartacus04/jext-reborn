<script lang="ts">
	import MCText from 'minecraft-text-js';
	import { onDestroy } from 'svelte';

	import { slide } from 'svelte/transition';

	export let text = '';
	export let firstline = '';
	let output = '';

	let previewDiv: HTMLDivElement;

	let timeout = setTimeout(() => MCText.refeashObfuscate(previewDiv), 1);

	// Workaround for library handling & when not necessary and not handling new line
	$: typeof text == 'string' && (output = MCText.toHTML(
		text
			.replace('<div>', '')	// replace first div to avoid empty line at the beginning
			.replaceAll('&', '')	// remove all & to avoid double escaping
			.replaceAll('<div>', '\\n')
			.replaceAll('</div>', '')
			.replaceAll('<br>', '')
	).replaceAll('amp;', '&')) && (() => {
		clearTimeout(timeout);
		timeout = setTimeout(() => MCText.refeashObfuscate(previewDiv), 1);
	})();

	const pasteHandler = (e: ClipboardEvent | Event) => {
		e.preventDefault();
		const text =
			((e as any).originalEvent || (e as ClipboardEvent)).clipboardData?.getData('text/plain') ||
			'';
		document.execCommand('insertText', false, text);
	};

	onDestroy(() => clearTimeout(timeout));

	const insertSymbol = (symbol: string) => {
		document.execCommand('insertText', false, `§${symbol}`);
		// set cursor position to the end of the inserted symbol
		const range = document.getSelection()?.getRangeAt(0);
		const selection = document.getSelection();
		if (range && selection) {
			range.setStart(range.endContainer, range.endOffset);
			range.setEnd(range.endContainer, range.endOffset);
			selection.removeAllRanges();
			selection.addRange(range);
		}

		// trigger input event to update the preview
		const inputEvent = new Event('input', { bubbles: true });
		previewDiv.dispatchEvent(inputEvent);
	};

	let colorPickerOpen = false;
</script>

<div class="bg-[#0e0e0e] flex h-min flex-col border-[#0dd166] border-2 focus-within:border-white">
	<div class="h-8 m-2 mb-0 pb-2 border-b border-mc-light-gray flex items-center gap-2">
		<button class="[&>*]:pointer-events-none mr-4 mb-[2px]" on:click={() => colorPickerOpen = !colorPickerOpen}>
			<svg
				xmlns="http://www.w3.org/2000/svg"
				width="24"
				height="24"
				viewBox="0 0 24 24"
				fill="none"
				stroke="white"
				stroke-width="2"
				stroke-linecap="round"
				stroke-linejoin="round"
				class="lucide lucide-palette"
				><path d="M11 2h2v2h2v4h2v4h2v6h-2v2h-2v2H9v-2H7v-2H5v-6h2V8h2V4h2V2z" fill="white"/>
			</svg>
		</button>

		<button class="[&>*]:pointer-events-none" on:click={() => insertSymbol('l')}>
			<b class="font-minecraft text-white text-3xl">B</b>
		</button>

		<button class="[&>*]:pointer-events-none" on:click={() => insertSymbol('o')}>
			<i class="font-minecraft text-white text-3xl">I</i>
		</button>

		<button class="[&>*]:pointer-events-none" on:click={() => insertSymbol('n')}>
			<u class="font-minecraft text-white text-3xl">U</u>
		</button>

		<button class="[&>*]:pointer-events-none" on:click={() => insertSymbol('m')}>
			<s class="font-minecraft text-white text-3xl">S</s>
		</button>
		<button class="[&>*]:pointer-events-none mb-[2px]" on:click={() => insertSymbol('k')}>
			<svg
				xmlns="http://www.w3.org/2000/svg"
				width="24"
				height="24"
				viewBox="0 0 24 24"
				fill="none"
				stroke="white"
				stroke-width="2"
				stroke-linecap="round"
				stroke-linejoin="round"
				class="lucide lucide-palette"
			>
			<path d="M2 2h20v20H2V2zm2 2v4h4v4H4v4h4v4h4v-4h4v4h4v-4h-4v-4h4V8h-4V4h-4v4H8V4H4zm8 8H8v4h4v-4zm0-4v4h4V8h-4z" fill="white"/>
			</svg>
		</button>
		<button class="[&>*]:pointer-events-none ml-4" on:click={() => insertSymbol('r')}>
			<p class="font-minecraft text-white text-3xl">X</p>
		</button>
	</div>

	{#if colorPickerOpen}
		<div
			class="m-2 mb-0 pb-2 border-b border-mc-light-gray flex items-center gap-2 flex-wrap [&>*]:w-4 [&>*]:aspect-square [&>*]:cursor-pointer [&>*]:border [&>*]:border-transparent"
			transition:slide={{ duration: 200 }}	
		>
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
	{/if}

	<div class="grid grid-cols-1 sm:grid-cols-2 mb-2">
		<div
			class="overflow-x-auto h-full bg-transparent resize-none border-0 shadow-none outline-none border-none m-1 p-2 before:empty:content-[attr(placeholder)] before:empty:focus:content-[''] text-mc-light-gray"
			contenteditable="true"
			bind:innerHTML={text}
			on:paste={pasteHandler}
			on:input
			placeholder="Add your description here"
		/>
		<div
			bind:this={previewDiv}
			class="overflow-x-auto font-minecraft text-white p-2 bg-[#0f0110] -mb-2 rounded-b-md sm:rounded-bl-none"
		>
			<p class="text-mc-aqua h5 font-minecraft">Music disc</p>
			{#if firstline != ''}
				<p>{firstline}</p>
			{/if}
			{@html output}
		</div>
	</div>
</div>