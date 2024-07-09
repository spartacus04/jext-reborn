<script lang="ts">
	export let title: string;
	export let description: string;
	export let url: string;
</script>

<a
	href={url}
	class="bg-black hover:bg-[#1e1e1e] p-4 text-white border-2 rounded-sm [&>*]:pointer-events-none [&>*]:select-none"
>
	<h3 class="text-lg font-semibold">{title}</h3>
	<p class="text-sm">{description}</p>
</a>
