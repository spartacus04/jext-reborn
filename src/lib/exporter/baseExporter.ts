export interface ExporterOuput {
    javaRP: Blob;
    bedrockRP: Blob|undefined;
}

export abstract class BaseExporter {
    public abstract export(): Promise<ExporterOuput>;
    public abstract name: string;
    public abstract icon: string;
    public abstract subtitle: string;
};