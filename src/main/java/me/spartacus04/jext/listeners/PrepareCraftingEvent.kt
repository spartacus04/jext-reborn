package me.spartacus04.jext.listeners

import me.spartacus04.jext.JextState.DISCS
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.listeners.utils.JextListener
import me.spartacus04.jext.utils.Constants.JEXT_FRAGMENT_OUTPUT
import org.bukkit.block.Block
import org.bukkit.block.Crafter
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.CrafterInventory

internal class PrepareCraftingEvent : JextListener("1.19") {
    @EventHandler
    fun prepareCraftingEvent(e: PrepareItemCraftEvent) {
        if (e.inventory.result == null || e.inventory.result!!.type != JEXT_FRAGMENT_OUTPUT) return
        val block = e.inventory.holder as Block

        printDisabledSlots(block)
        val inventory = e.inventory.matrix.clone()

        printDisabledSlots(block)
        val isCustomDisc = inventory.any {
            return@any Disc.isCustomDisc(it)
        }

        printDisabledSlots(block)
        // check if every disc has same namespace, if they have the same namespace return the namespace else null
        val namespace = inventory.map {
            Disc.fromItemstack(it)?.namespace
        }.distinct().singleOrNull()

        printDisabledSlots(block)
        if (isCustomDisc && namespace != null) {
            e.inventory.result = DISCS[namespace]!!.discItemStack
        } else if (isCustomDisc) {
            e.inventory.result = null
        }
        printDisabledSlots(block)
    }

    fun printDisabledSlots(b: Block) {
        if(b !is Crafter) return

        val slots = arrayListOf<Number>()

        for(i in 1..b.inventory.size) {
            if(b.isSlotDisabled(i)) {
                slots.add(i)
            }
        }

        println("Disabled slots: $slots")
    }
}