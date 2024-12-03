import type { MusicDisc } from "$lib/discs/musicDisc";

export abstract class BaseDownloader {
    abstract download(url: string): Promise<MusicDisc[]>;
    abstract checkUrl(url: string): boolean;
}