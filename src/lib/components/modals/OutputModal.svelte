<script lang="ts">
	import { fade } from 'svelte/transition';
	import ForgeProgressBar from '../inputs/ForgeProgressBar.svelte';
	import {
		default_icon,
		panorama_0 as panorama_front,
		panorama_1 as panorama_right,
		panorama_2 as panorama_back,
		panorama_3 as panorama_left,
		panorama_4 as panorama_top,
		panorama_5 as panorama_bottom
	} from '$lib/assets';
	import { exporterSteps, exportResourcePack } from '$lib/exporter/exporterLine';
	import { PluginExporter } from '$lib/exporter/pluginExporter';
	import { get } from 'svelte/store';
	import { ResourcePackData } from '$lib/discs/resourcePackManager';
	import * as THREE from 'three';
	import MinecraftButton from '../buttons/MinecraftButton.svelte';
	import type { ExporterOuput } from '$lib/exporter/baseExporter';
	import { saveAs } from '$lib/utils';
	import { isTauri } from '$lib/state';
	import { pluginConnectorStore } from '$lib/pluginAccess/pluginConnector';

	export let loaded = false;
	export let onFinish: () => unknown;

	let dialog: HTMLDialogElement;

	export const open = () => dialog.show();
	export const close = () => dialog.close();
	export const isOpen = () => dialog.open;
	export const toggle = () => (dialog.open ? close() : open());
	export const openModal = () => dialog.showModal();

	exportResourcePack(new PluginExporter()).then((result) => {
		loaded = true;
		setTimeout(() => {
			loadPanorama();
		}, 200);

		output = result;
	});

	let output: ExporterOuput | undefined = undefined;

	let canvas: HTMLDivElement;

	const scene = new THREE.Scene();
	const camera = new THREE.PerspectiveCamera(95, window.innerWidth / window.innerHeight, 0.1, 50);

	camera.up.set(-1, 0, 0);

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

	materials.forEach((material) => {
		material.needsUpdate = true;
	});

	renderer.toneMapping = THREE.ReinhardToneMapping;
	renderer.toneMappingExposure = 0.1;

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

	window.onresize = () => {
		camera.aspect = window.innerWidth / window.innerHeight;
		camera.updateProjectionMatrix();
		renderer.setSize(window.innerWidth, window.innerHeight);
		renderer.render(scene, camera);
	};

	let idleUpload = true;

	const uploadAssets = async () => {
		if(output && $pluginConnectorStore) {
			idleUpload = false;

			await $pluginConnectorStore.applyDiscs(output.javaRP).catch(() => null);

			if(output.bedrockRP) {
				await $pluginConnectorStore.applyDiscsGeyser(output.bedrockRP).catch(() => null);
			}

			idleUpload = true;
		}
	}
</script>

<dialog
	bind:this={dialog}
	class="h-screen overflow-y-hidden w-screen flex items-center justify-center flex-col backdrop:bg-white {output
		? 'z-10'
		: ''}"
>
	{#if loaded}
		<div in:fade={{ duration: 500 }} bind:this={canvas} class="w-full fixed h-full z-20" />
		<div
			in:fade={{ duration: 500 }}
			class="w-full fixed h-full z-30 flex items-center justify-center gap-4 flex-col"
		>
			<h1
				class="text-4xl sm:text-7xl mt-2 font-minecraft-title text-center text-white"
				style="text-shadow:
			3px 3px 0 #000,
			-3px 3px 0 #000,
			-3px -3px 0 #000,
			3px -3px 0 #000;"
			>
				Your resourcepack is ready
			</h1>
			<div class="flex gap-4 flex-col w-[75%] sm:w-[50%]">
				{#if isTauri && $pluginConnectorStore}
					<MinecraftButton
						bind:enabled={idleUpload}
						on:click={uploadAssets}
					>
						{!idleUpload ? 'Uploading Resource Packs(s)...' : 'Apply Resource Pack(s) automatically'}
					</MinecraftButton>
				{/if}
				<MinecraftButton
					on:click={() => saveAs(output?.javaRP, `${get(ResourcePackData).name}.zip`)}
					flex={true}>Download Resource Pack for Minecraft: Java Edition</MinecraftButton
				>
				<MinecraftButton
					enabled={output?.bedrockRP != undefined}
					on:click={() =>
						saveAs(output?.bedrockRP, `${get(ResourcePackData).name}-geysermc.mcpack`)}
				>
					Download Resource Pack for GeyserMC
				</MinecraftButton>
				<MinecraftButton
					on:click={() => {
						close();
						onFinish();
					}}>Back to disc generation</MinecraftButton
				>
			</div>
		</div>
	{:else}
		<div class="w-full fixed h-full flex flex-col items-center justify-center">
			<div class="xl:w-[50%] lg:w-[60%] w-[100%] flex flex-col items-center justify-center p-4">
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

				{#if $exporterSteps[0] != undefined}
					<p
						class="font-minecraft text-2xl text-black text-left w-full overflow-hidden whitespace-nowrap text-ellipsis"
					>
						{$exporterSteps[0].status}
					</p>
					<ForgeProgressBar
						bind:max={$exporterSteps[0].total}
						bind:value={$exporterSteps[0].current}
					/>
				{:else}
					<p class="font-minecraft text-2xl text-white text-left w-full select-none">&nbsp;</p>
					<ForgeProgressBar max={0} value={0} invisible={true} />
				{/if}

				{#if $exporterSteps[1] != undefined}
					<p
						class="font-minecraft text-2xl text-black text-left w-full overflow-hidden whitespace-nowrap text-ellipsis"
					>
						{$exporterSteps[1].status}
					</p>
					<ForgeProgressBar
						bind:max={$exporterSteps[1].total}
						bind:value={$exporterSteps[1].current}
					/>
				{:else}
					<p class="font-minecraft text-2xl text-white text-left w-full select-none">&nbsp;</p>
					<ForgeProgressBar max={0} value={0} invisible={true} />
				{/if}

				{#if $exporterSteps[2] != undefined}
					<p
						class="font-minecraft text-2xl text-black text-left w-full overflow-hidden whitespace-nowrap text-ellipsis"
					>
						{$exporterSteps[2].status}
					</p>
					<ForgeProgressBar
						bind:max={$exporterSteps[2].total}
						bind:value={$exporterSteps[2].current}
					/>
				{:else}
					<p class="font-minecraft text-2xl text-white text-left w-full select-none">&nbsp;</p>
					<ForgeProgressBar max={0} value={0} invisible={true} />
				{/if}
			</div>
		</div>
	{/if}
</dialog>
