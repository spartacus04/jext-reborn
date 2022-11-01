package me.spartacus04.jext.listener

import me.spartacus04.jext.disc.DiscLootTable
import org.bukkit.entity.Creeper
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Skeleton
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.loot.LootContext

class CreeperDeathListener : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onCreeperDeath(event: EntityDeathEvent) {
        val lastDamageCause = event.entity.lastDamageCause ?: return
        val killer = lastDamageCause.entity
        if (event.entity !is Creeper || killer !is Skeleton) return

        val disc = event.drops.find {
            it.type.isRecord
        }

        if (disc != null) {
            val lootContext = LootContext.Builder(event.entity.location)
                .killer(killer as HumanEntity)
                .lootedEntity(event.entity)
                .build()

            event.drops.remove(disc)
            event.drops.addAll(DiscLootTable().populateLoot(null, lootContext))
        }
    }
}