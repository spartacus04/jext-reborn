import { type RendererObject } from "marked"

export const renderer : RendererObject = {
    blockquote: (quote : string) => `<blockquote class="blockquote m-2">${quote}</blockquote>`,
    heading: (text : string, level : number) => `<h${level} class="m-2 h${level}">${text}</h${level}>`,
    paragraph: (text : string) => `<p class="p m-2">${text}</p>`,
    link: (href : string, _ : string|null|undefined, text : string) => `<a href="${href}" class="a">${text}</a>`,
    list: (body : string, ordered : boolean, _ : number|"") => ordered ? `<ol class="m-4 ol list-decimal">${body}</ol>` : `<ul class="m-4 ul list-disc">${body}</ul>`,
    listitem: (text : string) => `<li class="m-4 li">${text}</li>`,
    image: (href : string, _: string|null|undefined, text : string) => `<img src="${href}" alt="${text}" class="img">`,
}