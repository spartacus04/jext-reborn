package me.spartacus04.jext.listener

import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareItemCraftEvent

/**
 * The class `PrepareCraftingEvent` is a listener class that is used to craft fragments into discs.
 *
 * @constructor Creates a new disc replace event listener.
 */
internal class PrepareCraftingEvent : Listener {

    /**
     * The function `prepareCraftingEvent` is an event listener that is called when a player crafts an item.
     *
     * @param e The `e` parameter is of type `PrepareItemCraftEvent`. It represents the event that is being listened to.
     */
    @EventHandler
    fun prepareCraftingEvent(e: PrepareItemCraftEvent) {
        if(e.inventory.result == null || e.inventory.result!!.type != Material.MUSIC_DISC_5) return

        val isCustomDisc = e.inventory.matrix.any {
            try {
                DiscContainer(it).namespace
                true
            }
            catch (_: IllegalStateException) {
                false
            }
        }

        // check if every disc has same namespace, if they have the same namespace return the namespace else an empty string
        val namespace = e.inventory.matrix.map {
            try {
                DiscContainer(it).namespace
            }
            catch (_: IllegalStateException) {
                ""
            }
        }.distinct().singleOrNull()

        if (isCustomDisc && namespace != null) {
            e.inventory.result = DiscContainer(DISCS.first { it.DISC_NAMESPACE == namespace }).discItem
        } else if(isCustomDisc) {
            e.inventory.result = null
        }
    }
}