package me.spartacus04.jext.listener

import me.spartacus04.jext.disc.DiscLootTable
import org.bukkit.entity.Creeper
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.loot.LootContext

/**
 * This class is used to listen to creeper death events.
 *
 * @constructor Creates a new creeper death event listener.
 */
internal class CreeperDeathListener : Listener {
    /**
     * The function `onCreeperDeath` is an event listener that is called when a creeper dies.
     *
     * @param event The `event` parameter is of type `EntityDeathEvent`. It represents the event that is being listened to.
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
