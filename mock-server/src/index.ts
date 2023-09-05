import { createApp } from './app';

(async () => {
	const app = await createApp();

	app.listen(3000, () => {
		console.log('Server is running on port 3000');
	});
});