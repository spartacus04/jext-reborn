<script lang="ts">
    export let text;
</script>

<div id="container">
    <span id="tooltiptext">{text}</span>
    <slot />
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
            width: 120px;
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
            left: 105%;
        }
    }

    #container:hover #tooltiptext {
        visibility: visible;
        height: auto;
    }
</style>