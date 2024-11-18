import { BaseDisc } from "./baseDisc";
import { fromArrayBuffer } from '@nbsjs/core';

export class NbsDisc extends BaseDisc {
    public nbsFile: Blob = new Blob();

    constructor(file: File, arrayBuffer: ArrayBuffer, isNew: boolean = true) {
        super();

        this.isNew = isNew;
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