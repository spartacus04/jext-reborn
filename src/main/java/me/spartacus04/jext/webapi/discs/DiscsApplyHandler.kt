package me.spartacus04.jext.webapi.discs

import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.JextState.ASSETS_MANAGER
import me.spartacus04.jext.JextState.BASE_URL
import me.spartacus04.jext.JextState.DISCS
import me.spartacus04.jext.JextState.INTEGRATIONS
import me.spartacus04.jext.JextState.PLUGIN
import me.spartacus04.jext.gui.JukeboxGui
import me.spartacus04.jext.webapi.utils.JextHttpHandler

internal class DiscsApplyHandler : JextHttpHandler(true) {
    override fun onPost(exchange: HttpExchange) {
        val body = exchange.requestBody.readBytes()
        val file = PLUGIN.dataFolder.resolve("resource-pack.zip")

        file.writeBytes(body)

        DISCS.reloadDiscs {
            INTEGRATIONS.reloadDefaultIntegrations()
            JukeboxGui.loadFromFile()

            PLUGIN.server.onlinePlayers.forEach {
                val baseUrl = BASE_URL.getUrl(it)
                it.setResourcePack("$baseUrl/resource-pack.zip", ASSETS_MANAGER.resourcePackHostedHash)
            }
        }

        exchange.sendResponseHeaders(204, 0)
    }
}