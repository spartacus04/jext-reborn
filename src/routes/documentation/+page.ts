import { marked } from 'marked';
import type { PageLoad } from './$types.js';
import { renderer } from '$lib/marked-renderer.js';

export const load: PageLoad = async ({ fetch }) => {
    const res = await fetch('https://raw.githubusercontent.com/spartacus04/jext-reborn/master/README.md');

    if (res.ok) {
        const text = await res.text();

        marked.use({ renderer });

        const html = marked(text)

        return {
            status: res.status,
            props: {
                html
            }
        };
    }

    return {
        status: 404
    };
}