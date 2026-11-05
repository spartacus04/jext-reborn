package me.spartacus04.jext.webapi.discs

import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.Jext
import me.spartacus04.jext.webapi.utils.JextHttpHandler

internal class DiscsApplyGeyserHandler(plugin: Jext) : JextHttpHandler(plugin, true) {
    override fun onPost(exchange: HttpExchange) {
        val body = readRequestBody(exchange, 2000 * 1024 * 1024) ?: return

        plugin.geyserManager.applyResourcePack(body)
        exchange.sendResponseHeaders(204, 0)

        plugin.geyserManager.reloadGeyser()
    }
}