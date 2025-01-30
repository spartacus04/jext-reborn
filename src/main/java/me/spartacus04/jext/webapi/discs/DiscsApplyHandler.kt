package me.spartacus04.jext.webapi.discs

import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.JextState.DISCS
import me.spartacus04.jext.JextState.INTEGRATIONS
import me.spartacus04.jext.JextState.PLUGIN
import me.spartacus04.jext.gui.JukeboxGui
import me.spartacus04.jext.utils.sendResourcePack
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
                sendResourcePack(it)
            }
        }

        exchange.sendResponseHeaders(200, 0)
    }
}