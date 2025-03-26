package me.spartacus04.jext.webapi.auth

import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.webapi.utils.JextHttpHandler

internal class DisconnectHandler : JextHttpHandler(true) {
    override fun onPost(exchange: HttpExchange) {
        val addr = exchange.remoteAddress.address.address.map { it.toInt() }.joinToString(".")

        ConnectHandler.connectedHashMap.remove(addr)

        exchange.sendResponseHeaders(204, 0)
    }
}