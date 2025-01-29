<script lang="ts">
	import { base } from '$app/paths';
	import { warning, undo } from '$lib/assets';
</script>

<div class="flex h-full flex-col w-full">
	<div class="flex flex-row bg-surface-background w-full py-4 px-4">
		<a href="../">
			<img src={undo} alt="back" class="h-4 w-4 mr-2" />
		</a>
		<b class="text-white text-xs uppercase">DESKTOP APP & REMOTE CONTROL</b>
	</div>
	<div class="p-4 flex-col flex text-white overflow-y-auto w-full">
		<p>
			The desktop app is a companion app for the plugin that allows you to control the plugin
			remotely. It also adds some extra quality of life features that aren't available in the WebUI.
		</p>

		<h2 class="text-4xl my-4">New features</h2>

		<h3 class="text-2xl my-4">Faster generation speed</h3>

		<p>
			The desktop app is much faster than the WebUI when it comes to generating the resource pack.
			This is because the desktop app uses the native version of FFmpeg which is much faster than
			the WebUI's version.
		</p>

		<h3 class="text-2xl my-4">Direct download from Youtube/Soundcloud</h3>

		<p>
			The desktop app leverages the power of yt-dlp to allow you to download music directly from
			Youtube and Soundcloud. This means you can download music from these platforms without having
			to use a separate tool. It also allows you to download music from other platforms that yt-dlp
			supports (I haven't tested all of them so I can't guarantee that they will work). Discs
			downloaded from these platforms will have the correct metadata and a custom disc and fragment
			texture based on the thumbnail of the video.
		</p>

		<h3 class="text-2xl my-4">Community disc packs</h3>

		<p>
			The desktop app allows you to download community disc packs directly from the app. This means
			you can easily add new discs to your resource pack without having to manually download and add
			them.
		</p>

		<h3 class="text-2xl my-4">Exports history</h3>

		<p>
			The desktop app keeps a history of all the exports you've done. This means you can easily
			re-import a pack without having to go through the process of finding it. Only the last 15
			exports are saved.
		</p>

		<h3 class="text-2xl my-4">Bulk importing</h3>

		<p>
			The desktop app allows you to bulk import discs from a CSV file. This is useful if you have a
			lot of discs you want to add to your resource pack. You can find more info <a
				class="text-blue-400"
				href={`${base}/docs/bulk-import`}>here</a
			>.
		</p>

		<h3 class="text-2xl my-4">Remote control</h3>

		<p>
			The desktop app allows you to control the plugin remotely. By logging in with your password
			you can edit the plugin configuration and discs on the fly. This is extremely useful for
			server owners who use the built-in resource pack hosting feature, as it allows you to update
			the resource pack without having to restart the server.
		</p>

		<div class="bg-orange-900 rounded-lg p-4 my-4">
			<div class="flex gap-4 items-start">
				<img src={warning} alt="warning" />
				<div id="popup">
					Keep in mind that on some server hosts you may not be able to use the desktop app due to
					the way the server is set up. If you can't connect to the server make sure the plugin Web
					API is enabled and that the port is open. If you're still having issues contact your
					server host. <br /> Do not contact me about this issue as I can't help you with it, unless
					you're absolutely sure that the issue is on my end. All issues related to connection problems
					will be marked as invalid and closed.
				</div>
			</div>
		</div>
	</div>
</div>

<style>
	#popup {
		text-shadow: 0 0 5px rgba(0, 0, 0, 0.5);
	}
</style>
