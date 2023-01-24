import { writable } from 'svelte/store';

import type { SongData } from '@/config';


export const versionStore = writable(12);

export const discStore = writable<SongData[]>([]);