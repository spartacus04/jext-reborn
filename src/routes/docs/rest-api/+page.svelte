<script lang="ts">
	import { undo } from '$lib/assets';
	import TabbedJsonBlock from '$lib/components/docs/TabbedJsonBlock.svelte';
    import hljs from 'highlight.js/lib/core';
    import json from 'highlight.js/lib/languages/json';
	import { onMount } from 'svelte';

    hljs.registerLanguage('json', json);

    onMount(() => {
        hljs.highlightAll();
    });
</script>

<div class="flex h-full flex-col w-full">
	<div class="flex flex-row bg-surface-background w-full py-4 px-4">
		<a href="../">
			<img src={undo} alt="back" class="h-4 w-4 mr-2" />
		</a>
		<b class="text-white text-xs uppercase">REST API</b>
	</div>
	<div class="p-4 flex-col flex text-white overflow-y-auto w-full">
        <h1 class="text-4xl mb-4">REST Api</h1>

        <p>
            The REST API is used to interact with the JEXT plugin. It is used to upload discs, apply
            resourcepacks and more remotely. The API is secured with a bearer token that you can get
            by connecting to the server.
        </p>

        <p>Here is a list of all available endpoints:</p>

        <h3 class="text-2xl mt-4">Auth:</h3>

        <div class="p-5 bg-surface-500 rounded-lg mt-3">
            <h4 class="text-2xl"><b class="text-red-500">POST</b> /connect/</h4>

            <blockquote class="blockquote">Connects to the JEXT server</blockquote>

            <h5 class="text-xl mt 4">Request</h5>

            <p>Body:</p>

            <div class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia w-full text-white h-min">
                <p>SuperSecretJextPassword</p>
            </div>

            <h5 class="text-xl mt-4">Response</h5>

            <TabbedJsonBlock 
                tabs={["200", "401"]}
                tabsContents={[
                    `
TheSuperSecretJextBearerToken
                    `,
                    `
401 Unauthorized
                    `
                ]}
            />

        </div>

        <div class="p-5 bg-surface-500 rounded-lg mt-3">
            <h4 class="text-2xl"><b class="text-red-500">POST</b> /disconnect/</h4>

            <blockquote class="blockquote">Disconnects from the JEXT server</blockquote>

            <h5 class="text-xl mt-4">Request</h5>

            <p>Headers:</p>

            <div class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia w-full text-white h-min">
                <p>Authorization: Bearer TheSuperSecretJextBearerToken</p>
            </div>

            <h5 class="text-xl mt 4">Response</h5>

            <TabbedJsonBlock 
                tabs={["200", "401"]}
                tabsContents={[
                    `
(empty)
                    `,
                    `
401 Unauthorized
                    `
                ]}
            />
        </div>

        <div class="p-5 bg-surface-500 rounded-lg mt-3">
            <h4 class="text-2xl"><b class="text-blue-500">GET</b> /health/</h4>

            <blockquote class="blockquote">
                Checks the health of the JEXT server, if a bearer token is set it'll 401 when it's not
                registered
            </blockquote>

            <h5 class="text-xl mt-4">Request</h5>

            <p>Headers:</p>

            <div class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia w-full text-white h-min">
                <p>Authorization: Bearer TheSuperSecretJextBearerToken (optional)</p>
            </div>

            <h5 class="text-xl mt 4">Response</h5>

            <TabbedJsonBlock 
                tabs={["200", "401"]}
                tabsContents={[
                    `
(empty)
                    `,
                    `
401 Unauthorized
                    `
                ]}
            />
        </div>

        <h3 class="text-2xl mt-4">Config:</h3>

        <div class="p-5 bg-surface-500 rounded-lg mt-3">
            <h4 class="text-2xl"><b class="text-blue-500">GET</b> /config/read/</h4>

            <blockquote class="blockquote">
                Reads the current config, including key, description, value, default value and displayname
            </blockquote>

            <h5 class="text-xl mt-4">Request</h5>

            <p>Headers:</p>

            <div class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia w-full text-white h-min">
                <p>Authorization: Bearer TheSuperSecretJextBearerToken (optional)</p>
            </div>

            <h5 class="text-xl mt 4">Response</h5>

            <TabbedJsonBlock 
                tabs={["200", "401"]}
                tabsContents={[
                    `
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
]
                    `,
                    `
401 Unauthorized
                    `
                ]}
            />

        </div>

        <div class="p-5 bg-surface-500 rounded-lg mt-3">
            <h4 class="text-2xl"><b class="text-red-500">POST</b> /config/apply/</h4>

            <blockquote class="blockquote">
                Applies a new config using the config.json file, it'll 400 when not valid
            </blockquote>

            <h5 class="text-xl mt-4">Request</h5>

            <p>Headers:</p>

            <div class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia w-full text-white h-min">
                <p>Authorization: Bearer TheSuperSecretJextBearerToken</p>
            </div>

            <p>Body:</p>

            <pre class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia"><code class="language-json text-[#b5cea8]">{`{
    "lang": "auto",
    "jukebox-gui-size": 64,
    ...
}`}</code></pre>

            <h5 class="text-xl mt 4">Response</h5>

            <TabbedJsonBlock 
                tabs={["200", "401", "400"]}
                tabsContents={[
                    `
(empty)
                    `,
                    `
401 Unauthorized
                    `,
                    `
(empty)
                    `
                ]}
            />
        </div>

        <h3 class="text-2xl mt-4">Discs:</h3>

        <div class="p-5 bg-surface-500 rounded-lg mt-3">
            <h4 class="text-2xl"><b class="text-blue-500">GET</b> /discs/read/</h4>

            <blockquote class="blockquote">Reads the current resourcepack</blockquote>

            <h5 class="text-xl mt-4">Request</h5>

            <p>Headers:</p>

            <div class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia w-full text-white h-min">
                <p>Authorization: Bearer TheSuperSecretJextBearerToken</p>
            </div>

            <h5 class="text-xl mt 4">Response</h5>

            <TabbedJsonBlock 
                tabs={["200", "404", "401"]}
                tabsContents={[
                    `
(resource pack binary data)
                    `,
                    `
(empty)
                    `,
                    `
401 Unauthorized
                    `
                ]}
            />

        </div>

        <div class="p-5 bg-surface-500 rounded-lg mt-3">
            <h4 class="text-2xl"><b class="text-red-500">POST</b> /discs/apply/</h4>

            <blockquote class="blockquote">
                Applies a new resourcepack using the resource-pack.zip file
            </blockquote>

            <h5 class="text-xl mt-4">Request</h5>

            <p>Headers:</p>

            <div class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia w-full text-white h-min">
                <p>Authorization: Bearer TheSuperSecretJextBearerToken</p>
            </div>

            <p>Body:</p>
            
            <div class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia w-full text-white h-min">
                <p>(resource pack binary data)</p>
            </div>

            <h5 class="text-xl mt 4">Response</h5>

            <TabbedJsonBlock 
                tabs={["200", "401"]}
                tabsContents={[
                    `
(empty)
                    `,
                    `
401 Unauthorized
                    `
                ]}
            />

        </div>

        <div class="p-5 bg-surface-500 rounded-lg mt-3">
            <h4 class="text-2xl"><b class="text-red-500">POST</b> /discs/applygeyser/</h4>

            <blockquote class="blockquote">
                When Geyser-Spigot is installed, it'll apply the resourcepack to Geyser players
            </blockquote>

            <h5 class="text-xl mt-4">Request</h5>

            <p>Headers:</p>

            <div class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia w-full text-white h-min">
                <p>Authorization: Bearer TheSuperSecretJextBearerToken</p>
            </div>

            <p>Body:</p>

            <div class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia w-full text-white h-min">
                <p>(resource pack binary data)</p>
            </div>

            <h5 class="text-xl mt 4">Response</h5>

            <TabbedJsonBlock 
                tabs={["200", "401"]}
                tabsContents={[
                    `
(empty)
                    `,
                    `
401 Unauthorized
                    `
                ]}
            />

        </div>


        <p>There's also a <code>/resource-pack.zip</code> endpoint that returns the current resourcepack</p>

        <div class="p-5 bg-surface-500 rounded-lg mt-3">
            <h4 class="text-2xl"><b class="text-blue-500">GET</b> /resource-pack.zip</h4>

            <blockquote class="blockquote">Returns the current resourcepack</blockquote>

            <h5 class="text-xl">Response</h5>

            <TabbedJsonBlock 
                tabs={["200", "404"]}
                tabsContents={[
                    `
(resource pack binary data)
                    `,
                    `
(empty)
                    `
                ]}
            />
        </div>
    </div>
</div>

