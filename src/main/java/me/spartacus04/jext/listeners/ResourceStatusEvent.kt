package me.spartacus04.jext.listeners

import me.spartacus04.jext.JextState.CONFIG
import me.spartacus04.jext.JextState.LANG
import me.spartacus04.jext.JextState.SCHEDULER
import me.spartacus04.jext.listeners.utils.JextListener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerResourcePackStatusEvent

internal class ResourceStatusEvent : JextListener() {
    @EventHandler
    fun onResourceStatus(e: PlayerResourcePackStatusEvent) {
        val status = e.status

        SCHEDULER.runTaskLater({
            if(!CONFIG.FORCE_RESOURCE_PACK) return@runTaskLater

            when(status) {
                PlayerResourcePackStatusEvent.Status.DECLINED -> {
                    return@runTaskLater e.player.kickPlayer(LANG.getKey(
                        e.player,
                        "resource-pack-decline-kick-message"
                    ))
                }
                PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD -> {
                    return@runTaskLater e.player.kickPlayer(LANG.getKey(
                        e.player,
                        "failed-download-kick-message"
                    ))
                }

                else -> Unit
            }
        }, 100)
    }
}