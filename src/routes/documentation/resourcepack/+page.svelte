<script lang="ts">
	import { CodeBlock, Accordion, AccordionItem } from '@skeletonlabs/skeleton';
	import warning from '$lib/assets/warning.svg';
</script>

<main class="gap-4">
	<h1 class="h1 mb-4">Manual resourcepack configuration</h1>

	<p>
		If you don't want to use the website to generate the resource pack, you can create the
		resourcepack yourself.
	</p>
	<p>This walkthrough is designed for people who have never worked with a resource pack</p>

	<h2 class="h2 my-4">Setting up the resourcepack structure</h2>

	<p>
		First, you need to create a directory for your resource pack, you can name it whatever you want,
		but it's recommended to name it something that makes sense to you.
	</p>

	<p>Inside the directory, create a <code class="code">pack.mcmeta</code> file.</p>
	<p>Now open the file and add these contents:</p>

	<CodeBlock
		class="border border-mc-light-gray rounded-lg m-4"
		language="json"
		rounded="true"
		code={`
{
    \"pack\": {
        "pack_format": (Your version),
        "description": "(Your description)"
    }
}`}
	/>

	<p>
		Replace <code class="code">(Your description)</code> with a description of your resource pack
		and <code class="code">(Your version)</code> with the pack version associated with your minecraft
		version.
	</p>

	<div class="bg-orange-900 rounded-lg my-4">
		<Accordion>
			<AccordionItem>
				<svelte:fragment slot="lead">
					<img src={warning} alt="warning" />
				</svelte:fragment>
				<svelte:fragment slot="summary">What's my pack version?</svelte:fragment>
				<svelte:fragment slot="content">
					<p>
						You can find a useful table <a
							href="https://minecraft.wiki/w/Pack_format"
							target="_blank"
							rel="noopener noreferrer">here</a
						>!
					</p>
				</svelte:fragment>
			</AccordionItem>
		</Accordion>
	</div>

	<p>Also create a new <code class="code">jext.json</code> file, which we will edit later.</p>
	<br />

	<p>
		Now create an <code class="code">assets/</code> directory inside the root resource pack
		directory, and inside the new directory create a <code class="code">minecraft/</code>.
	</p>
	<p>
		Inside the <code class="code">minecraft/</code> directory create a
		<code class="code">sounds.json</code> file, which we will edit later
	</p>
	<p>
		Inside the <code class="code">minecraft/</code> directory, create the
		<code class="code">sounds/records</code>, <code class="code">models/item</code> and
		<code class="code">textures/item</code> directories. You should now have a file directory like the
		following:
	</p>

	<CodeBlock
		class="border border-mc-light-gray rounded-lg m-4"
		language="plaintext"
		rounded="true"
		code={`
Resource pack root/
└───pack.mcmeta
└───jext.json
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
`}
	/>

	<h2 class="h2 my-4">Adding custom discs</h2>

	<p>Now that you have the resource pack structure set up, you can add your custom discs.</p>
	<br />

	<p>
		First make sure that the audio files are in a <code class="code">.ogg</code> without thumbnail format.
	</p>
	<br />

	<p>
		I'll demostrate the process of adding a new disc with two discs, but you can create multiple
		discs using this method
	</p>

	<p>Open the <code class="code">jext.json</code> file and add the following contents</p>

	<CodeBlock
		class="border border-mc-light-gray rounded-lg m-4"
		language="json"
		rounded="true"
		code={`
[
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
    }

    ...
]`}
	/>
	<p>You can remove the comments (lines starting with //).</p>
	<br />
	<p>
		You need to edit the values with the needed parameters for each disc, just keep in mind the <code
			class="code">model-data</code
		>
		and <code class="code">disc-namespace</code> for each disc.
	</p>

	<p>
		Now open the <code class="code">assets/minecraft/sounds.json</code> file and add the following contents:
	</p>

	<CodeBlock
		class="border border-mc-light-gray rounded-lg m-4"
		language="json"
		rounded="true"
		code={`
{
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
}`}
	/>

	<p>
		Just keep in mind whatever you set as name for each music disc, because you'll need to add the
		music files in a <code class="code">.ogg</code> format with that same name in the
		<code class="code">assets/minecraft/sound/records/</code> directory.
	</p>

	<p>The resource pack tree should now look like this:</p>
	<CodeBlock
		class="border border-mc-light-gray rounded-lg m-4"
		language="plaintext"
		rounded="true"
		code={`
Resource pack root/
└───pack.mcmeta
└───jext.json
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
`}
	/>

	<p>Now we need to add the custom fragment and disc textures.</p>

	<p>
		Create a <code class="code">assets/minecraft/models/item/music_disc_11.json</code> file add add the
		following contents:
	</p>
	<CodeBlock
		class="border border-mc-light-gray rounded-lg m-4"
		language="json"
		rounded="true"
		code={`
{
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
}`}
	/>

	<p>
		You should replace the <code class="code">custom_model_data</code> field with each disc unique model
		data
	</p>
	<p>
		If you want to add custom disc fragments do the same with the <code class="code"
			>assets/minecraft/models/item/disc_fragment_5.json</code
		> file:
	</p>

	<CodeBlock
		class="border border-mc-light-gray rounded-lg m-4"
		language="json"
		rounded="true"
		code={`
{
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
}`}
	/>

	<p>
		Now you need to add the binding for the textures of the discs and fragments in the <code
			class="code">assets/minecraft/models/item/</code
		> directory.
	</p>
	<p>
		For each disc and file create a file with the <code class="code">(model).json</code> name we set
		in the previous files:
	</p>
	<p>Each file should contain the following:</p>

	<CodeBlock
		class="border border-mc-light-gray rounded-lg m-4"
		language="json"
		rounded="true"
		code={`
{ 
    "parent": "item/generated", 
    "textures": { 
        "layer0": "(custom_model_data)" 
    } 
}`}
	/>

	<p>
		Where <code class="code">(custom_model_data)</code> should be replaced by the
		<code class="code">custom_model_data</code> set in the previous file
	</p>

	<p>
		Finally add the textures in a <code class="code">.png</code> format in the
		<code class="code">assets/minecraft/textures/models/</code> directory with the same name as the binding
		set in the previous step
	</p>
	<p>Your resourcepack tree should now look like this:</p>

	<CodeBlock
		class="border border-mc-light-gray rounded-lg m-4"
		language="plaintext"
		rounded="true"
		code={`
Resource pack
└───pack.mcmeta
└───assets
    └───minecraft
        └───sounds.json
        |
        └───models
        |   └───item
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
                └─── ...`}
	/>

	Finally, you can zip the resource pack and add it to your minecraft server!
</main>
