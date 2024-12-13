import { Hono } from 'hono'
import { authRouter, configRouter, discsRouter } from './routes'

// Small mock implementation of the JEXT Plugin REST API
// It is badly written and dirty, but it works

const app = new Hono()

app.route('/', authRouter);
app.route('/', configRouter);
app.route('/', discsRouter);

export default app
