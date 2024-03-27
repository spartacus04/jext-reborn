package me.spartacus04.jext.listeners

import me.spartacus04.jext.State.CONFIG
import me.spartacus04.jext.State.LANG
import me.spartacus04.jext.State.PLUGIN
import me.spartacus04.jext.language.LanguageManager.Companion.CROWDIN_LINK
import me.spartacus04.jext.language.LanguageManager.Companion.CROWDIN_MESSAGE
import me.spartacus04.jext.language.LanguageManager.Companion.UPDATE_LINK
import me.spartacus04.jext.listeners.utils.JextListener
import me.spartacus04.jext.utils.Updater
import me.spartacus04.jext.utils.sendJEXTMessage
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

internal class PlayerJoinEvent : JextListener() {
    private val hostName = "http://" + Bukkit.getIp().ifEmpty {
        "localhost"
    }

    @EventHandler
    fun onPlayerJoin(playerJoinEvent: PlayerJoinEvent) {
        if(CONFIG.WEB_INTERFACE_API_ENABLED && CONFIG.RESOURCE_PACK_HOST) {
            playerJoinEvent.player.setResourcePack("${hostName}:${CONFIG.WEB_INTERFACE_PORT}/resource-pack.zip")
        }

        if (playerJoinEvent.player.hasPermission("jext.notifyupdate") && CONFIG.CHECK_FOR_UPDATES) {
            Updater().getVersion {
                if(it != PLUGIN.description.version) {
                    playerJoinEvent.player.sendJEXTMessage("update-available")
                    playerJoinEvent.player.sendMessage(UPDATE_LINK)
                }
            }
        }

        if(!LANG.hasLanguage(playerJoinEvent.player.locale)) {
            playerJoinEvent.player.sendMessage(CROWDIN_MESSAGE)
            playerJoinEvent.player.sendMessage(CROWDIN_LINK)
        }
    }
}