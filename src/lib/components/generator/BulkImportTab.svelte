<script lang="ts">
	import { folder } from '$lib/assets';
	import { exists, readFile } from '@tauri-apps/plugin-fs';
	import LauncherButton from '../buttons/LauncherButton.svelte';
	import LauncherTextbox from '../inputs/LauncherTextbox.svelte';
	import { open } from '@tauri-apps/plugin-dialog';
	import { cAlert } from '$lib/utils';
	import type { BaseDisc } from '$lib/discs/baseDisc';
	import { NbsDisc } from '$lib/discs/nbsDisc';
	import { MusicDisc } from '$lib/discs/musicDisc';
	import { addDisc } from '$lib/discs/discManager';

	let value = '';

	const selectPath = async () => {
		const filePath = (await open({
			multiple: false,
			directory: false,
			title: 'Select a CSV index file',
			filters: [{ name: 'CSV files', extensions: ['csv'] }]
		})) as string | null;

		if (!filePath) return;

		value = filePath;
	};

	const parseCSV = async () => {
		if (!value) return;

		try {
			if (!(await exists(value))) {
				cAlert("Error: the file doesn't exist!");
				return;
			}
		} catch {
			cAlert("Error: the file doesn't exist!");
			return;
		}

		try {
			const file = await readFile(value);
			const text = new TextDecoder().decode(file).split('\n');

			cAlert('Importing the discs...');

			const regex = /(?:^|,)(?=[^"]|(")?)"?((\?(1)(?:[^"]|"")*|[^,"]*))"?(?=,|$)/g;

			for await (const line of text) {
				const matches = line.match(regex)!;

				const data = {
					filePath: matches[0],
					name: matches[1].slice(1),
					author: matches[2].slice(1),
					description: matches[3].slice(1),
					discTexturePath: matches[4].slice(1),
					fragmentTexturePath: matches[5].slice(1)
				};

				try {
					if (!(await exists(data.filePath))) {
						continue;
					}
				} catch {
					continue;
				}

				const binaryData = await readFile(data.filePath);
				const blob = new Blob([binaryData]);
				const file = new File([blob], data.filePath.split('/').pop()!);

				let disc: BaseDisc;

				if (file.name.endsWith('.nbs')) {
					disc = new NbsDisc(file, binaryData, true);
				} else {
					disc = new MusicDisc(file, true);
				}

				if (data.name.trim() !== '') {
					disc.title = data.name;
				}

				if (data.author.trim() !== '') {
					disc.author = data.author;
				}

				if (data.description.trim() !== '') {
					disc.tooltip = data.description;
				}

				if (data.discTexturePath.trim() !== '') {
					try {
						if (await exists(data.discTexturePath)) {
							const texture = await readFile(data.discTexturePath);
							disc.setDiscTexture(new Blob([texture]));
						}
					} catch {
						cAlert('Error: failed to load the disc texture!');
					}
				}

				if (data.fragmentTexturePath.trim() !== '') {
					try {
						if (await exists(data.fragmentTexturePath)) {
							const texture = await readFile(data.fragmentTexturePath);
							disc.setFragmentTexture(new Blob([texture]));
						}
					} catch {
						cAlert('Error: failed to load the fragment texture!');
					}
				}

				addDisc(disc);
			}

			cAlert('Successfully imported the discs!');
		} catch {
			cAlert('Error: failed to parse the CSV file!');
		}
	};
</script>

<div class="flex flex-col items-center justify-center w-full h-full gap-4 text-white">
	<div class="flex flex-col w-[calc(90%)]">
		<b class="text-[#aeaeae] text-xs h-min"
			>CSV File (<a
				href="https://spartacus04.github.io/jext-reborn/docs/bulk-import/"
				target="_blank"
				class="text-blue-400">HELP</a
			>)</b
		>
		<div class="flex items-center">
			<LauncherTextbox
				placeholder="CSV indexing file"
				classes="w-full overflow-x-auto"
				bind:value
			/>
			<button class="w-7 h-7 -ml-8 hover:bg-gray-700 rounded" on:click={selectPath}>
				<img src={folder} class="ml-0.5 w-6 h-6" alt="text" />
			</button>
		</div>
	</div>
	<LauncherButton text="Import CSV" type="primary" on:click={parseCSV} />
</div>
