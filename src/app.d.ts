// See https://kit.svelte.dev/docs/types#app
// for information about these interfaces
// and what to do when importing types
declare namespace App {
	// interface Locals {}
	// interface PageData {}
	// interface Error {}
	// interface Platform {}
}

/// <reference types="svelte-gestures" />

// extend Console

interface Console {
	logs: {
		type: string;
		datetime: string;
		value: any[];
	}[];
	defaultLog: (...data: any[]) => void;
	defaultError: (...data: any[]) => void;
	defaultWarn: (...data: any[]) => void;
	defaultDebug: (...data: any[]) => void;
}