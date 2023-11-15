package me.spartacus04.jext.listeners

import me.spartacus04.jext.State.DISCS
import me.spartacus04.jext.listeners.utils.JextListener
import me.spartacus04.jext.utils.Constants.CREEPER_DROPPABLE_DISCS
import org.bukkit.entity.Creeper
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack

internal class CreeperDeathEvent : JextListener() {
    @EventHandler(ignoreCancelled = true)
    fun onCreeperDeath(event: EntityDeathEvent) {
        println("test")

        if(event.entity !is Creeper) return

        val disc = event.drops.find {
            it.type.isRecord
        }


        if (disc != null) {
            event.drops.remove(disc)

            val discs = ArrayList<ItemStack>()

            discs.addAll(CREEPER_DROPPABLE_DISCS)
            DISCS.filter { it.creeperDrop }.map { it.discItemStack }.forEach {
                discs.add(it)
            }

            val discItem = discs.random()

            event.drops.addAll(listOf(discItem))
        }
    }
}