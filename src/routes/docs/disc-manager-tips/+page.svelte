<script lang="ts">
	import { base } from '$app/paths';
    import { spigotmc, modrinth, hangar, github, hand_helping, warning, flame, undo } from '$lib/assets';
</script>

<div class="flex h-full flex-col w-full">
	<div class="flex flex-row bg-surface-background w-full py-4 px-4">
		<a href="../">
			<img src={undo} alt="back" class="h-4 w-4 mr-2" />
		</a>
		<b class="text-white text-xs uppercase">DISC MANAGER TIPS & TRICKS</b>
	</div>
	<div class="p-4 flex-col flex text-white overflow-y-auto w-full">
        <ul class="list-disc ml-4">
            <li>Some components in the disc manager support drag & drop. Try it!</li>
            <li>You can edit multiple discs at once. This is useful for common elements like tooltips or icons.</li>
            <li>You can style your resource pack description with minecraft colors by using the <code>§</code> character</li>
            <li>Even tought it wasn't built for this, the <code>nbs</code> file format is supported!</li>
            <li>If you import a resource pack in the discs tab, if it was made for JEXT, it'll import all discs</li>
            <li>You can submit a request for custom community resource packs on github!</li>
            <li>The Web UI settings can be used to darken the generation screen</li>
        </ul>
    </div>
</div>

<style>
	#popup {
		text-shadow: 0 0 5px rgba(0, 0, 0, 0.5);
	}
</style>