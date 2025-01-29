package me.spartacus04.jext.listeners

import me.spartacus04.jext.JextState.DISCS
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.listeners.utils.JextListener
import me.spartacus04.jext.utils.Constants.JEXT_FRAGMENT_MATERIAL
import me.spartacus04.jext.utils.Constants.JEXT_FRAGMENT_OUTPUT
import org.bukkit.block.Crafter
import org.bukkit.event.EventHandler
import org.bukkit.event.block.CrafterCraftEvent

// TODO: test this
@Suppress("UnstableApiUsage")
internal class CrafterCraftDiscEvent : JextListener("1.21") {
    @EventHandler
    fun onCrafterCraft(e: CrafterCraftEvent) {
        if (e.result.type != JEXT_FRAGMENT_OUTPUT) return

        if(e.block !is Crafter) return

        val counter = mutableMapOf<String, Int>()
        var firstMatchName: String = ""

        (e.block as Crafter).inventory.forEach {
            if(it.type == JEXT_FRAGMENT_MATERIAL) {
                val namespace = if(Disc.isCustomDisc(it)) {
                    Disc.fromItemstack(it)!!.namespace
                } else {
                    "default"
                }

                counter[namespace] = counter.getOrDefault(namespace, 0) + it.amount

                if(counter[namespace]!! >= 9) {
                    firstMatchName = namespace
                }
            }
        }

        if (firstMatchName != "default" && firstMatchName != "") {
            e.result = DISCS[firstMatchName]!!.discItemStack
        } else if(firstMatchName == "") {
            e.isCancelled = true
        }
    }
}