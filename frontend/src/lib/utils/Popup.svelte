<script lang="ts">
    export let open = false;

    document.addEventListener('keydown', (e) => {
        if(e.key === 'Escape') {
            open = false;
        }
    });
</script>

{#if open}
    <div class="background" on:click={() => open = false} />

    <div class="popup">
        <slot />
    </div>
{/if}

<style lang="scss">
    .background {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;

        background-color: rgba(0, 0, 0, 0.5);

        z-index: 1000;
    }

    .popup {
        z-index: 1001;

        display: flex;
        align-items: center;
        justify-content: center;

        position: absolute;
        left: 50%;
        top: 50%;
        height: min-content;
        width: min-content;
        transform: translate(-50%, -50%);
        padding: 0;
        margin: 0;
    }
</style>