<script lang="ts">
    import { undo } from '$lib/assets';
</script>

<div class="flex h-full flex-col w-full">
	<div class="flex flex-row bg-surface-background w-full py-4 px-4">
		<a href="../">
			<img src={undo} alt="back" class="h-4 w-4 mr-2" />
		</a>
		<b class="text-white text-xs uppercase">OTHER PLUGIN INFO</b>
	</div>
	<div class="p-4 flex-col flex text-white overflow-y-auto w-full">
        <h2 class="text-4xl mb-4">Resource pack host</h2>

        <p>
            If the <code>resource-pack-host</code> config option is enabled, the plugin will try to serve the resource pack from the <code>resource-pack.zip</code> file in the plugin directory.
        </p>

		<h2 class="text-4xl mb-4">Asset loading</h2>

        <p>
            If the <code>resource-pack-host</code> config option is enabled, the plugin will try to fetch the data from the <code>resource-pack.zip</code> file in the plugin directory.
            If not, the plugin will try to download the resource pack specified in the <code>server.properties</code> file in the <code>caches</code> directory.
            If a <code>resource-pack-sha1</code> is not specified in the <code>server.properties</code>, the resource pack will be downloaded every time you reload the plugin.
        </p>

		<h2 class="text-4xl mb-4">Jukebox GUI</h2>

        <p>
            The Jukebox GUI is a optional GUI that can appear when people click on jukeboxes or run the <code>/jukeboxgui</code> command.
            A player can put discs on the GUI by using the left click, and play/stop them by using the. right click.
            When a jukebox is destroyed all discs in the Jukebox GUI are dropped.
            The discs memorized in GUIs are saved in the <code>.savedata</code> file.
        </p>

		<h2 class="text-4xl mb-4">Noteblock API support</h2>

        <p>
            If noteblock API is found in the server plugins directory, JEXT will be able to play .nbs alongside other discs.
            A <code>nbs</code> directory will be created in the plugin directory. There you can put the <code>.nbs</code> music discs.
            By default the plugin will try to read the metadata associated with the discs from the resource pack, however, if it fails it relies on the embedded nbs metadata.
        </p>

		<h2 class="text-4xl mb-4">Geyser support</h2>
        
        <p>
            Geyser is supported both as a Spigot plugin, and as standalone. If you use Geyser as a Spigot plugin, everything will work without problems.
            If you use Geyser as a Standalone app, you can drop a copy of the plugin in the extensions directory: the plugin will function as a bridge between Geyser and JEXT.
        </p>
	</div>
</div>