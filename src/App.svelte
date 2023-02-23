<script lang="ts">
	import { Content, Popup, CSelect, Header } from '@lib';
	import { generate_btn, spinner } from '@assets';

	import { currentCount, generatePack, totalCount } from '@/generator';
	import { discStore } from '@/store';


	let popup_active = false;
	let type = 'Generate';

	let can_generate = false;

	let is_generating = false;


	discStore.subscribe(discs => {
		can_generate = discs.every(disc => disc.texture != null) && discs.length > 0;
	});

	const generate = async () => {
		if(is_generating) return;

		is_generating = true;
		await generatePack(type == 'Merge');
		is_generating = false;
	};
</script>

<main>
	<Popup text={$discStore.length > 0 ? 'Can\'t generate discs while adding one' : 'Add at least a disc'} bind:active={popup_active}></Popup>

	<Header />

	<Content />

	<div id="footer">
		{#if is_generating}
			<div id="generate_button" style="background-image: url({generate_btn});">
				<img src={spinner} alt="loading" height="28" width="28">
				<div id="progressbar">
					<div id="progress" style:width="calc(100% / {$totalCount} * {$currentCount})"></div>
				</div>
			</div>
		{:else}
			<div id="generate_button" style:background-image="url({generate_btn})" class={can_generate ? '' : 'grayscale'} on:click={can_generate ? generate : () => popup_active = true} on:keydown={null} />
			<CSelect options={['Generate', 'Merge']} bind:selected={type} />
		{/if}

	</div>
</main>

<style lang="scss">
	@import 'styles/crisp.scss';

	main {
		height: 100%;
		width: 100%;
		background-color: #303030;
		padding: 0;
		display: flex;
		flex-direction: column;
		justify-content: center;

		#footer {
			display: flex;
			align-items: center;
			justify-content: center;
			padding: 1em;
			background-color: #202020;
			padding: 0.5em;

			#generate_button {

				width: 256px;
				height: 64px;
				display: flex;
				flex-direction: column;
				align-items: center;
				justify-content: center;

				cursor: pointer;
				background-repeat: no-repeat;
				background-position: center;
				background-size: cover;

				@extend %crisp;

				&:hover:not(.grayscale) {
					filter: drop-shadow(1px 1px 5px green) contrast(130%);
				}
			}
		}
	}

	#progressbar {
		width: 100px;
		height: 2px;
		margin: 2px 0px 2px 0px;
		background-color: #303030;
		padding: 0;
	}

	#progress {
		height: 100%;
		background-color: white;
		margin: 0;
	}
</style>
