package me.spartacus04.jext.listeners

import me.spartacus04.jext.State.DISCS
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.listeners.utils.JextListener
import me.spartacus04.jext.utils.Constants.JEXT_FRAGMENT_OUTPUT
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.PrepareItemCraftEvent

internal class PrepareCraftingEvent : JextListener("1.19") {
    @EventHandler
    fun prepareCraftingEvent(e: PrepareItemCraftEvent) {
        if (e.inventory.result == null || e.inventory.result!!.type != JEXT_FRAGMENT_OUTPUT) return

        val isCustomDisc = e.inventory.matrix.any {
            return@any Disc.isCustomDisc(it)
        }

        // check if every disc has same namespace, if they have the same namespace return the namespace else an empty string
        val namespace = e.inventory.matrix.map {
            Disc.fromItemstack(it)!!.namespace
        }.distinct().singleOrNull()

        if (isCustomDisc && namespace != null) {
            e.inventory.result = DISCS[namespace]!!.discItemStack
        } else if (isCustomDisc) {
            e.inventory.result = null
        }
    }
}