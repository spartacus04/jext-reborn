package me.spartacus04.jext.webapi.utils

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import me.spartacus04.jext.Jext
import me.spartacus04.jext.webapi.auth.SessionRegistry
import java.io.ByteArrayOutputStream

internal open class JextHttpHandler(val plugin: Jext, val requireAuth: Boolean) : HttpHandler {
    override fun handle(exchange: HttpExchange) {
        addCorsHeaders(exchange)

        if(exchange.requestMethod == "OPTIONS") {
            exchange.sendResponseHeaders(200, 0)
            return exchange.close()
        }

        if(requireAuth && !isLoggedIn(exchange)) {
            val response = "401 Unauthorized"
            exchange.sendResponseHeaders(401, response.length.toLong())

            exchange.responseBody.use { os ->
                os.write(response.toByteArray())
            }

            exchange.close()
            return
        }

        try {
            when(exchange.requestMethod) {
                "POST" -> onPost(exchange)
                "GET" -> onGet(exchange)
                else -> notFound(exchange)
            }
        } catch(_ : Exception) {
            // the handler already responded or the exchange was closed, nothing else to do
        }

        exchange.close()
    }

    fun notFound(exchange: HttpExchange) = exchange.sendResponseHeaders(404, 0)

    open fun onPost(exchange: HttpExchange) = notFound(exchange)

    open fun onGet(exchange: HttpExchange) = notFound(exchange)

    /**
     * Reads the request body, enforcing a maximum size.
     *
     * @param exchange The HTTP exchange.
     * @param limit The maximum allowed size of the body, in bytes.
     *
     * @return The body, or null if the body exceeds the limit, in which case a
     * `413 Payload Too Large` response has already been sent.
     */
    protected fun readRequestBody(exchange: HttpExchange, limit: Int): ByteArray? {
        // discard the declared length upfront when it's already over the limit
        val declaredLength = exchange.requestHeaders["Content-Length"]?.firstOrNull()?.toLongOrNull() ?: -1L

        if(declaredLength > limit) {
            return sendTooLarge(exchange)
        }

        val output = ByteArrayOutputStream(
            if(declaredLength >= 0) declaredLength.coerceAtMost(limit.toLong()).toInt() else 32
        )

        val chunk = ByteArray(8192)
        var total = 0

        while(true) {
            val read = exchange.requestBody.read(chunk)
            if(read <= 0) break

            total += read
            if(total > limit) {
                return sendTooLarge(exchange)
            }

            output.write(chunk, 0, read)
        }

        return output.toByteArray()
    }

    private fun sendTooLarge(exchange: HttpExchange): ByteArray? {
        val response = "413 Payload Too Large"
        exchange.sendResponseHeaders(413, response.length.toLong())

        exchange.responseBody.use { os ->
            os.write(response.toByteArray())
        }

        exchange.close()

        return null
    }

    /**
     * Adds CORS headers only when the request's Origin matches the allowlist, so that arbitrary
     * websites can't script the API from a visitor's browser.
     *
     * @param exchange The HTTP exchange.
     */
    private fun addCorsHeaders(exchange: HttpExchange) {
        val origin = exchange.requestHeaders["Origin"]?.firstOrNull() ?: return

        if(!isAllowedOrigin(origin)) return

        exchange.responseHeaders.add("Access-Control-Allow-Origin", origin)
        exchange.responseHeaders.add("Vary", "Origin")
        exchange.responseHeaders.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        exchange.responseHeaders.add("Access-Control-Allow-Headers", "Authorization")
    }

    private companion object {
        private val localOriginRegex = Regex("^https?://(localhost|127\\.0\\.0\\.1|\\[::1\\])(:\\d+)?$")

        private fun isAllowedOrigin(origin: String): Boolean {
            return origin == "https://spartacus04.github.io" || localOriginRegex.matches(origin)
        }
    }

    private fun isLoggedIn(exchange: HttpExchange): Boolean {
        val addr = exchange.remoteAddress.address.address.map { it.toInt() }.joinToString(".")
        val token = exchange.requestHeaders["Authorization"]?.first()?.replace("Bearer ", "") ?: return false

        return SessionRegistry.isValid(addr, token)
    }
}
