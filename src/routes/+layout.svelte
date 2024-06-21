<script lang="ts">
	import { SidebarContainer } from '$lib/components/base';
	import { swipe, type SwipeCustomEvent } from 'svelte-gestures';
    import '../app.postcss';
    import '../global.d.ts';

    const media = matchMedia('(max-width: 768px)');

    let isOpen = !media.matches;
    let isMobile = media.matches;
    
    media.addEventListener('change', (event) => {
        isMobile = event.matches;

        if(!isMobile) {
            isOpen = true;
        }
    });

    const swipeHandler = (event: SwipeCustomEvent) => {      
        if(!isMobile) return;

        if(event.detail.direction === 'left') {
            isOpen = false;
        } else if(event.detail.direction === 'right') {
            isOpen = true;
        }
    };
</script>

<div class="w-full h-full" use:swipe on:swipe={swipeHandler}>
    <SidebarContainer bind:isOpen>
        <slot />
    </SidebarContainer>
</div>