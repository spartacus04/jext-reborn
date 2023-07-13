package me.spartacus04.jext.listener

import me.spartacus04.jext.disc.DiscLootTable
import org.bukkit.entity.Creeper
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.loot.LootContext

internal class CreeperDeathListener : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onCreeperDeath(event: EntityDeathEvent) {
        if(event.entity !is Creeper) return

        val disc = event.drops.find {
            it.type.isRecord
        }

        if (disc != null) {
            event.drops.remove(disc)

            val discItem = DiscLootTable().populateLoot(
                null,
                LootContext.Builder(event.entity.location).build()
            )

            event.drops.addAll(discItem)
        }
    }
}
