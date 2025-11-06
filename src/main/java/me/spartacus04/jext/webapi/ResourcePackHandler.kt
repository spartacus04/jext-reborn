package me.spartacus04.jext.webapi

import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.Jext
import me.spartacus04.jext.webapi.utils.JextHttpHandler

internal class ResourcePackHandler(plugin: Jext) : JextHttpHandler(plugin, false) {
    private val file = plugin.dataFolder.resolve("resource-pack.zip")
    override fun onGet(exchange: HttpExchange) {
        if(!file.exists()) {
            return notFound(exchange)
        }

        exchange.sendResponseHeaders(200, file.length())

        file.inputStream().use { input ->
            exchange.responseBody.use { output ->
                input.copyTo(output)
            }
        }
    }
}