package me.spartacus04.jext.webapi.discs

import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.JextState.GEYSER
import me.spartacus04.jext.webapi.utils.JextHttpHandler

internal class DiscsApplyGeyserHandler : JextHttpHandler(true) {
    override fun onPost(exchange: HttpExchange) {
        val body = exchange.requestBody.readBytes()

        GEYSER.applyResourcePack(body)
        exchange.sendResponseHeaders(204, 0)

        GEYSER.reloadGeyser()
    }
}