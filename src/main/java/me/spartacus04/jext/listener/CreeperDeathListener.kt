package me.spartacus04.jext.listener

import me.spartacus04.jext.disc.DiscLootTable
import org.bukkit.entity.Creeper
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.loot.LootContext

internal class CreeperDeathListener : Listener {
    /**
     * The function `onCreeperDeath` is an event listener that is called when a creeper dies.
     *
     * @param event The `event` parameter is of type `EntityDeathEvent`. It represents the event that is being listened to.
     * @return Nothing is being returned. The function is of type `Unit`, which means it does not return any value.
     */
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
