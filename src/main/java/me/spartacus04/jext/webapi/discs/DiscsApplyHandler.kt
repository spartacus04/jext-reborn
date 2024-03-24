package me.spartacus04.jext.webapi.discs

import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.State.PLUGIN
import me.spartacus04.jext.webapi.utils.JextHttpHandler

class DiscsApplyHandler : JextHttpHandler(true) {
    override fun onPost(exchange: HttpExchange) {
        val body = exchange.requestBody.readBytes()
        val file = PLUGIN.dataFolder.resolve("resource-pack.zip")

        file.writeBytes(body)

        exchange.sendResponseHeaders(200, 0)
    }
}