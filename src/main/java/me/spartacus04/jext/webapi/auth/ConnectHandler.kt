package me.spartacus04.jext.webapi.auth

import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.Jext
import me.spartacus04.jext.utils.constantTimeEquals
import me.spartacus04.jext.utils.generateSecureToken
import me.spartacus04.jext.webapi.utils.JextHttpHandler

internal class ConnectHandler(plugin: Jext) : JextHttpHandler(plugin, false) {
    override fun onPost(exchange: HttpExchange) {
        val addr = exchange.remoteAddress.address.address.map { it.toInt() }.joinToString(".")

        if(ConnectRateLimiter.isBlocked(addr)) {
            exchange.responseHeaders.add("Retry-After", ConnectRateLimiter.RETRY_AFTER.toString())
            exchange.sendResponseHeaders(429, 0)
            return
        }

        val body = readRequestBody(exchange, 4096)?.toString(Charsets.UTF_8) ?: return

        if(constantTimeEquals(body, plugin.config.WEB_INTERFACE_PASSWORD)) {
            ConnectRateLimiter.clear(addr)

            val token = generateSecureToken()

            SessionRegistry.create(addr, token)

            exchange.sendResponseHeaders(200, token.length.toLong())

            exchange.responseBody.use { os ->
                os.write(token.toByteArray())
            }
        } else {
            ConnectRateLimiter.recordFailure(addr)

            val response = "401 Unauthorized"
            exchange.sendResponseHeaders(401, response.length.toLong())

            exchange.responseBody.use { os ->
                os.write(response.toByteArray())
            }
        }
    }
}
