<script lang="ts">
    import { Textbox, SecondaryButton } from '@lib';
    
    export let value = '';
    // TODO: implement server requests
</script>

<div class="container">
    <Textbox placeholder="Youtube/Soundcloud/Spotify Url" bind:value/>
    <SecondaryButton>Download</SecondaryButton>
</div>

<style lang="scss">
    .container {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;

        height: 100%;
        width: 100%;
    }
</style>