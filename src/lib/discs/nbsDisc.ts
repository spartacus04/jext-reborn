import { BaseDisc } from "./baseDisc";
import { fromArrayBuffer } from '@nbsjs/core';

export class NbsDisc extends BaseDisc {
    private nbsFile: Blob = new Blob();

    constructor(file: File, arrayBuffer: ArrayBuffer) {
        super();

        this.nbsFile = file;
		this.title = file.name;

        const { name, description, author, originalAuthor } = fromArrayBuffer(arrayBuffer);

        if(name) this.title = name;
        if(description) this.tooltip = description;
        if(author) this.author = author;
        if(originalAuthor) this.author = originalAuthor;

        this.autoSetNamespace();
    } 
}