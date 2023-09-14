<script lang="ts">
	import { inputFile } from "@ui";

    export let data: (files ?: File[]) => void
</script>

<div class="addsongsbtn" use:inputFile={{ accept: import.meta.env.PROD ? 'audio/*' : '.ogg', cb: data, multiple: true, drag: true }}>
    <h1>+</h1>
</div>

<style lang="scss">
    .addsongsbtn {
		background-color: #484848;
		height: 100%;
		margin: 1em;
		display: flex;
		align-items: center;
		justify-content: center;
		border: 2px solid black;

		cursor: pointer;

		h1 {
			color: #d3d3d3;
			user-select: none;
		}

		&:hover {
			background-color: #404040;
		}
	}
</style>