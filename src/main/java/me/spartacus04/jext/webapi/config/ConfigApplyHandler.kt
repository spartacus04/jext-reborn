package me.spartacus04.jext.webapi.config

import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.State.CONFIG
import me.spartacus04.jext.State.WEBSERVER
import me.spartacus04.jext.utils.JextMetrics
import me.spartacus04.jext.webapi.utils.JextHttpHandler

class ConfigApplyHandler : JextHttpHandler(true) {
    override fun onPost(exchange: HttpExchange) {
        val body = exchange.requestBody.bufferedReader().use { it.readText() }

        if(CONFIG.fromText(body)) {
            CONFIG.save()

            exchange.sendResponseHeaders(200, 0)
            JextMetrics.reloadMetrics()
            WEBSERVER.reload()
        } else {
            exchange.sendResponseHeaders(400, 0)
        }
    }
}