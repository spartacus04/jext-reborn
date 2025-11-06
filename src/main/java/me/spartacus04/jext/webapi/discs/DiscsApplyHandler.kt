package me.spartacus04.jext.webapi.discs

import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.Jext
import me.spartacus04.jext.gui.JukeboxGui
import me.spartacus04.jext.webapi.utils.JextHttpHandler

internal class DiscsApplyHandler(plugin: Jext) : JextHttpHandler(plugin, true) {
    override fun onPost(exchange: HttpExchange) {
        val body = exchange.requestBody.readBytes()
        val file = plugin.dataFolder.resolve("resource-pack.zip")

        file.writeBytes(body)

        plugin.discs.reloadDiscs {
            plugin.integrations.reloadDefaultIntegrations()
            JukeboxGui.loadFromFile()

            plugin.server.onlinePlayers.forEach {
                val baseUrl = plugin.baseUrl.getUrl(it)
                it.setResourcePack("$baseUrl/resource-pack.zip", plugin.assetsManager.resourcePackHostedHash)
            }
        }

        exchange.sendResponseHeaders(204, 0)
    }
}