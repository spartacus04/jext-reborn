<script lang="ts">
	import { undo } from '$lib/assets';
</script>

<div class="flex h-full flex-col w-full overflow-x-hidden">
	<div class="flex flex-row bg-surface-background w-full py-4 px-4">
		<a href="../">
			<img src={undo} alt="back" class="h-4 w-4 mr-2" />
		</a>
		<b class="text-white text-xs uppercase">BULK IMPORT</b>
	</div>
	<div class="p-4 flex-col flex text-white overflow-y-auto w-full overflow-x-hidden">
		<h2 class="text-4xl mb-4">Bulk Import</h2>

		<p>
			Bulk importing is a feature that allows you to import multiple custom music discs at once.
			This feature is useful for server owners who want to add a large number of custom music discs
			to their server. Disc data is imported from a CSV file, which can be created using a
			spreadsheet program like Microsoft Excel or Google Sheets. The CSV file should contain the
			following columns:
		</p>
		<ul class="list-disc list-inside mt-4">
			<li>Contents: The absolute file path to the audio/nbs file.</li>
			<li>Name (optional): The name of the custom music disc.</li>
			<li>Author (optional): The author of the custom music disc.</li>
			<li>Description (optional): A description of the custom music disc.</li>
			<li>Disc Texture Path (optional): The absolute file path to the disc texture image.</li>
			<li>
				Fragment Texture Path (optional): The absolute file path to the fragment texture image.
			</li>
		</ul>

		<p class="mt-4">
			Even tought the name, author, description, disc texture path and fragment texture path are
			optional, the column must be present in the CSV file.
		</p>

		<h2 class="text-2xl my-4">CSV File standard</h2>

		<p>
			The CSV file has no specific standard, as such I implemented a standard that I think is the
			most user-friendly. The CSV file should be formatted as follows:
		</p>

		<ul class="list-disc list-inside mt-4">
			<li>The file is commma separed</li>
			<li>
				If a comma needs to be included in some of the fields, the whole field should be wrapped in
				double quotes
			</li>
		</ul>

		<p class="mt-4">Here is an example of a CSV file:</p>

		<div class="w-full overflow-x-auto">
			<pre
				class="bg-[#1e1e1e] p-2 rounded-lg mt-4 text-sm font-cascadia overflow-x-auto w-full"><code
					class="language-csv overflow-x-auto"
					>C:\Users\user\Documents\audio1.nbs,Disc 1,Author 1,Description 1,C:\Users\user\Documents\disc1.png,C:\Users\user\Documents\fragment1.png
C:\Users\user\Documents\audio2.nbs,Disc 2,Author 2,"Description 2, with a comma",C:\Users\user\Documents\disc2.png,C:\Users\user\Documents\fragment2.png
C:\Users\user\Documents\audio3.nbs,Disc 3,Author 3,Description 3,C:\Users\user\Documents\disc3.png,C:\Users\user\Documents\fragment3.png</code
				></pre>
		</div>
	</div>
</div>
