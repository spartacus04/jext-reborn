package me.spartacus04.jext.utils

import me.spartacus04.jext.JextState
import me.spartacus04.jext.JextState.ASSETS_MANAGER
import me.spartacus04.jext.JextState.CONFIG
import org.bukkit.entity.Player

fun sendResourcePack(player: Player) {
    if(CONFIG.WEB_INTERFACE_API_ENABLED && CONFIG.RESOURCE_PACK_HOST) {
        val hostName = JextState.BASE_URL.getBaseUrl(player)

        player.setResourcePack("http://${hostName}:${CONFIG.WEB_INTERFACE_PORT}/resource-pack.zip", ASSETS_MANAGER.resourcePackHostedHash)
    }
}