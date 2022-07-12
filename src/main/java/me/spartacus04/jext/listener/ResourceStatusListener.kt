package me.spartacus04.jext.listener

import me.spartacus04.jext.config.ConfigData.Companion.CONFIG
import me.spartacus04.jext.config.ConfigData.Companion.LANG
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerResourcePackStatusEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

internal class ResourceStatusListener(private val plugin: JavaPlugin) : Listener {
    @EventHandler
    fun onResourceStatus(e: PlayerResourcePackStatusEvent) {
        val status = e.status

        object : BukkitRunnable() {
            override fun run() {
                if (status == PlayerResourcePackStatusEvent.Status.DECLINED && CONFIG.FORCE_RESOURCE_PACK) {
                    e.player.kickPlayer(LANG.RESOURCE_PACK_DECLINE_KICK_MESSAGE)
                    return
                }
                if (status == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD && !CONFIG.IGNORE_FAILED_DOWNLOAD) {
                    e.player.kickPlayer(LANG.FAILED_DOWNLOAD_KICK_MESSAGE)
                    return
                }
            }
        }.runTaskLater(plugin, 100)
    }
}