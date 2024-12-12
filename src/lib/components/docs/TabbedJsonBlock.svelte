<script lang="ts">
    export let tabs: string[];
    export let tabsContents: string[];
    export let currentTab: string = tabs[0];

    import hljs from 'highlight.js/lib/core';
    import json from 'highlight.js/lib/languages/json';

    hljs.registerLanguage('json', json);
</script>

<div class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia w-full text-white h-min">
    <div class="flex justify-start space-x-2 border-b border-b-white">
        {#each tabs as tab}
            <button
                class="px-4 py-2 bg-[#2d2d2d] text-white rounded-t-lg {currentTab === tab ? 'bg-[#3d3d3d]' : ''}"
                on:click={() => currentTab = tab}
            >
                {tab}
            </button>
        {/each}
    </div>
    {#key currentTab}
        <pre><code class="language-{currentTab}">{@html hljs.highlight(tabsContents[tabs.indexOf(currentTab)], { language: 'json' }).value}</code></pre>
    {/key}
</div>