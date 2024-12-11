// See https://kit.svelte.dev/docs/types#app
// for information about these interfaces
declare global {
	namespace App {
		// interface Error {}
		// interface Locals {}
		// interface PageData {}
		// interface PageState {}
		// interface Platform {}
	}
}

export {};

/// <reference types="svelte-gestures" />

declare global {
	interface Window {
		__TAURI__: Record<string, unknown>;
	}
}
