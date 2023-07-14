package me.spartacus04.jext.listener

import me.spartacus04.jext.Updater
import me.spartacus04.jext.config.ConfigData.Companion.CONFIG
import me.spartacus04.jext.config.ConfigData.Companion.LANG
import me.spartacus04.jext.config.LanguageManager.Companion.CROWDIN_LINK
import me.spartacus04.jext.config.LanguageManager.Companion.CROWDIN_MESSAGE
import me.spartacus04.jext.config.LanguageManager.Companion.UPDATE_LINK
import me.spartacus04.jext.config.sendJEXTMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

/**
 * The class `PlayerJoinListener` is a listener class that is used to handle player join events.
 *
 * @property plugin the plugin instance
 * @constructor Creates a new player join event listener.
 */
internal class PlayerJoinListener(private val plugin: JavaPlugin) : Listener {
    /**
     * The function `onPlayerJoin` is an event listener that is called when a player joins the server.
     *
     * @param playerJoinEvent The `playerJoinEvent` parameter is of type `PlayerJoinEvent`. It represents the event that is being listened to.
     */
    @EventHandler
    fun onPlayerJoin(playerJoinEvent: PlayerJoinEvent) {
        if (playerJoinEvent.player.hasPermission("jext.notifyupdate")) {
            Updater(plugin).getVersion {
                if(it != plugin.description.version) {
                    playerJoinEvent.player.sendJEXTMessage("update-available")
                    playerJoinEvent.player.sendMessage(UPDATE_LINK)
                }
            }

            if(CONFIG.JUKEBOX_BEHAVIOUR == "legacy-gui") {
                playerJoinEvent.player.sendMessage("[§cJEXT§f] §cLegacy GUI is not supported anymore and will be removed in the next major update, please switch to gui or vanilla")
            }
        }

        if(!LANG.hasLanguage(playerJoinEvent.player.locale)) {
            playerJoinEvent.player.sendMessage(CROWDIN_MESSAGE)
            playerJoinEvent.player.sendMessage(CROWDIN_LINK)
        }
    }
}