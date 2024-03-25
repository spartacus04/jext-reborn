<script lang="ts">
    import { CodeBlock, TabGroup, Tab } from '@skeletonlabs/skeleton'

    let responses = new Array(9).fill(0);
</script>

<h1 class="h1">REST Api</h1>

<p>The REST API is used to interact with the JEXT server. It is used to upload discs, apply resourcepacks and more.</p>
<p>Here is a list of all available endpoints:</p>

<h3 class="h3 mt-4">Auth:</h3>

<div class="p-5 bg-surface-500 rounded-lg mt-3">
    <h4 class="h4"><b class="text-red-500">POST</b> /connect/</h4>

    <blockquote class="blockquote">Connects to the JEXT server</blockquote>
    
    <h5 class="h5 mt-4">Request</h5>
    
    <p>Body: </p>
    <CodeBlock
        class="border border-mc-light-gray rounded-lg m-4"
        language="http"
        rounded="true"
        code={`
SuperSecretJextPassword`} />

    <h5 class="h5 mt-4">Response</h5>

    <TabGroup>
        <Tab bind:group={responses[0]} name="200" value={0}>200</Tab>
        <Tab bind:group={responses[0]} name="401" value={1}>401</Tab>

        <svelte:fragment slot="panel">
            {#if responses[0] === 0}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="text"
                    rounded="true"
                    code={`
TheSuperSecretJextBearerToken`} />
            {:else if responses[0] === 1}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="text"
                    rounded="true"
                    code={`
401 Unauthorized`} />
            {/if}
        </svelte:fragment>
    </TabGroup>
</div>

<div class="p-5 bg-surface-500 rounded-lg mt-3">
    <h4 class="h4"><b class="text-red-500">POST</b> /disconnect/</h4>

    <blockquote class="blockquote">Disconnects from the JEXT server</blockquote>
    
    <h5 class="h5 mt-4">Request</h5>
    
    <p>Headers: </p>
    <CodeBlock
        class="border border-mc-light-gray rounded-lg m-4"
        language="http"
        rounded="true"
        code={`
Authorization: Bearer TheSuperSecretJextBearerToken`} />

    <h5 class="h5 mt-4">Response</h5>

    <TabGroup>
        <Tab bind:group={responses[1]} name="200" value={0}>200</Tab>
        <Tab bind:group={responses[1]} name="401" value={1}>401</Tab>

        <svelte:fragment slot="panel">
            {#if responses[1] === 0}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="text"
                    rounded="true"
                    code={`
(empty)`} />
            {:else if responses[1] === 1}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="text"
                    rounded="true"
                    code={`
401 Unauthorized`} />
            {/if}
        </svelte:fragment>
    </TabGroup>
</div>

<div class="p-5 bg-surface-500 rounded-lg mt-3">
    <h4 class="h4"><b class="text-blue-500">GET</b> /health/</h4>

    <blockquote class="blockquote">Checks the health of the JEXT server, if a bearer token is set it'll 401 when it's not registered</blockquote>
    
    <h5 class="h5 mt-4">Request</h5>
    
    <p>Headers: </p>
    <CodeBlock
        class="border border-mc-light-gray rounded-lg m-4"
        language="http"
        rounded="true"
        code={`
Authorization: Bearer TheSuperSecretJextBearerToken (optional)`} />

    <h5 class="h5 mt-4">Response</h5>

    <TabGroup>
        <Tab bind:group={responses[2]} name="200" value={0}>200</Tab>
        <Tab bind:group={responses[2]} name="401" value={1}>401</Tab>

        <svelte:fragment slot="panel">
            {#if responses[2] === 0}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="text"
                    rounded="true"
                    code={`
(empty)`} />
            {:else if responses[2] === 1}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="text"
                    rounded="true"
                    code={`
401 Unauthorized`} />
            {/if}
        </svelte:fragment>
    </TabGroup>
</div>

<h3 class="h3 mt-4">Config:</h3>

<div class="p-5 bg-surface-500 rounded-lg mt-3">
    <h4 class="h4"><b class="text-blue-500">GET</b> /config/read/</h4>

    <blockquote class="blockquote">Reads the current config, including key, description, value, default value and displayname</blockquote>
    
    <h5 class="h5 mt-4">Request</h5>
    
    <p>Headers: </p>
    <CodeBlock
        class="border border-mc-light-gray rounded-lg m-4"
        language="http"
        rounded="true"
        code={`
Authorization: Bearer TheSuperSecretJextBearerToken`} />

    <h5 class="h5 mt-4">Response</h5>

    <TabGroup>
        <Tab bind:group={responses[3]} name="200" value={0}>200</Tab>
        <Tab bind:group={responses[3]} name="401" value={1}>401</Tab>

        <svelte:fragment slot="panel">
            {#if responses[3] === 0}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="json"
                    rounded="true"
                    code={`
[
    {
        "name": "Language mode",
        "id": "lang",
        "description": "If set to auto, the plugin will use the player's locale; if set to silent, the plugin won't output any messages; if set to custom the plugin will use the language specified in the custom language file; If set to a locale, the plugin will use that locale.",
        "value": "auto",
        "defaultValue": "auto",
        "enumValues": ["en_us", "de_de", "auto", "silent", "custom", ...]
    },
    {
        "name": "Jukebox GUI size",
        "id": "jukebox-gui-size",
        "description": "Sets the maximum amount of items that can be added to a jukebox GUI.",
        "value": 64,
        "defaultValue": 96
    },
    ...
]`} />
            {:else if responses[3] === 1}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="text"
                    rounded="true"
                    code={`
401 Unauthorized`} />
            {/if}
        </svelte:fragment>
    </TabGroup>
</div>

<div class="p-5 bg-surface-500 rounded-lg mt-3">
    <h4 class="h4"><b class="text-red-500">POST</b> /config/apply/</h4>

    <blockquote class="blockquote">Applies a new config using the config.json file, it'll 400 when not valid</blockquote>
    
    <h5 class="h5 mt-4">Request</h5>
    
    <p>Headers: </p>
    <CodeBlock
        class="border border-mc-light-gray rounded-lg m-4"
        language="http"
        rounded="true"
        code={`
Authorization: Bearer TheSuperSecretJextBearerToken`} />

    <p>Body: </p>
    <CodeBlock
        class="border border-mc-light-gray rounded-lg m-4"
        language="json"
        rounded="true"
        code={`
{
    "lang": "auto",
    "jukebox-gui-size": 64,
    ...
}`}/>

    <h5 class="h5 mt-4">Response</h5>

    <TabGroup>
        <Tab bind:group={responses[4]} name="200" value={0}>200</Tab>
        <Tab bind:group={responses[4]} name="401" value={1}>401</Tab>
        <Tab bind:group={responses[4]} name="400" value={2}>400</Tab>

        <svelte:fragment slot="panel">
            {#if responses[4] === 0}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="text"
                    rounded="true"
                    code={`
(empty)`} />
            {:else if responses[4] === 1}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="text"
                    rounded="true"
                    code={`
401 Unauthorized`} />
            {:else if responses[4] === 2}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="text"
                    rounded="true"
                    code={`
(empty)`} />
            {/if}
        </svelte:fragment>
    </TabGroup>
</div>

<h3 class="h3 mt-4">Discs:</h3>

<div class="p-5 bg-surface-500 rounded-lg mt-3">
    <h4 class="h4"><b class="text-blue-500">GET</b> /discs/read/</h4>

    <blockquote class="blockquote">Reads the current resourcepack</blockquote>
    
    <h5 class="h5 mt-4">Request</h5>
    
    <p>Headers: </p>
    <CodeBlock
        class="border border-mc-light-gray rounded-lg m-4"
        language="http"
        rounded="true"
        code={`
Authorization: Bearer TheSuperSecretJextBearerToken`} />

    <h5 class="h5 mt-4">Response</h5>

    <TabGroup>
        <Tab bind:group={responses[5]} name="200" value={0}>200</Tab>
        <Tab bind:group={responses[5]} name="400" value={1}>400</Tab>
        <Tab bind:group={responses[5]} name="401" value={2}>401</Tab>

        <svelte:fragment slot="panel">
            {#if responses[5] === 0}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="json"
                    rounded="true"
                    code={`
(binary data)`} />
            {:else if responses[5] === 1}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="text"
                    rounded="true"
                    code={`
(empty)`} />
            {:else if responses[5] === 2}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="text"
                    rounded="true"
                    code={`
401 Unauthorized`} />
            {/if}
        </svelte:fragment>
    </TabGroup>
</div>

<div class="p-5 bg-surface-500 rounded-lg mt-3">
    <h4 class="h4"><b class="text-red-500">POST</b> /discs/apply/</h4>

    <blockquote class="blockquote">Applies a new resourcepack using the resource-pack.zip file</blockquote>
    
    <h5 class="h5 mt-4">Request</h5>
    
    <p>Headers: </p>
    <CodeBlock
        class="border border-mc-light-gray rounded-lg m-4"
        language="http"
        rounded="true"
        code={`
Authorization: Bearer TheSuperSecretJextBearerToken`} />

    <p>Body: </p>
    <CodeBlock
        class="border border-mc-light-gray rounded-lg m-4"
        language="text"
        rounded="true"
        code={`
(binary data)`}/>

    <h5 class="h5 mt-4">Response</h5>

    <TabGroup>
        <Tab bind:group={responses[6]} name="200" value={0}>200</Tab>
        <Tab bind:group={responses[6]} name="401" value={1}>401</Tab>

        <svelte:fragment slot="panel">
            {#if responses[6] === 0}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="text"
                    rounded="true"
                    code={`
(empty)`} />
            {:else if responses[6] === 1}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="text"
                    rounded="true"
                    code={`
401 Unauthorized`} />
            {/if}
        </svelte:fragment>
    </TabGroup>
</div>

<div class="p-5 bg-surface-500 rounded-lg mt-3">
    <h4 class="h4"><b class="text-red-500">POST</b> /discs/applygeyser/</h4>

    <blockquote class="blockquote">When Geyser-Spigot is installed, it'll apply the resourcepack to Geyser players</blockquote>
    
    <h5 class="h5 mt-4">Request</h5>
    
    <p>Headers: </p>
    <CodeBlock
        class="border border-mc-light-gray rounded-lg m-4"
        language="http"
        rounded="true"
        code={`
Authorization: Bearer TheSuperSecretJextBearerToken`} />

    <p>Body: </p>
    <CodeBlock
        class="border border-mc-light-gray rounded-lg m-4"
        language="text"
        rounded="true"
        code={`
(binary data)`}/>

    <h5 class="h5 mt-4">Response</h5>

    <TabGroup>
        <Tab bind:group={responses[7]} name="200" value={0}>200</Tab>
        <Tab bind:group={responses[7]} name="400" value={1}>400</Tab>
        <Tab bind:group={responses[7]} name="401" value={2}>401</Tab>

        <svelte:fragment slot="panel">
            {#if responses[7] === 0}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="text"
                    rounded="true"
                    code={`
(empty)`} />
            {:else if responses[7] === 1}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="text"
                    rounded="true"
                    code={`
(empty)`} />
            {:else if responses[7] === 2}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="text"
                    rounded="true"
                    code={`
401 Unauthorized`} />
            {/if}
        </svelte:fragment>
    </TabGroup>
</div>

<p>There's also a <code>/resource-pack.zip</code> endpoint that returns the current resourcepack</p>

<div class="p-5 bg-surface-500 rounded-lg mt-3">
    <h4 class="h4"><b class="text-blue-500">GET</b> /resource-pack.zip</h4>

    <blockquote class="blockquote">Returns the current resourcepack</blockquote>
    
    <h5 class="h5">Response</h5>

    <TabGroup>
        <Tab bind:group={responses[8]} name="200" value={0}>200</Tab>
        <Tab bind:group={responses[8]} name="404" value={1}>404</Tab>

        <svelte:fragment slot="panel">
            {#if responses[8] === 0}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="text"
                    rounded="true"
                    code={`
(binary data)`} />
            {:else if responses[8] === 1}
                <CodeBlock
                    class="border border-mc-light-gray rounded-lg m-4"
                    language="text"
                    rounded="true"
                    code={`
(empty)`} />
            {/if}
        </svelte:fragment>
    </TabGroup>
</div>