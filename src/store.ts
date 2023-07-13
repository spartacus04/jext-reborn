import { writable } from 'svelte/store';

import type { SongData } from '@/config';
import { pack_icon } from './assets';


export const versionStore = writable(15);
export const nameStore = writable('your_pack_name');
export const iconStore = writable(pack_icon);

export const discStore = writable<SongData[]>([]);
