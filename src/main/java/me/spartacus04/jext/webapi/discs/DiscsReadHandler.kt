package me.spartacus04.jext.webapi.discs

import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.Jext
import me.spartacus04.jext.webapi.utils.JextHttpHandler

internal class DiscsReadHandler(plugin: Jext) : JextHttpHandler(plugin, true) {
    private val rpHosted = plugin.dataFolder.resolve("resource-pack.zip")
    private val rpCache = plugin.dataFolder.resolve("caches").resolve("${plugin.assetsManager.resourcePackHash.ifBlank { "current" }}.zip")

    override fun onGet(exchange: HttpExchange) {
        if(plugin.config.RESOURCE_PACK_HOST) {
            if(!rpHosted.exists()) {
                return notFound(exchange)
            }

            exchange.sendResponseHeaders(200, rpHosted.length())

            rpHosted.inputStream().use { input ->
                exchange.responseBody.use { output ->
                    input.copyTo(output)
                }
            }

            return
        }

        if(!rpCache.exists()) {
            return notFound(exchange)
        }

        exchange.sendResponseHeaders(200, rpCache.length())

        rpCache.inputStream().use { input ->
            exchange.responseBody.use { output ->
                input.copyTo(output)
            }
        }
    }
}