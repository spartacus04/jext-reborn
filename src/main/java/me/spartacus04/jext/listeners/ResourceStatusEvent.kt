package me.spartacus04.jext.listeners

import me.spartacus04.jext.State.CONFIG
import me.spartacus04.jext.State.LANG
import me.spartacus04.jext.State.PLUGIN
import me.spartacus04.jext.listeners.utils.JextListener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerResourcePackStatusEvent
import org.bukkit.scheduler.BukkitRunnable

internal class ResourceStatusEvent : JextListener() {
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
                if (status == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD && !CONFIG.FORCE_RESOURCE_PACK) {
                    return e.player.kickPlayer(LANG.getKey(
                        e.player,
                        "failed-download-kick-message"
                    ))
                }
            }
        }.runTaskLater(PLUGIN, 100)
    }
}