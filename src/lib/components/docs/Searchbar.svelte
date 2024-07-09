<script lang="ts">
	export let query: string;

	let input: HTMLInputElement;

	const focus = (e: MouseEvent) => {
		const id = (e.target as HTMLElement).id;

		console.debug(id);

		if (id === 'reset') {
			query = '';
		}

		input.focus();
	};
</script>

<button class="flex w-full h-8 items-center mt-1 cursor-default" on:click={focus}>
	<svg
		xmlns="http://www.w3.org/2000/svg"
		class="ml-1 h-6 w-6 p-0.5 z-[5] text-[white]"
		fill="none"
		viewBox="0 0 24 24"
		stroke="currentColor"
	>
		<path
			stroke-linecap="round"
			stroke-linejoin="round"
			stroke-width="1"
			d="M21 21l-5-5m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
		/>
	</svg>
	<input
		bind:this={input}
		bind:value={query}
		type="text"
		class="-ml-7 w-full px-8 pt-0.5 pb-1 text-sm text-white bg-[#0e0e0e] border-2 border-[#0dd166] rounded-sm"
		placeholder="What do you seek?"
	/>
	{#if query != ''}
		<button class="hover:bg-white h-4 w-4 rounded-sm hover:bg-opacity-10 p-0.5 -ml-5" id="reset">
			<svg
				xmlns="http://www.w3.org/2000/svg"
				class="pointer-events-none h-6 w-6 p-0.5 z-10 -mt-1.5 -ml-1.5 text-[white]"
				fill="none"
				viewBox="0 0 24 24"
				stroke="currentColor"
			>
				<path
					stroke-linecap="round"
					stroke-linejoin="round"
					stroke-width="1"
					d="M6 18L18 6M6 6l12 12"
				/>
			</svg>
		</button>
	{/if}
</button>
