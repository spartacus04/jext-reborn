import type { LayoutLoad } from './$types';

export const ssr = false;
export const prerender = false;
export const trailingSlash = 'never';

export const load: LayoutLoad = async ({ url }) => {
	const params = new URLSearchParams(url.search);

	const ip = params.get('ip');
	const port = params.get('port');

	return {
		connection: {
			ip,
			port: port ? +port : NaN
		}
	};
};
