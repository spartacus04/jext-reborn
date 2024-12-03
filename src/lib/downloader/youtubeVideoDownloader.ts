import type { BaseDownloader } from "./baseDownloader";

export class YoutubeVideoDownloader implements BaseDownloader {
    // This includes normal youtube links, shortened links, embed links and shorts links
    private videoRegex = ;

    checkUrl(url: string): boolean {
        return this.videoRegex.test(url);
    }


    download(url: string): Promise<MusicDisc[]> {
        // get video id from url, it's the 3rd group in the regex
        const videoId = this.videoRegex.exec(url)![3];
    }
}