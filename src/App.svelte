<script lang="ts">
    import Content from "./lib/Content.svelte";
    import Header from "./lib/Header.svelte";

	import generate_btn from './assets/generate_btn.png'
</script>

<main>
  	
	<Header />

	<Content />

	<div id="footer">
		<div id="generate_button" style="background-image: url({generate_btn});">
			<p id="generate_text" class="noselect">GENERATE</p>
		</div>
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

				#generate_text {
					font-family: 'minecraft';
					color: white;
					font-size: 2em;
					padding: 0;
					margin: 0;

					transition: ease-in-out all 0.4s;
				}
			}
		}
	}
</style>