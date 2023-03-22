package me.spartacus04.jext.listener

import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscPlayer
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryType

internal class InventoryMoveItemEvent : Listener {
    @EventHandler
    fun inventoryMoveItemEvent(e: InventoryMoveItemEvent) {
        return

        if(e.source.location != null) {
            if(e.source.location!!.block.type == Material.JUKEBOX) {
                DiscPlayer(DiscContainer(e.item)).stop(e.source.location!!)
            }
        } else if(e.destination.location != null) {
            if(e.destination.location!!.block.type == Material.JUKEBOX) {
                DiscPlayer(DiscContainer(e.item)).play(e.destination.location!!)
            }
        }
    }
}