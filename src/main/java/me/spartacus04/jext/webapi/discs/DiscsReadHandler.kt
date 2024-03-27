package me.spartacus04.jext.webapi.discs

import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.JextState.CONFIG
import me.spartacus04.jext.JextState.PLUGIN
import me.spartacus04.jext.webapi.utils.JextHttpHandler
import org.bukkit.Bukkit

internal class DiscsReadHandler : JextHttpHandler(true) {
    private val rpHosted = PLUGIN.dataFolder.resolve("resource-pack.zip")
    private val rpCache = PLUGIN.dataFolder.resolve("caches").resolve("${Bukkit.getServer().resourcePackHash}.zip")
    override fun onGet(exchange: HttpExchange) {
        if(CONFIG.RESOURCE_PACK_HOST) {
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