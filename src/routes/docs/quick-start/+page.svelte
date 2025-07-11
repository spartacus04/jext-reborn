<script lang="ts">
	import { base } from '$app/paths';
	import {
		spigotmc,
		modrinth,
		hangar,
		github,
		hand_helping,
		warning,
		flame,
		undo
	} from '$lib/assets';
</script>

<div class="flex h-full flex-col w-full">
	<div class="flex flex-row bg-surface-background w-full py-4 px-4">
		<a href="../">
			<img src={undo} alt="back" class="h-4 w-4 mr-2" />
		</a>
		<b class="text-white text-xs uppercase">QUICK START</b>
	</div>
	<div class="p-4 flex-col flex text-white overflow-y-auto w-full">
		<h2 class="text-4xl mb-4">Downloading and installing the plugin</h2>

		You can download the plugin from any of these sources:

		<div
			class="grid grid-cols-1 sm:grid-cols-2 lg:!grid-cols-4 bg-surface-background rounded-3xl [&>*]:p-4 my-4"
		>
			<a
				class="flex flex-col items-center justify-center hover:bg-surface-separator gap-2 rounded-l-3xl"
				href="https://www.spigotmc.org/resources/jukebox-extended-reborn-add-custom-music-discs.103219/"
				target="_blank"
			>
				<span class="h-[220px] aspect-square"
					><img src={spigotmc} alt="github" srcset="" class="h-full w-full" /></span
				>
				<span>Spigot MC</span>
			</a>
			<a
				class="flex flex-col items-center justify-center border-cyan-500 border-4 gap-2 hover:bg-surface-separator"
				href="https://modrinth.com/plugin/jukebox-extended-reborn"
				target="_blank"
			>
				<span class="h-[220px] aspect-square"
					><img src={modrinth} alt="github" srcset="" class="h-full w-full" /></span
				>
				<span>Modrinth</span>
			</a>
			<a
				class="flex flex-col items-center justify-center hover:bg-surface-separator gap-2"
				href="https://hangar.papermc.io/spartacus04/JukeboxExtendedReborn"
				target="_blank"
			>
				<span class="h-[220px] aspect-square"
					><img src={hangar} alt="github" srcset="" class="h-full w-full" /></span
				>
				<span>Hangar</span>
			</a>
			<a
				class="flex flex-col items-center justify-center hover:bg-surface-separator gap-2 rounded-r-3xl"
				href="https://github.com/spartacus04/jext-reborn/releases/latest"
				target="_blank"
			>
				<span class="h-[220px] aspect-square"
					><img src={github} alt="github" srcset="" class="h-full w-full" /></span
				>
				<span>Github</span>
			</a>
		</div>

		<p>
			You also need to download <b>PacketEvents</b> as it's one of the
			<b class="underline">required</b> dependencies, you can download it from any of these sources:
		</p>

		<div class="grid grid-cols-1 sm:grid-cols-3 bg-surface-background rounded-3xl [&>*]:p-4 my-4">
			<a
				class="flex flex-col items-center justify-center hover:bg-surface-separator gap-2 rounded-l-3xl"
				href="https://www.spigotmc.org/resources/packetevents-api.80279/"
				target="_blank"
			>
				<span class="h-[220px] aspect-square"
					><img src={spigotmc} alt="github" srcset="" class="h-full w-full" /></span
				>
				<span>Spigot MC</span>
			</a>
			<a
				class="flex flex-col items-center justify-center hover:bg-surface-separator gap-2"
				href="https://modrinth.com/plugin/packetevents"
				target="_blank"
			>
				<span class="h-[220px] aspect-square"
					><img src={modrinth} alt="github" srcset="" class="h-full w-full" /></span
				>
				<span>Modrinth</span>
			</a>
			<a
				class="flex flex-col items-center justify-center hover:bg-surface-separator gap-2 rounded-r-3xl"
				href="https://github.com/retrooper/packetevents/releases/latest"
				target="_blank"
			>
				<span class="h-[220px] aspect-square"
					><img src={github} alt="github" srcset="" class="h-full w-full" /></span
				>
				<span>Github</span>
			</a>
		</div>

		<!-- blue thing saying that modrinth supports developers without accordion -->

		<div class="bg-cyan-500 rounded-lg p-4 mb-4">
			<div class="flex gap-4 items-start">
				<img src={hand_helping} alt="warning" />
				<div id="popup">
					Did you know that Modrinth runs a monetization program? That means that for each free
					download from modrinth the developer gets paid a few cents! That's not a lot, but it can
					help me improve the plugin faster and be a bit more motivated.
				</div>
			</div>
		</div>

		<!-- popup for protocollib version not supported -->

		<div class="bg-orange-900 rounded-lg p-4 mb-4">
			<div class="flex gap-4 items-start">
				<img src={warning} alt="warning" />
				<div id="popup">
					Versions of JEXT Reborn before 2.1.10 require ProtocolLib instead of Packetevents.

					You can find the latest version of ProtocolLib on any of these sources:

					<div class="grid grid-cols-1 sm:grid-cols-3 bg-surface-background rounded-3xl [&>*]:p-4 my-4">
						<a
							class="flex flex-col items-center justify-center hover:bg-surface-separator gap-2 rounded-l-3xl"
							href="https://www.spigotmc.org/resources/protocollib.1997/"
							target="_blank"
						>
							<span class="h-[220px] aspect-square"
								><img src={spigotmc} alt="github" srcset="" class="h-full w-full" /></span
							>
							<span>Spigot MC</span>
						</a>
						<a
							class="flex flex-col items-center justify-center hover:bg-surface-separator gap-2"
							href="https://hangar.papermc.io/dmulloy2/ProtocolLib"
							target="_blank"
						>
							<span class="h-[220px] aspect-square"
								><img src={hangar} alt="github" srcset="" class="h-full w-full" /></span
							>
							<span>Hangar</span>
						</a>
						<a
							class="flex flex-col items-center justify-center hover:bg-surface-separator gap-2 rounded-r-3xl"
							href="https://github.com/dmulloy2/ProtocolLib/releases/latest"
							target="_blank"
						>
							<span class="h-[220px] aspect-square"
								><img src={github} alt="github" srcset="" class="h-full w-full" /></span
							>
							<span>Github</span>
						</a>
					</div>

					Sometimes protocollib gets updated for later versions but the actual update doesn't get
					published until later dates. You can download these updated versions from the latest
					successful compilation on their Jenkins CI (<a
						href="https://ci.dmulloy2.net/job/ProtocolLib/"
						class="text-blue-400"
						target="_blank">here!</a
					>). Be warned though, these versions may be unstable or sometimes not even work.
				</div>
			</div>
		</div>

		<p>
			Once you have the plugin, drop it in your plugins folder and restart the server. You should
			see this message in the server output:
		</p>

		<blockquote class="bg-black p-2 flex font-cascadia my-4">
			[
			<p class="text-mc-light-green">JEXT</p>
			]&#160
			<p class="text-mc-light-green">Enabled Jukebox Extended Reborn, Do Re Mi!</p>
		</blockquote>

		<p>
			The plugin should work on every Spigot-flavoured server (Spigot, Paper, Purpur, Folia & more).
			You are now ready to configure it!
		</p>

		<h2 class="text-4xl my-4">Configuring the plugin</h2>

		<p>
			The plugin comes with a default configuration file that you can use as a base, you can find it
			in the <code>plugins/JukeboxExtendedReborn</code> folder. You can edit it with any text
			editor, and you can check out the
			<a href="{base}/docs/configuration" class="text-blue-400">config documentation</a> for more information
			on how to configure the plugin.
		</p>

		<!-- popup that tells that you can use the desktop app green -->

		<div class="bg-green-900 rounded-lg p-4 mt-4">
			<div class="flex gap-4 items-start">
				<img src={flame} alt="warning" />
				<div id="popup">
					You can also use the desktop app to configure the plugin! Check out the <a
						href="{base}/docs/desktop-app"
						class="text-blue-400">desktop app documentation</a
					> for more information.
				</div>
			</div>
		</div>

		<h2 class="text-4xl my-4">Adding your first custom disc</h2>

		<!-- remove the manual disc loading method, show the self host option first and the built in resource pack host later -->

		<p>
			The WebUI is the easiest way to add custom discs to the plugin, you can access it <a
				href={base}
				class="text-blue-400">here</a
			>. You can add discs by pressing the big + button in the "discs" tab, and uploading either a
			audio, nbs or resource pack file. The WebUI will handle the rest. You can then edit and
			customize each disc to your liking. You can also customize the generated resourcepack in the
			"Resource pack manager", it allows to merge and configure other resource packs with the
			generated one! After finishing your configuration you can click on the "Generate" button, and
			you can download your resource packs!
		</p>

		<!-- popup that shows an alternative to the webui: altought not recomended -->

		<div class="bg-orange-900 rounded-lg p-4 my-4">
			<div class="flex gap-4 items-start">
				<img src={warning} alt="warning" />
				<div id="popup">
					Altought it's not recommended you can also add custom discs manually by using a jext.json
					file inside a resource pack, you can check out the <a
						href="{base}/docs/manual-setup"
						class="text-blue-400">resourcepack documentation</a
					> for more information on how to manage discs.
				</div>
			</div>
		</div>

		<p>
			To apply the new resource pack you can add it to your <code>server.properties</code> file
			normally, the plugin will parse and download it automatically. You can also use the built-in
			resource pack host by enabling it in the config and moving the java resource pack to
			<code>JukeboxExtendedReborn/resource-pack.zip</code>.
		</p>

		<!-- popup that tells the user that the bedrock edition resourcepack is supposed to be used with geyser -->

		<div class="bg-green-900 rounded-lg p-4 my-4">
			<div class="flex gap-4 items-start">
				<img src={flame} alt="warning" />
				<div id="popup">
					Remember to use the resource pack generated for the Java Edition of Minecraft. If you want
					to use it with Bedrock Edition you need to use <a
						href="https://geysermc.org/"
						class="text-blue-400">Geyser</a
					> and download it to its resource pack directory!
				</div>
			</div>
		</div>

		<p>
			You are now finished with the quickstart guide! You can now explore the rest of the
			documentation to learn more about the plugin.
		</p>
	</div>
</div>

<style>
	#popup {
		text-shadow: 0 0 5px rgba(0, 0, 0, 0.5);
	}
</style>
