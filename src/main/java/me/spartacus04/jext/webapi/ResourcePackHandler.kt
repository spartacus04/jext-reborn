package me.spartacus04.jext.webapi

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import me.spartacus04.jext.State.PLUGIN

class ResourcePackHandler : HttpHandler {
    private val file = PLUGIN.dataFolder.resolve("resource-pack.zip")
    override fun handle(exchange: HttpExchange) {
        if(!file.exists()) {
            exchange.sendResponseHeaders(404, 0)
            exchange.close()
            return
        }

        exchange.sendResponseHeaders(200, file.length())
        file.inputStream().use { input ->
            exchange.responseBody.use { output ->
                input.copyTo(output)
            }
        }
        exchange.close()
    }
}