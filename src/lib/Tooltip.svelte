<script lang="ts">
    export let text;
    export let width = '120px';
	export let right = true;
</script>

<div id="container">
	{#if right}
    	<span id="tooltiptext" class="right" style="width: {width};">{@html text}</span>
    	<slot />
	{:else}
		<slot />
		<span id="tooltiptext" class="left" style="width: {width};">{@html text}</span>
	{/if}
</div>

<style lang="scss">
    #container {
        position: relative;
        display: inline-block;
        border: 0;
        padding: 0;
        margin: 0;

        #tooltiptext:hover {
            display: none;
        }

        #tooltiptext {
            visibility: hidden;
            height: 0;
            background-color: black;
            color: #fff;
            text-align: center;
            padding: 5px 0;
            border-radius: 6px;
            font-size: 1vw;

            position: absolute;
            z-index: 1;

            top: -5px;
        }

		.right {
            left: 105%;
		}

		.left {
			left: -110%;
		}
    }

    #container:hover #tooltiptext {
        visibility: visible;
        height: auto;
    }
</style>