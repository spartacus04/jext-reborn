package me.spartacus04.jext.webapi.auth

import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.webapi.utils.JextHttpHandler

internal class HealthHandler : JextHttpHandler(false) {
    override fun onGet(exchange: HttpExchange) {
        val token = exchange.requestHeaders["Authorization"]?.first()?.replace("Bearer ", "")

        if(token == null) {
            exchange.sendResponseHeaders(204, 0)
        } else {
            val addr = exchange.remoteAddress.address.address.map { it.toInt() }.joinToString(".")

            if(ConnectHandler.connectedHashMap[addr] == token) {
                exchange.sendResponseHeaders(204, 0)
            } else {
                exchange.sendResponseHeaders(401, 0)
            }
        }
    }
}