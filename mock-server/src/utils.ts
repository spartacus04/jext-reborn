import { Context } from 'hono';
import { getConnInfo } from 'hono/bun';
import { bearerTokenMap } from './state';

export const isLoggedIn = (c: Context): boolean => {
	const token = c.req.header('Authorization')?.replace('Bearer ', '');
	const ip = getConnInfo(c).remote.address!;

	return bearerTokenMap.get(ip) === token;
};

export const toArrayBuffer = (buf: Buffer): ArrayBuffer => {
	const ab = new ArrayBuffer(buf.length);
	const view = new Uint8Array(ab);

	for (let i = 0; i < buf.length; ++i) {
		view[i] = buf[i];
	}

	return ab;
};
