package me.spartacus04.jext.webapi.discs

import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.Jext
import me.spartacus04.jext.webapi.utils.JextHttpHandler

internal class DiscsApplyGeyserHandler(plugin: Jext) : JextHttpHandler(plugin, true) {
    override fun onPost(exchange: HttpExchange) {
        val body = exchange.requestBody.readBytes()

        plugin.geyserManager.applyResourcePack(body)
        exchange.sendResponseHeaders(204, 0)

        plugin.geyserManager.reloadGeyser()
    }
}