<script lang="ts">
	import {
		panorama_0 as panorama_front,
		panorama_1 as panorama_right,
		panorama_2 as panorama_back,
		panorama_3 as panorama_left,
		panorama_4 as panorama_top,
		panorama_5 as panorama_bottom,
		default_icon
	} from '$lib/assets';

	import { getModalStore } from '@skeletonlabs/skeleton';
	import { fade } from 'svelte/transition';
	import { writable } from 'svelte/store';
	import * as THREE from 'three';

	import { MinecraftButton, ForgeProgressBar } from '.';

	import { outputEverything, saveAs, fetchAuthed, isLoggedIn } from '../';

	const modalStore = getModalStore();

	let canvas: HTMLDivElement;

	const scene = new THREE.Scene();
	const camera = new THREE.PerspectiveCamera(85, window.innerWidth / window.innerHeight, 0.1, 50);

	const renderer = new THREE.WebGLRenderer();
	renderer.setSize(window.innerWidth, window.innerHeight);

	// render 6 sides forming a cube, each side should have a panorama_* texture, backside
	const geometry = new THREE.BoxGeometry(10, 10, 10);
	const materials = [
		new THREE.MeshBasicMaterial({
			map: new THREE.TextureLoader().load(panorama_right),
			side: THREE.BackSide
		}),
		new THREE.MeshBasicMaterial({
			map: new THREE.TextureLoader().load(panorama_left),
			side: THREE.BackSide
		}),
		new THREE.MeshBasicMaterial({
			map: new THREE.TextureLoader().load(panorama_top),
			side: THREE.BackSide
		}),
		new THREE.MeshBasicMaterial({
			map: new THREE.TextureLoader().load(panorama_bottom),
			side: THREE.BackSide
		}),
		new THREE.MeshBasicMaterial({
			map: new THREE.TextureLoader().load(panorama_front),
			side: THREE.BackSide
		}),
		new THREE.MeshBasicMaterial({
			map: new THREE.TextureLoader().load(panorama_back),
			side: THREE.BackSide
		})
	];

	const cube = new THREE.Mesh(geometry, materials);

	cube.position.set(0, 0, 0);

	scene.add(cube);

	const animate = () => {
		if (!canvas) return;

		setTimeout(() => {
			requestAnimationFrame(animate);
		}, 1000 / 60);

		cube.rotation.y += 0.0005;

		renderer.render(scene, camera);
	};

	const loadPanorama = () => {
		canvas.appendChild(renderer.domElement);

		animate();
	};

	const progressStore = writable([
		{
			total: 3,
			current: 0,
			status: 'Loading FFmpeg'
		},
		undefined,
		undefined
	]);

	let javaRp: Blob | undefined = undefined;
	let bedrockRp: Blob | undefined = undefined;

	outputEverything(
		(rp) => {
			setTimeout(() => {
				javaRp = rp;
				setTimeout(() => {
					loadPanorama();
				}, 1);
			}, 500);
		},
		(total, current, status, index) => {
			progressStore.update((store) => {
				if (total == undefined || current == undefined || status == undefined)
					store[index] = undefined;
				else store[index] = { total, current, status };

				return store;
			});
		},
		(rp) => {
			bedrockRp = rp;
		}
	);

	const applyToServer = async () => {
		try {
			const javaResponse = await fetchAuthed('discs/apply', {
				method: 'POST',
				body: await javaRp?.arrayBuffer()
			});

			if (javaResponse.status != 200) {
				throw new Error('Could not apply resourcepack');
			}

			const bedrockResponse = await fetchAuthed('discs/applygeyser', {
				method: 'POST',
				body: await bedrockRp?.arrayBuffer()
			});

			if (![200, 404].includes(bedrockResponse.status)) {
				throw new Error('Could not apply resourcepack');
			}

			alert('Resourcepack applied!');
		} catch (e) {
			alert(e);
		}
	};

	window.onresize = () => {
		camera.aspect = window.innerWidth / window.innerHeight;
		camera.updateProjectionMatrix();
		renderer.setSize(window.innerWidth, window.innerHeight);
	};

	const getDesktopAppDownload = (async () => {
		const response = await fetch(
			'https://api.github.com/repos/spartacus04/jext-reborn/actions/workflows/build-tauri.yml/runs?status=success&per_page=1'
		);

		const json = await response.json();

		if (json.total_count == 0) return;

		return json.workflow_runs[0].html_url;
	})();
</script>

<main
	in:fade
	class="bg-white h-screen overflow-y-auto w-screen flex items-center justify-center gap-4 flex-col p-4 {javaRp
		? 'z-10'
		: ''}"
>
	{#if javaRp}
		<div in:fade={{ duration: 500 }} bind:this={canvas} class="w-full fixed h-full z-20" />
		<div
			in:fade={{ duration: 500 }}
			class="w-full fixed h-full z-30 flex items-center justify-center gap-4 flex-col"
		>
			<h1
				class="h1 sm:text-7xl mt-2 font-minecraft-title text-center shadow-lg"
				style="text-shadow:
            3px 3px 0 #000,
            -3px 3px 0 #000,
            -3px -3px 0 #000,
            3px -3px 0 #000;"
			>
				Your resourcepack is ready
			</h1>
			<div class="flex gap-4 flex-col w-[75%] sm:w-[50%]">
				{#if isLoggedIn()}
					<MinecraftButton on:click={applyToServer}>Apply to your JEXT server!</MinecraftButton>
				{/if}
				<MinecraftButton on:click={() => saveAs(javaRp, 'resourcepack.zip')}
					>Download Resource Pack for Minecraft: Java Edition</MinecraftButton
				>
				<MinecraftButton
					enabled={bedrockRp != undefined}
					on:click={() => saveAs(bedrockRp, 'resourcepack-geysermc.mcpack')}
				>
					{#if bedrockRp == undefined}
						Generating Resource Pack for GeyserMC...
					{:else}
						Download Resource Pack for GeyserMC
					{/if}
				</MinecraftButton>
				<MinecraftButton on:click={() => modalStore.close()}
					>Back to disc generation</MinecraftButton
				>
			</div>
		</div>
	{/if}
	<div class="w-full fixed h-full flex flex-col items-center justify-center p-2">
		<div class="xl:w-[50%] lg:w-[60%] w-[100%] flex flex-col items-center justify-center p-2">
			<div class="flex items-center justify-center gap-4 flex-col sm:flex-row mb-4">
				<img src={default_icon} alt="jext-icon" class="aspect-square h-32 sm:h-full" />
				<h1
					class="font-minecraft-launcher text-black h1 text-6xl {window.__TAURI__
						? 'text-nowrap'
						: ''} md:text-7xl sm:text-nowrap sm:text-8xl text-center"
				>
					JEXT Reborn
				</h1>
			</div>
			{#if !window.__TAURI__}
				<h4 class="h4 bg-red-800 p-2 rounded-lg font-minecraft">
					Warning! The website may take a while to generate the resourcepack!<br />Consider using
					the desktop app
					{#await getDesktopAppDownload}
						<p></p>
					{:then url}
						{#if url != null}
							(<a href={url} target="_blank" rel="noopener noreferrer" class="underline">Here</a>)
						{/if}
					{/await}
				</h4>
			{/if}

			{#if $progressStore[0] != undefined}
				<p class="font-minecraft text-2xl text-black text-left w-full">
					{$progressStore[0].status}
				</p>
				<ForgeProgressBar
					bind:max={$progressStore[0].total}
					bind:value={$progressStore[0].current}
				/>
			{:else}
				<p class="font-minecraft text-2xl text-white text-left w-full select-none">&nbsp;</p>
				<ForgeProgressBar max={0} value={0} invisible={true} />
			{/if}

			{#if $progressStore[1] != undefined}
				<p class="font-minecraft text-2xl text-black text-left w-full">
					{$progressStore[1].status}
				</p>
				<ForgeProgressBar
					bind:max={$progressStore[1].total}
					bind:value={$progressStore[1].current}
				/>
			{:else}
				<p class="font-minecraft text-2xl text-white text-left w-full select-none">&nbsp;</p>
				<ForgeProgressBar max={0} value={0} invisible={true} />
			{/if}

			{#if $progressStore[2] != undefined}
				<p class="font-minecraft text-2xl text-black text-left w-full">
					{$progressStore[2].status}
				</p>
				<ForgeProgressBar
					bind:max={$progressStore[2].total}
					bind:value={$progressStore[2].current}
				/>
			{:else}
				<p class="font-minecraft text-2xl text-white text-left w-full select-none">&nbsp;</p>
				<ForgeProgressBar max={0} value={0} invisible={true} />
			{/if}
		</div>
	</div>
</main>
