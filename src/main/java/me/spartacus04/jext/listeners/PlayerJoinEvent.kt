package me.spartacus04.jext.listeners

import me.spartacus04.colosseum.i18n.sendI18nInfo
import me.spartacus04.colosseum.listeners.ColosseumListener
import me.spartacus04.colosseum.logging.sendInfo
import me.spartacus04.colosseum.logging.sendUrl
import me.spartacus04.jext.Jext
import me.spartacus04.jext.language.DefaultMessages.CROWDIN_LINK
import me.spartacus04.jext.language.DefaultMessages.CROWDIN_MESSAGE
import me.spartacus04.jext.language.DefaultMessages.UPDATE_LINK
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

internal class PlayerJoinEvent(val plugin: Jext) : ColosseumListener(plugin) {
    @EventHandler
    fun onPlayerJoin(playerJoinEvent: PlayerJoinEvent) {
        val player = playerJoinEvent.player

        if(plugin.config.WEB_INTERFACE_API_ENABLED && plugin.config.RESOURCE_PACK_HOST) {
            if(!plugin.dataFolder.resolve("resource-pack.zip").exists()) return

            val baseUrl = plugin.baseUrl.getUrl(player)

            player.setResourcePack("$baseUrl/resource-pack.zip", plugin.assetsManager.resourcePackHostedHash)
        }

        if (player.hasPermission("jext.notifyupdate") && plugin.config.CHECK_FOR_UPDATES) {
            plugin.checkForUpdates("spartacus04/jext-reborn") {
                if(it != plugin.description.version) {
                    player.sendI18nInfo(plugin, "update-available")
                    player.sendUrl(plugin, UPDATE_LINK)
                }
            }
        }

        if(!plugin.i18nManager!!.hasLanguage(playerJoinEvent.player.locale)) {
            player.sendInfo(plugin, CROWDIN_MESSAGE)
            player.sendUrl(plugin, CROWDIN_LINK)
        }
    }
}