package me.spartacus04.jext.listener

import me.spartacus04.jext.disc.DiscLootTable
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

internal class CreeperDeathListener : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onCreeperDeath(event: EntityDeathEvent) {
        if(event.entity is Player) return

        val disc = event.drops.find {
            it.type.isRecord
        }

        if (disc != null) {
            event.drops.remove(disc)
            event.drops.add(DiscLootTable.getRandomDisc())
        }
    }
}
