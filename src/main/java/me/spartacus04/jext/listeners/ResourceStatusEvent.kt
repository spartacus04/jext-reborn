package me.spartacus04.jext.listeners

import me.spartacus04.colosseum.listeners.ColosseumListener
import me.spartacus04.jext.Jext
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerResourcePackStatusEvent

internal class ResourceStatusEvent(val plugin: Jext) : ColosseumListener(plugin) {
    @EventHandler
    fun onResourceStatus(e: PlayerResourcePackStatusEvent) {
        val status = e.status

        plugin.scheduler.runTaskLater({
            if(!plugin.config.FORCE_RESOURCE_PACK) return@runTaskLater

            when(status) {
                PlayerResourcePackStatusEvent.Status.DECLINED -> {
                    return@runTaskLater e.player.kickPlayer(plugin.i18nManager!![
                        e.player,
                        "resource-pack-decline-kick-message"
                    ])
                }
                PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD -> {
                    return@runTaskLater e.player.kickPlayer(plugin.i18nManager!![
                        e.player,
                        "failed-download-kick-message"
                    ])
                }

                else -> Unit
            }
        }, 100)
    }
}