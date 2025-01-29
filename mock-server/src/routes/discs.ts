import { Hono } from 'hono';
import { isLoggedIn, toArrayBuffer } from '../utils';
import { existsSync, readFileSync, writeFileSync } from 'fs';

const discsRouter = new Hono();

discsRouter.post('/discs/apply', async (c) => {
	if (!isLoggedIn(c)) {
		return c.text('401 Unauthorized', 401);
	}

	const body = await c.req.arrayBuffer();
	writeFileSync('./resource-pack.zip', new Uint8Array(body));

	return c.text('', 200);
});

discsRouter.post('/discs/applygeyser', async (c) => {
	if (!isLoggedIn(c)) {
		return c.text('401 Unauthorized', 401);
	}

	const body = await c.req.arrayBuffer();
	writeFileSync('./geyser.mcpack', new Uint8Array(body));

	return c.text('', 200);
});

discsRouter.get('/discs/read', async (c) => {
	if (!isLoggedIn(c)) {
		return c.text('401 Unauthorized', 401);
	}

	if (!existsSync('./resource-pack.zip')) {
		return c.text('', 404);
	}

	const file = readFileSync('./resource-pack.zip');
	return c.body(toArrayBuffer(file), 200);
});

export default discsRouter;
