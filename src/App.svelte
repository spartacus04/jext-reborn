<script lang="ts">
	import { Content, Popup, CSelect, Header } from '@lib';

	import { generate_btn, spinner } from '@assets';

  import { generatePack } from '@/generator';
	import { discStore } from '@/store';


	let packIcon : string;
	let packName : string;

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
		await generatePack($discStore, packIcon, packName, type == 'Merge');
		is_generating = false;
	};
</script>

<main>
	<Popup text={$discStore.length > 0 ? 'Can\'t generate discs while adding one' : 'Add at least a disc'} bind:active={popup_active}></Popup>

	<Header bind:packname={packName} bind:imagesrc={packIcon} />

	<Content />

	<div id="footer">
		{#if is_generating}
			<div id="generate_button" style="background-image: url({generate_btn});">
				<img src={spinner} alt="loading" height="32" width="32">
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
</style>
