package me.spartacus04.jext.webapi.discs

import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.State
import me.spartacus04.jext.State.DISCS
import me.spartacus04.jext.State.INTEGRATIONS
import me.spartacus04.jext.State.PLUGIN
import me.spartacus04.jext.gui.JukeboxGui
import me.spartacus04.jext.webapi.utils.JextHttpHandler

class DiscsApplyHandler : JextHttpHandler(true) {
    override fun onPost(exchange: HttpExchange) {
        val body = exchange.requestBody.readBytes()
        val file = PLUGIN.dataFolder.resolve("resource-pack.zip")

        file.writeBytes(body)

        DISCS.reloadDiscs {
            INTEGRATIONS.reloadDefaultIntegrations()
            JukeboxGui.loadFromFile()
        }

        exchange.sendResponseHeaders(200, 0)
    }
}