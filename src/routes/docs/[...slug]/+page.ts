import { marked } from 'marked';

export const load = async ({ params, fetch }) => {
	const { slug } = params;

	const page = await fetch(`/docssrc/${slug}.md`);

	if (!page.ok) {
		return {
			isFound: false,
			text: 'Page not found'
		};
	}

	const text = await page.text();

	return {
		isFound: true,
		text: marked(text)
	};
};
