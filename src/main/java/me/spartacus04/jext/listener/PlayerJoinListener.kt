package me.spartacus04.jext.listener

import me.spartacus04.jext.Updater
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

internal class PlayerJoinListener(private val plugin: JavaPlugin) : Listener {
    @EventHandler
    fun onPlayerJoin(playerJoinEvent: PlayerJoinEvent) {
        if (playerJoinEvent.player.hasPermission("jext.notifyupdate")) {
            Updater(plugin, 103219).getVersion {
                if(it != plugin.description.version) {
                    playerJoinEvent.player.sendMessage("[§aJEXT§f] a new update is avaiable!")
                    playerJoinEvent.player.sendMessage("§6[§2https://www.spigotmc.org/resources/jukebox-extended-reborn.103219/§6]")
                }
            }
        }

        // TODO: PROMPT PLAYER IF LOCALE ISN'T TRANSLATED YET 
    }
}