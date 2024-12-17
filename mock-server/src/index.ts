import { Hono } from 'hono'
import { authRouter, configRouter, discsRouter } from './routes'

// Small mock implementation of the JEXT Plugin REST API
// It is badly written and dirty, but it works

const app = new Hono()

// add Access-Control-Allow-Origin header to allow CORS to all origins
app.use(async (c, next) => {
    c.res.headers.set('Access-Control-Allow-Origin', '*')
    c.res.headers.set('Access-Control-Allow-Headers', '*')
    c.res.headers.set('Access-Control-Allow-Methods', '*')

    await next()
})

app.route('/', authRouter);
app.route('/', configRouter);
app.route('/', discsRouter);

export default app
