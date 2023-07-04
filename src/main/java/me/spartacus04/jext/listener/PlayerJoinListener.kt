package me.spartacus04.jext.listener

import me.spartacus04.jext.Updater
import me.spartacus04.jext.config.ConfigData.Companion.LANG
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

internal class PlayerJoinListener(private val plugin: JavaPlugin) : Listener {
    @EventHandler
    fun onPlayerJoin(playerJoinEvent: PlayerJoinEvent) {
        if (playerJoinEvent.player.hasPermission("jext.notifyupdate")) {
            Updater(plugin).getVersion {
                if(it != plugin.description.version) {
                    playerJoinEvent.player.sendMessage("[§aJEXT§f] A new update is available!")
                    playerJoinEvent.player.sendMessage("§6[§2https://github.com/spartacus04/jext-reborn/releases/latest/§6]")
                }
            }
        }

        if(!LANG.hasLanguage(playerJoinEvent.player.locale)) {
            playerJoinEvent.player.sendMessage("[§aJEXT§f] It looks like your language isn't in JEXT yet. Why not contribute and add it yourself here?")
            playerJoinEvent.player.sendMessage("§6[§2https://crwd.in/jext-reborn§6]")
        }
    }
}