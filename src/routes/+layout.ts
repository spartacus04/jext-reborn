import type { LayoutLoad } from "./$types";

export const load: LayoutLoad = async ({ url }) => {
    // fetch url parameters
    const params = new URLSearchParams(url.search);

    const connect = !!params.get("c");
    const ip = params.get("ip");
    const port = params.get("port");

    return {
        server: {
            connect,
            ip,
            port: port ? (isNaN(+port) ? undefined : +port) : undefined
        }
    }
}