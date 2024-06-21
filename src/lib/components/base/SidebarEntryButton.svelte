<script lang="ts">
    export let icon: string;
    export let title: string;
</script>


<button class="flex p-2 pl-4 items-center hover:bg-surface-background-variant" on:click>
    <img class="min-w-12 w-12 aspect-square" src={icon} alt={title}>
    
    <h3 class="font-minecraft text-white text-lg ml-3">{title}</h3>
</button>