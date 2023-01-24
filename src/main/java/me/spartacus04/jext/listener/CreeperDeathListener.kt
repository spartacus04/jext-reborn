package me.spartacus04.jext.listener

import me.spartacus04.jext.disc.DiscLootTable
import org.bukkit.entity.Arrow
import org.bukkit.entity.Creeper
import org.bukkit.entity.Skeleton
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.inventory.InventoryOpenEvent

internal class CreeperDeathListener : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onCreeperDeath(event: EntityDeathEvent) {
        val lastDamageCause = event.entity.lastDamageCause ?: return
        if(lastDamageCause !is EntityDamageByEntityEvent) return
        val killerArrow = (EntityDamageByEntityEvent::class.java.cast(lastDamageCause)).damager
        if(killerArrow !is Arrow) return
        if (killerArrow.shooter !is Skeleton) return
        if (event.entity !is Creeper) return

        val disc = event.drops.find {
            it.type.isRecord
        }

        if (disc != null) {
            event.drops.remove(disc)
            event.drops.add(DiscLootTable.getRandomDisc())
        }
    }
}