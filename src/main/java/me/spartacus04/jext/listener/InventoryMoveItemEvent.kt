package me.spartacus04.jext.listener

import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscPlayer
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryMoveItemEvent

internal class InventoryMoveItemEvent : Listener {
    @EventHandler
    fun inventoryMoveItemEvent(e: InventoryMoveItemEvent) {
        if(e.destination.location != null) {
            if(e.destination.location!!.block.type == Material.JUKEBOX) {
                DiscPlayer(DiscContainer(e.item)).play(e.destination.location!!)
            }
        }
    }
}