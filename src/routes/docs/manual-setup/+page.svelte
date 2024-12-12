<script lang="ts">
	import { flame, undo, warning } from '$lib/assets';
    import hljs from 'highlight.js/lib/core';
    import json from 'highlight.js/lib/languages/json';
	import { onMount } from "svelte";

    hljs.registerLanguage('json', json);
    hljs.highlightAll();
    onMount(() => {
        hljs.highlightAll();
    })
</script>

<div class="flex h-full flex-col w-full">
	<div class="flex flex-row bg-surface-background w-full py-4 px-4">
		<a href="../">
			<img src={undo} alt="back" class="h-4 w-4 mr-2" />
		</a>
		<b class="text-white text-xs uppercase">MANUAL RESOURCE PACK BUILDING</b>
	</div>
	<div class="p-4 flex-col flex text-white overflow-y-auto w-full">
        <h2 class="text-4xl mb-4">Manual resourcepack configuration</h2>

        <p>
            If you don't want to use the website to generate the resource pack, you can create the
            resourcepack yourself. This walkthrough is designed for people who have never worked with a resource pack.
        </p>

        <div class="bg-orange-900 rounded-lg p-4 mt-4">
            <div class="flex gap-4 items-start">
                <img src={warning} alt="warning" />
                <div id="popup">
                    This walkthrough will not cover the creation of resource pack for Bedrock Edition. I'm sorry for the inconvenience, but you're on your own for that one...
                    <br />
                    If you are a bit experienced with code you can check out how to create a resource pack for Bedrock Edition from the <a
                        href="https://github.com/spartacus04/jext-reborn/blob/dev/gh-pages/src/lib/exporter/pluginExporter.ts#L217"
                        target="_blank"
                        class="text-blue-400"
                        rel="noopener noreferrer">source code</a>!
                </div>
            </div>
        </div>

        <h2 class="text-4xl my-4">Setting up the resourcepack structure</h2>

        <p>
            First, you need to create a directory for your resource pack, you can name it whatever you want,
            but it's recommended to name it something that makes sense to you. Inside the directory, create a <code class="code">pack.mcmeta</code> file.
            Now open the file and add these contents: 
        </p>

        <pre class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia"><code class="language-json text-[#b5cea8]">{`{
    "pack": {
        "pack_format": (Your version),
        "description": "(Your description)"
    }
}`}</code></pre>

        <p>
            Replace <code class="code">(Your description)</code> with a description of your resource pack
            and <code class="code">(Your version)</code> with the pack version associated with your minecraft
            version.
        </p>

        <div class="bg-green-900 rounded-lg p-4 my-4">
            <div class="flex gap-4 items-start">
                <img src={flame} alt="warning" />
                <div id="popup">
                    You can find a useful table for the pack format <a
                        href="https://minecraft.wiki/w/Pack_format"
                        target="_blank"
                        class="text-blue-400"
                        rel="noopener noreferrer">here</a>!
                </div>
            </div>
        </div>

        <p>
            Also create a new <code class="code">jext.json</code> and <code class="code">jext.nbs.json</code> file, which we will edit later.
        </p>

        <p>
            Now create an <code class="code">assets/</code> directory inside the root resource pack
            directory, and inside the new directory create a <code class="code">minecraft/</code>.
            Inside the <code class="code">minecraft/</code> directory create a <code class="code">sounds.json</code> file, which we will edit later.
            Inside the <code class="code">minecraft/</code> directory, create the <code class="code">sounds/records</code>, <code class="code">models/item</code> and <code class="code">textures/item</code> directories. You should now have a file directory like the following:
            You should now have a file directory like the following:
        </p>

        <pre class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia"><code class="language-plaintext">{`Resource pack root/
└───pack.mcmeta
└───jext.json
└───jext.nbs.json
└───assets/
    └───minecraft/
        └───sounds.json
        |
        |
        └───models/
        |   └───item/
        |
        └───sounds
        |   └───records/
        |
        └───textures/
            └───item/
`}</code></pre>

        <h2 class="text-4xl my-4">Adding custom music discs</h2>

        <p>
            Now that you have the resource pack structure set up, you can add your custom music discs.
        </p>

        <p>
            First make sure that the audio files are in a <code class="code">.ogg</code> without thumbnail format.
        </p>

        <p>
            I'll demostrate the process of adding a new disc with two discs, but you can create multiple
            discs using this method
        </p>

        <p>Open the <code class="code">jext.json</code> file and add the following contents</p>

        <pre class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia"><code class="language-json">{`[
    {
        "title": "Disc name",
        "author": "disc author",
        // duration of the disc file in seconds
        "duration": 100,
        // disc namespace, must be unique
        "disc-namespace": "music_disc.musicdiscname",
        // model data of the disc, must be positive and unique (keep this in mind)
        "model-data": 1,
        // true if allows creeper drops, false otherwise
        "creeper-drop": true,
        "lores": [
            "lore added by disc",
            "each entry is a new line"
        ],
        // loottables in which the disc can be found with chance to be found (out of 1000)
        "loot-tables": [
            "chests/ancient_city": 573,
        ],
        // loottables in which the disc fragments can be found
        "fragment-loot-tables": [
            "chests/ancient_city": 573,
        ]
    },
    {
        "title": "disc 2",
        "author": "author of disc 2",
        "duration": 50,
        "disc-namespace": "music_disc.musicdiscname2",
        "model-data": 2,
        "creeper-drop": true,
        "lores": [],
        "loot-tables": [],
        "fragment-loot-tables": []
    },

    ...
]`}</code></pre>

        <p>You can remove the comments (lines starting with //).</p>

        <p>
            You need to edit the values with the needed parameters for each disc, just keep in mind the <code
                class="code">model-data</code> and <code class="code">disc-namespace</code> for each disc.
        </p>

        <p>
            Now open the <code class="code">assets/minecraft/sounds.json</code> file and add the following contents:
        </p>

        <pre class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia"><code class="language-json">{`{
    // The previusly set disc namespaces
    "music_disc.musicdiscname": {
        "sounds": [
            {
                "name": "records/musicdiscname",
                "stream": true
            }
        ]
    },

    "music_disc.musicdiscname2": {
        "sounds": [
            {
                "name": "records/musicdiscname2",
                "stream": true
            }
        ]
    },

    ...
}`}</code></pre>

        <p>
            Just keep in mind whatever you set as name for each music disc, because you'll need to add the
            music files in a <code class="code">.ogg</code> format with that same name in the
            <code class="code">assets/minecraft/sound/records/</code> directory.
        </p>

        <p>The resource pack tree should now look like this:</p>
        <pre class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia"><code class="language-plaintext">{`Resource pack root/
└───pack.mcmeta
└───jext.json
└───jext.nbs.json
└───assets/
    └───minecraft/
        └───sounds.json
        |
        |
        └───models/
        |   └───item/
        |
        └───sounds
        |   └───records/
        |       └───musicdiscname.ogg
        |       └───musicdiscname2.ogg
        |       └─── ...
        |
        └───textures/
            └───item/
`}</code></pre>

        <h2 class="text-4xl my-4">Adding custom NBS discs</h2>

        <p>
            If you want to, you can also add custom NBS discs to your resource pack. The process is similar to adding custom music discs, but with a few differences.
        </p>

        <p>
            Open the <code class="code">jext.nbs.json</code> file and add the following contents:
        </p>

        <!-- same as previous but without duration -->
        <pre class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia"><code class="language-json">{`[
    {
        "title": "Nbs Disc name",
        "author": "disc author",
        // disc namespace, must be unique (across music discs as well)
        "disc-namespace": "music_disc.nbsmusicdiscname",
        // model data of the disc, must be positive and unique (keep this in mind)
        "model-data": 1,
        // true if allows creeper drops, false otherwise
        "creeper-drop": true,
        "lores": [
            "lore added by disc",
            "each entry is a new line"
        ],
        // loottables in which the disc can be found with chance to be found (out of 1000)
        "loot-tables": [
            "chests/ancient_city": 573,
        ],
        // loottables in which the disc fragments can be found
        "fragment-loot-tables": [
            "chests/ancient_city": 573,
        ]
    },
    {
        "title": "nbs disc 2",
        "author": "author of disc 2",
        "disc-namespace": "music_disc.nbsmusicdiscname2",
        "model-data": 2,
        "creeper-drop": true,
        "lores": [],
        "loot-tables": [],
        "fragment-loot-tables": []
    },

    ...
]`}</code></pre>

        <p>
            You should now create a <code class="code">assets/jext/</code> directory inside the root resource pack directory.
            Inside the new directory, you should put all the NBS files with the same name as the <code class="code">disc-namespace</code> set in the <code class="code">jext.nbs.json</code> file.
            The files should still be in the <code class="code">.nbs</code> format.
        </p>

        <p>
            The resource pack tree should now look like this:
        </p>

        <pre class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia"><code class="language-plaintext">{`Resource pack root/
└───pack.mcmeta
└───jext.json
└───jext.nbs.json
└───assets/
    └───jext/
    |   └───music_disc.nbsmusicdiscname.nbs
    |   └───music_disc.nbsmusicdiscname2.nbs
    |   └─── ...
    |
    └───minecraft/
        └───sounds.json
        |
        |
        └───models/
        |   └───item/
        |
        └───sounds
        |   └───records/
        |       └───musicdiscname.ogg
        |       └───musicdiscname2.ogg
        |       └─── ...
        |
        └───textures/
            └───item/
`}</code></pre>

        <h2 class="text-4xl my-4">Adding custom disc and fragment textures</h2>

        <p>
            Create a <code class="code">assets/minecraft/models/item/music_disc_11.json</code> file add add the following contents:
        </p>
    
        <pre class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia"><code class="language-json">{`{
    "parent": "item/generated",
    "textures": {
        "layer0": "item/music_disc_11"
    },
    "overrides": [
        {
            "predicate": {
                "custom_model_data": 1
            },
            "model": "item/music_disc_musicdiscname"
        },
        {
            "predicate": {
                "custom_model_data": 2
            },
            "model": "item/music_disc_musicdiscname2"
        },

        ...
    ]
}`}</code></pre>

        <p>
            You should replace the <code class="code">custom_model_data</code> field with each disc unique model data
        </p>
        <p>
            If you want to add custom disc fragments do the same with the <code class="code">assets/minecraft/models/item/disc_fragment_5.json</code> file:
        </p>

        <pre class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia"><code class="language-json">{`{
    "parent": "item/generated",
    "textures": {
        "layer0": "item/disc_fragment_5"
    },
    "overrides": [
        {
            "predicate": {
                "custom_model_data": 1
            },
            "model": "item/fragment_namespace"
        },
        {
            "predicate": {
                "custom_model_data": 2
            },
            "model": "item/fragment_namespace2"
        },

        ...
    ]
}`}</code></pre>

        <p>
            Now you need to add the binding for the textures of the discs and fragments in the <code class="code">assets/minecraft/models/item/</code> directory.
            For each disc and file create a file with the <code class="code">(model).json</code> name we set in the previous files.
            Each file should contain the following:
        </p>

        <pre class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia"><code class="language-json">{`{
    "parent": "item/generated",
    "textures": {
        "layer0": "(custom_model_data)"
    }
}`}</code></pre>

        <p>
            Where <code class="code">(custom_model_data)</code> should be replaced by the <code class="code">custom_model_data</code> set in the previous file
        </p>

        <p>
            Finally add the textures in a <code class="code">.png</code> format in the <code class="code">assets/minecraft/textures/models/</code> directory with the same name as the binding set in the previous step
        </p>
        <p>Your resourcepack tree should now look like this:</p>

        <pre class="bg-[#1e1e1e] p-2 my-4 rounded-lg text-sm font-cascadia"><code class="language-plaintext">{`Resource pack
└───pack.mcmeta
└───jext.json
└───jext.nbs.json
└───assets/
    └───jext/
    |   └───music_disc.nbsmusicdiscname.nbs
    |   └───music_disc.nbsmusicdiscname2.nbs
    |   └─── ...
    |
    └───minecraft/
        └───sounds.json
        |
        |
        └───models/
        |   └───item/
        |       └───music_disc_11.json
        |       └───disc_fragment_5.json
        |       └───music_disc_musicdiscname.json
        |       └───music_disc_musicdiscname2.json
        |       └───fragment_namespace.json
        |       └───fragment_namespace2.json
        |       └─── ...
        │
        └───sounds
        |   └───records
        |       └───musicdiscname.ogg
        |       └───musicdiscname2.ogg
        |       └─── ...
        |
        └───textures
            └───item
                └───music_disc_musicdiscname.png
                └───music_disc_musicdiscname2.png
                └───fragment_namespace.json
                └───fragment_namespace2.json
                └─── ...`}</code></pre>

        <p>Finally, you can zip the resource pack and add it to your minecraft server!</p>
    </div>
</div>
