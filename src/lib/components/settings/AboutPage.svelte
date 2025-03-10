<script lang="ts">
	import { external_link } from '$lib/assets';
	import { isTauri } from '$lib/state';
    import { version, arch, platform } from '@tauri-apps/plugin-os';
	import ThirdPartyLicensesModal from '$lib/components/modals/ThirdPartyLicensesModal.svelte';
	import { writeText } from '@tauri-apps/plugin-clipboard-manager';

	const fetchAbout = async () => {
		const response = await fetch('../about');
		return await response.json();
	};

    const copyToClipboard = async (text: string) => {
        if(isTauri) {
            await writeText(text);
        } else {
            await navigator.clipboard.writeText(text);
        }
    };

    let openJs: () => void;
    let openRust: () => void;
</script>


{#await fetchAbout() then data}
    <ThirdPartyLicensesModal bind:open={openJs} licenses={data.jsLicenses}/>
    <ThirdPartyLicensesModal bind:open={openRust} licenses={data.rustLicenses}/>
    <div class="flex p-4 w-fit gap-40 items-center text-white text-md">

        <div class="flex flex-col">
            <b class="text-lg mb-0.5">WebUI</b>
            <div class="flex flex-col gap-1 text-mc-light-gray border-b border-mc-light-gray pb-2">
                <p>{data.webuiVersion} - {data.commit}</p>
                <p>{data.date}</p>

                <button on:click={openJs} class="text-white border border-white pt-1 pb-2 px-5 font-bold hover:bg-[#5c5c5c] w-fit mt-2" on:click={openJs}>Third party licenses</button>
            </div>

            {#if isTauri}
                <b class="text-lg mb-0.5">Desktop app</b>
                <div class="flex flex-col gap-1 text-mc-light-gray">
                    <p>{data.desktopVersion}</p>
                    <p>{platform()} - {arch()} - {version()}</p>
                    <p>{data.date}</p>
                    <button on:click={openRust} class="text-white border border-white pt-1 pb-2 px-5 font-bold hover:bg-[#5c5c5c] w-fit mt-2">Third party licenses</button>
                </div>
            {/if}
        </div>
        <div class="flex flex-col gap-2">
            <button class="border border-white pt-1 pb-2 px-5 font-bold hover:bg-[#5c5c5c] w-fit mt-2" on:click={() => {
                if(isTauri) {
                    data['osVersion'] = `${platform()} ${arch()} - ${version()}`;
                } else {
                    delete data['desktopVersion'];
                }
                delete data['jsLicenses'];
                delete data['rustLicenses'];
                copyToClipboard(JSON.stringify(data, null, 4));
            }}>Copy data to clipboard</button>
            <b class="flex flex-row"><a class="underline" href="https://github.com/spartacus04/jext-reborn/issues/new?template=2-webui_bug_report.yml" target="_blank" rel="noopener noreferrer">Report a WebUI bug</a> <img src={external_link} class="ml-2 w-4" alt=""></b>
        </div>
    </div>
{/await}
