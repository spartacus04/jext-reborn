package me.spartacus04.jext.listeners

import me.spartacus04.colosseum.listeners.ColosseumListener
import me.spartacus04.colosseum.utils.version.VersionCompatibilityMin
import me.spartacus04.jext.Jext
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.utils.Constants.JEXT_FRAGMENT_OUTPUT
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.PrepareItemCraftEvent

@VersionCompatibilityMin("1.19")
internal class PrepareCraftingEvent(val plugin: Jext) : ColosseumListener(plugin) {
    @EventHandler
    fun prepareCraftingEvent(e: PrepareItemCraftEvent) {
        if (e.inventory.result == null || e.inventory.result!!.type != JEXT_FRAGMENT_OUTPUT) return

        val inventory = e.inventory.matrix.clone()

        val isCustomDisc = inventory.any {
            return@any Disc.isCustomDisc(it)
        }

        // check if every disc has same namespace, if they have the same namespace return the namespace else null
        val namespace = inventory.map {
            Disc.fromItemstack(it)?.namespace
        }.distinct().singleOrNull()

        if (isCustomDisc && namespace != null) {
            e.inventory.result = plugin.discs[namespace]!!.discItemStack
        } else if (isCustomDisc) {
            e.inventory.result = null
        }
    }
}