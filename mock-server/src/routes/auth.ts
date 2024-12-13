import { Hono } from "hono";
import { bearerTokenMap } from "../state";
import { getConnInfo } from "hono/bun";
import { isLoggedIn } from "../utils";
import { config } from "./config";

const authRouter = new Hono();

authRouter.get("/connect", async (c) => {
    return c.text("Please use POST method");
});

authRouter.get("/disconnect", async (c) => {
    return c.text("Please use POST method");
});

authRouter.post("/connect", async (c) => {
    const body = await c.req.text();

    if (body === config["web-interface-password"]) {
        const bearerToken = Math.random().toString(36).substring(2);
        const ip = getConnInfo(c).remote.address!;

        bearerTokenMap.set(ip, bearerToken);

        c.status(200);
        return c.text(bearerToken);
    } else {
        c.status(401);
        return c.text("401 Unauthorized");
    }
});

authRouter.post("/disconnect", async (c) => {
    if(!isLoggedIn(c)) {
        c.status(401);
        return c.text("401 Unauthorized");
    }

    const ip = getConnInfo(c).remote.address!;

    bearerTokenMap.delete(ip);

    c.status(200);
    return c.text("");
});

authRouter.get("/health", async (c) => {
    const token = c.req.header("Authorization")?.replace("Bearer ", "");
    const ip = getConnInfo(c).remote.address!;

    if (token === undefined) {
        c.status(200);
    } else {
        if (bearerTokenMap.get(ip) === token) {
            c.status(200);
        } else {
            c.status(401);
        }
    }
});

export default authRouter;