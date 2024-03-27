package me.spartacus04.jext.webapi.discs

import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.JextState
import me.spartacus04.jext.JextState.INTEGRATIONS
import me.spartacus04.jext.integrations.unique.GeyserIntegration.Companion.GEYSER
import me.spartacus04.jext.webapi.utils.JextHttpHandler

internal class DiscsApplyGeyserHandler : JextHttpHandler(true) {
    override fun onPost(exchange: HttpExchange) {
        if(GEYSER == null) return notFound(exchange)

        val geyserPlugin = JextState.PLUGIN.server.pluginManager.getPlugin("Geyser-Spigot") ?: return notFound(exchange)

        val body = exchange.requestBody.readBytes()

        geyserPlugin.dataFolder.resolve("packs").resolve("jext_resources.mcpack").writeBytes(body)

        exchange.sendResponseHeaders(200, 0)

        INTEGRATIONS.registerIntegrations()
    }
}