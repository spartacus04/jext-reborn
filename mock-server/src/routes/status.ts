import { Router } from 'express';

const router = Router();

router.get('/api/health', (_, res) => {
	res.status(200).json({ status: 'ok' });
});

export default router;