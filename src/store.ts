import { writable } from 'svelte/store';

import type { songData } from '@/config';


export const versionStore = writable(12);

export const discStore = writable<songData[]>([]);