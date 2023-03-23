package me.spartacus04.jext.listener

import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryType

internal class InventoryMoveItemEvent : Listener {
    @EventHandler
    fun inventoryMoveItemEvent(e: InventoryMoveItemEvent) {
        if(e.destination.type != InventoryType.JUKEBOX) return

        try {
            val container = DiscContainer(e.item)
            DiscPlayer(container).play(e.destination.location!!)

        } catch (_: IllegalStateException) {
            return
        }
    }
}