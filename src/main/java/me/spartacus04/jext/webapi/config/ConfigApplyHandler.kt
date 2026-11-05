package me.spartacus04.jext.webapi.config

import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.Jext
import me.spartacus04.jext.utils.JextMetrics
import me.spartacus04.jext.webapi.utils.JextHttpHandler

internal class ConfigApplyHandler(plugin: Jext) : JextHttpHandler(plugin, true) {
    override fun onPost(exchange: HttpExchange) {
        val body = readRequestBody(exchange, 1024 * 1024)?.toString(Charsets.UTF_8) ?: return

        if(plugin.config.fromText(body)) {
            plugin.config.save()

            exchange.sendResponseHeaders(204, 0)
            JextMetrics.reloadMetrics()
            plugin.webServer.reload()
        } else {
            exchange.sendResponseHeaders(400, 0)
        }
    }
}