<script lang="ts">
	import { undo } from '$lib/assets';
</script>

<div class="flex h-full flex-col w-full">
	<div class="flex flex-row bg-surface-background w-full py-4 px-4">
		<a href="../">
			<img src={undo} alt="back" class="h-4 w-4 mr-2" />
		</a>
		<b class="text-white text-xs uppercase">COMMANDS & PERMISSIONS</b>
	</div>
	<div class="p-4 flex-col flex text-white overflow-y-auto w-full">
		<h2 class="text-4xl mb-4">Commands</h2>
		<table class="w-full border border-white">
			<thead>
				<tr>
					<th class="text-left border border-white p-2">Command & Usage</th>
					<th class="text-left border border-white p-2">Description</th>
					<th class="text-left border border-white p-2">Permission</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td class="border border-white p-2">/disc [name]</td>
					<td class="border border-white p-2">Get the specified custom music disc.</td>
					<td class="border border-white p-2">jext.disc</td>
				</tr>
				<tr>
					<td class="border border-white p-2">/discgive [selector] [name]</td>
					<td class="border border-white p-2">Give the specified custom music disc to player(s).</td
					>
					<td class="border border-white p-2">jext.disc.give</td>
				</tr>
				<tr>
					<td class="border border-white p-2"
						>/playmusic [selector] [name] <pitch> <volume></volume></pitch></td
					>
					<td class="border border-white p-2">Play specified custom music to selected player(s).</td
					>
					<td class="border border-white p-2">jext.playmusic</td>
				</tr>
				<tr>
					<td class="border border-white p-2"
						>/playat [X] [Y] [Z] <pitch> <volume></volume></pitch></td
					>
					<td class="border border-white p-2">Play specified custom music to selected player(s).</td
					>
					<td class="border border-white p-2">jext.playat</td>
				</tr>
				<tr>
					<td class="border border-white p-2">/stopmusic [selector] <name></name></td>
					<td class="border border-white p-2"
						>Stop specified custom music or all custom music for selected player(s).</td
					>
					<td class="border border-white p-2">jext.stopmusic</td>
				</tr>
				<tr>
					<td class="border border-white p-2">/jukeboxgui</td>
					<td class="border border-white p-2">Opens a player specific jukebox gui</td>
					<td class="border border-white p-2">jext.jukeboxgui</td>
				</tr>
				<tr>
					<td class="border border-white p-2">/fragment [name]</td>
					<td class="border border-white p-2">Gives the player a fragment</td>
					<td class="border border-white p-2">jext.fragment</td>
				</tr>
				<tr>
					<td class="border border-white p-2">/fragmentgive [selector] [name]</td>
					<td class="border border-white p-2">Gives the player a fragment</td>
					<td class="border border-white p-2">jext.fragment.give</td>
				</tr>
				<tr>
					<td class="border border-white p-2">/jext [subcommand] ...</td>
					<td class="border border-white p-2">Accesses the sub-menu commands</td>
					<td class="border border-white p-2">/</td>
				</tr>
				<tr>
					<td class="border border-white p-2">/jextreload</td>
					<td class="border border-white p-2">Reloads the plugin</td>
					<td class="border border-white p-2">jext.reload</td>
				</tr>
				<tr>
					<td class="border border-white p-2">/jextwebui [page]</td>
					<td class="border border-white p-2">Opens the webui</td>
					<td class="border border-white p-2">jext.webui</td>
				</tr>
				<tr>
					<td class="border border-white p-2">/jextadmingui</td>
					<td class="border border-white p-2">Opens the admin gui</td>
					<td class="border border-white p-2">jext.admingui</td>
				</tr>
			</tbody>
		</table>

        <h2 class="text-4xl mb-4 mt-8">Permissions</h2>


        <table class="w-full border border-white">
            <thead>
                <tr>
                    <th class="text-left border border-white p-2">Permission</th>
                    <th class="text-left border border-white p-2">Description</th>
                    <th class="text-left border border-white p-2">Default</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td class="border border-white p-2">jext.*</td>
                    <td class="border border-white p-2">Gives access to all Jukebox Extended commands.</td>
                    <td class="border border-white p-2">op</td>
                </tr>
                <tr>
                    <td class="border border-white p-2">jext.disc</td>
                    <td class="border border-white p-2">Allows player to access all custom disc.</td>
                    <td class="border border-white p-2">op</td>
                </tr>
                <tr>
                    <td class="border border-white p-2">jext.disc.give</td>
                    <td class="border border-white p-2">Allows player to give others custom disc.</td>
                    <td class="border border-white p-2">op</td>
                </tr>
                <tr>
                    <td class="border border-white p-2">jext.playmusic</td>
                    <td class="border border-white p-2">Allows player to play custom music to players.</td>
                    <td class="border border-white p-2">op</td>
                </tr>
                <tr>
                    <td class="border border-white p-2">jext.playat</td>
                    <td class="border border-white p-2">Allows player to play custom music to a location.</td>
                    <td class="border border-white p-2">op</td>
                </tr>
                <tr>
                    <td class="border border-white p-2">jext.stopmusic</td>
                    <td class="border border-white p-2">Allows player to stop custom music.</td>
                    <td class="border border-white p-2">op</td>
                </tr>
                <tr>
                    <td class="border border-white p-2">jext.reload</td>
                    <td class="border border-white p-2">Allows player to reload the plugin</td>
                    <td class="border border-white p-2">op</td>
                </tr>
                <tr>
                    <td class="border border-white p-2">jext.notifyupdate</td>
                    <td class="border border-white p-2">Notifies player of new update on join</td>
                    <td class="border border-white p-2">op</td>
                </tr>
                <tr>
                    <td class="border border-white p-2">jext.jukeboxgui</td>
                    <td class="border border-white p-2">Allows player to open the jukebox gui</td>
                    <td class="border border-white p-2">op</td>
                </tr>
                <tr>
                    <td class="border border-white p-2">jext.fragment</td>
                    <td class="border border-white p-2">Allows player to access all custom fragments</td>
                    <td class="border border-white p-2">op</td>
                </tr>
                <tr>
                    <td class="border border-white p-2">jext.fragment.give</td>
                    <td class="border border-white p-2">Allows player to give others custom fragments</td>
                    <td class="border border-white p-2">op</td>
                </tr>
                <tr>
                    <td class="border border-white p-2">jext.webui</td>
                    <td class="border border-white p-2">Allows player to access webui</td>
                    <td class="border border-white p-2">op</td>
                </tr>
                <tr>
                    <td class="border border-white p-2">jext.usejukebox</td>
                    <td class="border border-white p-2">Allows player to use jukeboxes.</td>
                    <td class="border border-white p-2">true</td>
                </tr>
                <tr>
                    <td class="border border-white p-2">jext.admingui</td>
                    <td class="border border-white p-2">Allows player to access admin gui</td>
                    <td class="border border-white p-2">op</td>
                </tr>
            </tbody>
        </table>
	</div>
</div>
