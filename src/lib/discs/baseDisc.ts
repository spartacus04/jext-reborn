import { get } from "svelte/store";
import { randomTextures } from "./textures";
import { discsStore } from "./discManager";

export class BaseDisc {
    public title: string = "";
    public author: string = "Unknown artist";

    public namespace: string = "";

    public discTexture: Blob = new Blob();
    public fragmentTexture: Blob = new Blob();

    public discTextureURL: string = "";
    public fragmentTextureURL: string = "";

    public tooltip: string = "";

    public redstonePower: number = 0;

    public creeperDroppable: boolean = false;
    public discLootTables: { [key: string]: string[] } = {};
    public fragmentLootTables: { [key: string]: string[] } = {};

    public setRedstonePower(power: number): void {
        this.redstonePower = power;

        if(this.redstonePower < 1) {
            this.redstonePower = 1;
        } else if(this.redstonePower > 15) {
            this.redstonePower = 15;
        }
    }

    public randomizeDiscData(): void {
        this.redstonePower = Math.floor(Math.random() * 15) + 1;
        this.creeperDroppable = Math.random() < 0.5;
    }

    async RerollTextures() {
        const textures = await randomTextures();

        this.setDiscTexture(textures.discTexture);
        this.setFragmentTexture(textures.fragmentTexture);
    }

    setDiscTexture(blob: Blob) {
        URL.revokeObjectURL(this.discTextureURL);
        this.discTexture = blob;
        this.discTextureURL = URL.createObjectURL(blob);
    }

    setFragmentTexture(blob: Blob) {
        URL.revokeObjectURL(this.fragmentTextureURL);
        this.fragmentTexture = blob;
        this.fragmentTextureURL = URL.createObjectURL(blob);
    }

    autoSetNamespace() {
        this.namespace = `${this.title}${this.author}`
            .replace(/[^a-zA-Z0-9]/g, '')
            .replaceAll('1', 'one')
            .replaceAll('2', 'two')
            .replaceAll('3', 'three')
            .replaceAll('4', 'four')
            .replaceAll('5', 'five')
            .replaceAll('6', 'six')
            .replaceAll('7', 'seven')
            .replaceAll('8', 'eight')
            .replaceAll('9', 'nine')
            .replaceAll('0', 'zero')
            .toLowerCase();

        const count = get(discsStore).filter(disc => disc.namespace.includes(this.namespace)).length;

        if(count > 0) {
            this.namespace = `${this.namespace}${count}`
                .replaceAll('1', 'one')
                .replaceAll('2', 'two')
                .replaceAll('3', 'three')
                .replaceAll('4', 'four')
                .replaceAll('5', 'five')
                .replaceAll('6', 'six')
                .replaceAll('7', 'seven')
                .replaceAll('8', 'eight')
                .replaceAll('9', 'nine')
                .replaceAll('0', 'zero');
        }

        while(get(discsStore).map(disc => disc.namespace).includes(this.namespace)) {
            this.namespace = `${this.namespace}zero`;
        }
    }
}