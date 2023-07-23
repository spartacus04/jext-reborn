package me.spartacus04.jext.listener

import me.spartacus04.jext.config.ConfigData.Companion.CONFIG
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscPlayer
import org.bukkit.block.Jukebox
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryType

/**
 * The class `InventoryMoveItemEvent` is a listener class that is used to prevent players from moving items into jukeboxes while in gui mode
 * and to play discs when they are moved into a jukebox.
 *
 * @constructor Creates a new inventory move item event listener.
 */
internal class InventoryMoveItemEvent : Listener {
    /**
     * The function `inventoryMoveItemEvent` is an event listener that is called when an item is moved from one inventory to another.
     *
     * @param e The `e` parameter is of type `InventoryMoveItemEvent`. It represents the event that is being listened to.
     * @return Nothing is being returned. The function is of type `Unit`, which means it does not return any value.
     */
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
                if(e.destination.type == InventoryType.JUKEBOX) {
                    val jukebox = e.destination.location!!.block.state as Jukebox
                    if (jukebox.isPlaying) return

                    try {
                        val container = DiscContainer(e.item)
                        container.play(e.destination.location!!)
                    } catch (_: IllegalStateException) {
                    }
                } else if(e.source.type == InventoryType.JUKEBOX) {
                    try {
                        val container = DiscContainer(e.item)
                        DiscPlayer.stop(e.source.location!!, container.namespace)
                    } catch (_: IllegalStateException) {
                    }
                }
            }
        }
    }
}