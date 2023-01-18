<script lang="ts">
	import Arrow from '../assets/arrow.png';

	export let options: string[];
	export let selected = options[0];

	let open = false;

	const toggleMenu = () => open = !open;

	const select = (ele : string) => {
	    selected = ele;
	    open = false;
	};
</script>

{#if open}
	<div id="openmenu">
		{#each options as option}
			<div class="selectopt" on:click={() => select(option)} on:keydown={null}>{option}</div>
		{/each}
	</div>
{/if}

<div id="container">
	<div id="row">
		<div id="selectedoption">
			{selected}
		</div>
		<div id="arrowcontainer" on:click={toggleMenu} on:keydown={null}>
			<img src="{Arrow}" alt="🢓" id="arrow" class={open ? 'active' : ''}>
		</div>
	</div>
</div>

<style lang="scss">
	@import '../styles/crisp.scss';

	#container {
		width: 226px;
		height: 44px;
		padding: 10px;
		max-height: 44px;

		margin-left: -256px;
		z-index: 2;

		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: space-between;
		pointer-events: none;
	}

	#arrow {
		@extend %crisp;

		height: 12px;
		user-select: none;
		transform: rotate(180deg);

		&.active {
			transform: rotate(0deg);
		}
	}

	#arrowcontainer {
		height: 32px;
		width: 32px;
		display: flex;
		justify-content: center;
		align-items: center;
		border: 1px solid transparent;
		background-color: rgba($color: black, $alpha: 0);

		&:hover {
			cursor: pointer;
			background-color: rgba($color: black, $alpha: 0.2);

			border: 1px solid #484848;
		}

		pointer-events: all;
	}

	#selectedoption {
		flex: 1;
		text-align: center;
		font-family: 'minecraft';
		color: white;
		font-size: 2em;
		padding: 0;
		margin: 0;

		transition: ease-in-out all 0.4s;
	}

	#row {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
	}

	#openmenu {
		position: relative;
		width: 256px;
		margin-left: -256px;
		display: flex;
		flex-direction: column;
		align-items: center;
		margin-bottom: -68px;

		top: -132px;
	}

	.selectopt {
		font-family: 'Minecraft';

		text-align: center;
		font-family: 'minecraft';
		color: white;
		font-size: 2em;

		border: 1px solid black;
		width: 216px;
		height: 24px;
		padding: 20px;

		background-color: #404040;
		pointer-events: all;

		user-select: none;

		&:hover {
			background-color: #606060;
			cursor: pointer;
		}
	}
</style>