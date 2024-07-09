<script lang="ts">
	let dialog: HTMLDialogElement;

	export const open = () => dialog.show();
	export const close = () => dialog.close();
	export const isOpen = () => dialog.open;
	export const toggle = () => (dialog.open ? close() : open());
</script>

<dialog bind:this={dialog}>
	<slot />
</dialog>
