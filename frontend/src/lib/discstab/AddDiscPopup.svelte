<script lang="ts">
    import { isServed } from '@/state';
import { DownloadFileTab, Popup, TabContainer, UploadFileTab } from '@lib';

    export let open = false;
    export let cb: (files: File[]) => void;

    const outp = (files?: File[]) => {
        if(files && cb) {
            open = false;
            cb(files);
        }
    };
</script>

<Popup bind:open>
    <div class="popupcontainer">
        {#if $isServed}
            <TabContainer items={[
                {
                    name: 'Upload file',
                    component: UploadFileTab
                },
                {
                    name: 'Download from URL',
                    component: DownloadFileTab,
                },
                {
                    name: 'Music packs',
                    component: null,
                }
            ]} data={outp}/>
        {:else}
            <TabContainer items={[
                {
                    name: 'Upload file',
                    component: UploadFileTab
                },
                {
                    name: 'Music packs',
                    component: null,
                }
            ]} data={outp}/>
        {/if}
        
    </div>
</Popup>

<style lang="scss">
    .popupcontainer {
        background-color: #202020;
        display: flex;
        flex-direction: column;

        height: 30em;
        width: 40em;
    }
</style>