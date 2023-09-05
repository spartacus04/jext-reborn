import cors from 'cors';
import express, { Express } from 'express';
import fs from 'node:fs';

export const createApp = async () : Promise<Express> => {
	const app = express();

	app.use(express.json());
	app.use(cors());

	await Promise.all(
		fs.readdirSync(`${__dirname}/routes`).map(async (file) => {
			const route = await import(`./routes/${file}`);
			app.use(route);
		})
	);

	return app;
};