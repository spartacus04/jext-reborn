package me.spartacus04.jext.listener

import me.spartacus04.jext.config.ConfigData.Companion.CONFIG
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryType

internal class InventoryMoveItemEvent : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun inventoryMoveItemEvent(e: InventoryMoveItemEvent) {
        if(e.isCancelled) return

        if(CONFIG.JUKEBOX_GUI) {
            if(e.source.type == InventoryType.JUKEBOX || e.destination.type == InventoryType.JUKEBOX) {
                e.isCancelled = true
            }
        } else {
            if(e.destination.type != InventoryType.JUKEBOX) return

            try {
                val container = DiscContainer(e.item)
                DiscPlayer(container).play(e.destination.location!!)

            } catch (_: IllegalStateException) {
                return
            }
        }
    }
}