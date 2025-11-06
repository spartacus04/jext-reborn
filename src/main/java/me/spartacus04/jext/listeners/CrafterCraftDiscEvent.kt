package me.spartacus04.jext.listeners

import me.spartacus04.colosseum.listeners.ColosseumListener
import me.spartacus04.colosseum.utils.version.VersionCompatibilityMin
import me.spartacus04.jext.Jext
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.utils.Constants.JEXT_FRAGMENT_OUTPUT
import org.bukkit.block.Crafter
import org.bukkit.event.EventHandler
import org.bukkit.event.block.CrafterCraftEvent

@VersionCompatibilityMin("1.21")
internal class CrafterCraftDiscEvent(val plugin: Jext) : ColosseumListener(plugin) {
    @EventHandler
    fun onCrafterCraft(e: CrafterCraftEvent) {
        if (e.result.type != JEXT_FRAGMENT_OUTPUT) return
        if (e.block !is Crafter) return

        val inventory = (e.block as Crafter).inventory.contents.clone()

        val isCustomDisc = inventory.any {
            return@any Disc.isCustomDisc(it)
        }

        // check if every disc has same namespace, if they have the same namespace return the namespace else null
        val namespace = inventory.map {
            Disc.fromItemstack(it)?.namespace
        }.distinct().singleOrNull()

        if (isCustomDisc && namespace != null) {
            e.result = plugin.discs[namespace]!!.discItemStack
        } else if (isCustomDisc) {
            e.isCancelled = true
        }
    }
}