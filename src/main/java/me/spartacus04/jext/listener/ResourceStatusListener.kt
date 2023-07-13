package me.spartacus04.jext.listener

import me.spartacus04.jext.config.ConfigData.Companion.CONFIG
import me.spartacus04.jext.config.ConfigData.Companion.LANG
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerResourcePackStatusEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

internal class ResourceStatusListener(private val plugin: JavaPlugin) : Listener {
    /**
     * The function `onResourceStatus` is an event listener that is called when a player accepts or declines a resource pack.
     *
     * @param e The `e` parameter is of type `PlayerResourcePackStatusEvent`. It represents the event that is being listened to.
     * @return Nothing is being returned. The function is of type `Unit`, which means it does not return any value.
     */
    @EventHandler
    fun onResourceStatus(e: PlayerResourcePackStatusEvent) {
        val status = e.status

        object : BukkitRunnable() {
            override fun run() {
                if (status == PlayerResourcePackStatusEvent.Status.DECLINED && CONFIG.FORCE_RESOURCE_PACK) {
                    return e.player.kickPlayer(LANG.getKey(
                        e.player,
                        "resource-pack-decline-kick-message"
                    ))
                }
                if (status == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD && !CONFIG.IGNORE_FAILED_DOWNLOAD) {
                    return e.player.kickPlayer(LANG.getKey(
                        e.player,
                        "failed-download-kick-message"
                    ))
                }
            }
        }.runTaskLater(plugin, 100)
    }
}