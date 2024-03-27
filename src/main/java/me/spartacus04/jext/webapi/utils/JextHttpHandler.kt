package me.spartacus04.jext.webapi.utils

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import me.spartacus04.jext.webapi.auth.ConnectHandler

internal open class JextHttpHandler(private val requireAuth: Boolean) : HttpHandler {
    override fun handle(exchange: HttpExchange) {
        val origin = exchange.requestHeaders["Origin"]?.firstOrNull() ?: "*"

        exchange.responseHeaders.add("Access-Control-Allow-Origin", origin)
        exchange.responseHeaders.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        exchange.responseHeaders.add("Access-Control-Allow-Headers", "Authorization, *")

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
        }

        when(exchange.requestMethod) {
            "POST" -> onPost(exchange)
            "GET" -> onGet(exchange)
            else -> notFound(exchange)
        }

        exchange.close()
    }

    fun notFound(exchange: HttpExchange) = exchange.sendResponseHeaders(404, 0)

    open fun onPost(exchange: HttpExchange) = notFound(exchange)

    open fun onGet(exchange: HttpExchange) = notFound(exchange)

    private fun isLoggedIn(exchange: HttpExchange): Boolean {
        val addr = exchange.remoteAddress.address.address.map { it.toInt() }.joinToString(".")
        val token = exchange.requestHeaders["Authorization"]?.first()?.replace("Bearer ", "") ?: return false

        return ConnectHandler.connectedHashMap[addr] == token
    }
}