import { Router } from 'express';

const router = Router();

router.get('/status', (_, res) => {
	res.status(200).json({ status: 'OK' });
});

export default router;