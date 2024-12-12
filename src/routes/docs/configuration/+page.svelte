<script lang="ts">
	import { base } from "$app/paths";
	import { flame, undo } from "$lib/assets";
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
		<b class="text-white text-xs uppercase">CONFIGURATION</b>
	</div>
	<div class="p-4 flex-col flex text-white overflow-y-auto w-full">
        <h2 class="text-4xl mb-4">Configuring the plugin</h2>

        <p>The plugin comes with a default configuration file that you can use as a base, you can find it in the <code>plugins/JukeboxExtendedReborn</code> folder. You can edit it with any text editor</p>

        <div class="bg-green-900 rounded-lg p-4 mt-4">
            <div class="flex gap-4 items-start">
                <img src={flame} alt="warning" />
                <div id="popup">
                    You can also use the desktop app to configure the plugin! Check out the <a href="{base}/docs/desktop-app" class="text-blue-400">desktop app documentation</a> for more information.
                </div>
            </div>
        </div>

        <h2 class="text-4xl my-4">Config nodes</h2>

        <p>Here is a list of all the configuration nodes that you can use to configure the plugin:</p>

        <div class="ml-8">
            <ul class="list-disc">
                <li><code>lang</code> - The language mode of the plugin.</li>
                <li><code>jukebox-behaviour</code> - The behaviour of the jukebox.</li>
                <li><code>jukebox-gui-style</code> - The style of the jukebox GUI.</li>
                <li><code>jukebox-gui-size</code> - The maximum amount of items that can be added to a jukebox GUI.</li>
                <li><code>disable-music-overlap</code> - If set to true, the plugin will not play music if there is already music playing.</li>
                <li><code>jukebox-range</code> - The range for sound playback.</li>
                <li><code>disc-loottables-limit</code> - The maximum amount of discs that can be found in chests.</li>
                <li><code>fragment-loottables-limit</code> - The maximum amount of disc fragments that can be found in chests.</li>
                <li><code>force-resource-pack</code> - If set to true, the plugin will force players to use the resource pack.</li>
                <li><code>check-for-updates</code> - If set to true, the plugin will check for updates on startup.</li>
                <li><code>allow-metrics</code> - If set to true, the plugin will send metrics to bStats.</li>
                <li><code>enable-resource-pack-host</code> - If set to true, the plugin will host the resource pack and automatically send it to players.</li>
                <li><code>web-interface-port</code> - The port the web interface api & resource pack will be hosted on.</li>
                <li><code>override-web-interface-base-url</code> - The custom base url for both the web interface api & resource pack host.</li>
                <li><code>web-interface-api-enabled</code> - If set to true, the plugin will have a REST api running to edit the discs settings and the config.</li>
                <li><code>web-interface-password</code> - The password required to access the web interface api.</li>
            </ul>
        </div>

        <h2 class="text-4xl my-4">Configuration values</h2>

        <p>Here is a list of all the possible values that you can use to configure the plugin:</p>


        <h3 class="text-2xl my-4">Language mode</h3>

        <p>The language mode of the plugin. You can use the following values:</p>

        <ul class="list-disc ml-8">
            <li><code>auto</code> - The plugin will use the player's locale.</li>
            <li><code>custom</code> - The plugin will use the language specified in the custom language file.</li>
            <li><code>silent</code> - The plugin won't output any messages.</li>
            <li><code>[locale_code]</code> - The plugin will use that locale.</li>
        </ul>

        <h3 class="text-2xl my-4">Jukebox behaviour</h3>

        <p>The behaviour of the jukebox. You can use the following values:</p>

        <ul class="list-disc ml-8">
            <li><code>vanilla</code> - The plugin will use the vanilla jukebox behaviour.</li>
            <li><code>gui</code> - The plugin will use the custom GUI.</li>
        </ul>

        <h3 class="text-2xl my-4">Jukebox GUI style</h3>

        <p>The style of the jukebox GUI. You can use the following values:</p>

        <ul class="list-disc ml-8">
            <li><code>scroll-vertical</code> - Scroll vertically.</li>
            <li><code>scroll-horizontal</code> - Scroll horizontally.</li>
            <li><code>page-vertical</code> - Page vertically.</li>
            <li><code>page-horizontal</code> - Page horizontally.</li>
        </ul>

        <h3 class="text-2xl my-4">Jukebox GUI size</h3>

        <p>The maximum amount of items that can be added to a jukebox GUI. You can use any positive integer value.</p>

        <h3 class="text-2xl my-4">Disable music overlap</h3>

        <p>If set to true, the plugin will not play music if there is already music playing. You can use the following values:</p>

        <ul class="list-disc ml-8">
            <li><code>true</code> - The plugin will not play music if there is already music playing.</li>
            <li><code>false</code> - The plugin will play music even if there is already music playing.</li>
        </ul>

        <h3 class="text-2xl my-4">Jukebox range</h3>
        
        <p>Sets the range for sound playback. If set to values less than or equals to 0 it'll play for everyone on the server. Only works if the audio is mono. You can use any positive integer value.</p>

        <h3 class="text-2xl my-4">Disc loot tables limit</h3>

        <p>Sets the maximum amount of discs that can be found in chests. You can use a JSON object with the following format:</p>

        <pre class="bg-[#1e1e1e] p-2 rounded-lg mt-4 text-sm font-cascadia"><code class="language-json">{`{
    "loot-table-1": 2,
    "loot-table-2": 3,
    ...
}`}</code></pre>

        <h3 class="text-2xl my-4">Disc fragments loot tables limit</h3>

        <p>Sets the maximum amount of disc fragments that can be found in chests. You can use a JSON object with the following format:</p>

        <pre class="bg-[#1e1e1e] p-2 rounded-lg mt-4 text-sm"><code class="language-json">{`{
    "loot-table-1": 2,
    "loot-table-2": 3,
    ...
}`}</code></pre>

        <h3 class="text-2xl my-4">Force resource pack</h3>

        <p>If set to true, the plugin will force players to use the resource pack. If the player declines, they will be kicked. You can use the following values:</p>

        <ul class="list-disc ml-8">
            <li><code>true</code> - The plugin will force players to use the resource pack.</li>
            <li><code>false</code> - The plugin will not force players to use the resource pack.</li>
        </ul>

        <h3 class="text-2xl my-4">Check for updates</h3>

        <p>If set to true, the plugin will check for updates on startup. You can use the following values:</p>

        <ul class="list-disc ml-8">
            <li><code>true</code> - The plugin will check for updates on startup.</li>
            <li><code>false</code> - The plugin will not check for updates on startup.</li>
        </ul>

        <h3 class="text-2xl my-4">Allow metrics</h3>

        <p>If set to true, the plugin will send metrics to bStats. Please consider enabling this, as it helps me improve the plugin. You can use the following values:</p>

        <ul class="list-disc ml-8">
            <li><code>true</code> - The plugin will send metrics to bStats.</li>
            <li><code>false</code> - The plugin will not send metrics to bStats.</li>
        </ul>

        <h3 class="text-2xl my-4">Enable resource pack host</h3>

        <p>If set to true, the plugin will host the resource pack and automatically send it to players. You can use the following values:</p>

        <ul class="list-disc ml-8">
            <li><code>true</code> - The plugin will host the resource pack and automatically send it to players.</li>
            <li><code>false</code> - The plugin will not host the resource pack.</li>
        </ul>

        <h3 class="text-2xl my-4">Web interface port</h3>

        <p>The port the web interface api & resource pack will be hosted on. You can use any positive integer value.</p>

        <h3 class="text-2xl my-4">Web interface base url</h3>

        <p>If set to something it'll use the custom base url for both the web interface api & resource pack host. You can use any string value.</p>

        <h3 class="text-2xl my-4">Web interface api enabled</h3>

        <p>If set to true, the plugin will have a REST api running to edit the discs settings and the config. You can use the following values:</p>

        <ul class="list-disc ml-8">
            <li><code>true</code> - The plugin will have a REST api running.</li>
            <li><code>false</code> - The plugin will not have a REST api running.</li>
        </ul>

        <h3 class="text-2xl my-4">Web interface password</h3>

        <p>The password required to access the web interface api. You can use any string value.</p>
    </div>
</div>
