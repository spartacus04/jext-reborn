package me.spartacus04.jext.listener

import me.spartacus04.jext.config.ConfigData.Companion.CONFIG
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.block.Jukebox
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryType

internal class InventoryMoveItemEvent : Listener {
    @EventHandler
    fun inventoryMoveItemEvent(e: InventoryMoveItemEvent) {
        if(e.isCancelled) return

        when(CONFIG.JUKEBOX_BEHAVIOUR) {
            "legacy-gui" -> {
                if(e.source.type == InventoryType.PLAYER && e.destination.type == InventoryType.JUKEBOX) {
                    e.isCancelled = true
                }
            }
            "gui" -> {
                if(e.source.type == InventoryType.PLAYER && e.destination.type == InventoryType.JUKEBOX) {
                    e.isCancelled = true
                }
            }
            else -> {
                if (e.destination.type != InventoryType.JUKEBOX) return

                val jukebox = e.destination.location!!.block.state as Jukebox
                if (jukebox.isPlaying) return

                try {
                    val container = DiscContainer(e.item)
                    container.play(e.destination.location!!)
                } catch (_: IllegalStateException) {
                }
            }
        }
    }
}