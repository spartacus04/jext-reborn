package me.spartacus04.jext.listener

import me.spartacus04.jext.SpigotVersion
import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.entity.Creeper
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import java.util.*

class CreeperDeathListener : Listener {
    private val droppableDiscs: MutableList<DiscContainer> = ArrayList()
    private val generator = Random()

    init {
        for (DISC in DISCS) {
            if (DISC.CREEPER_DROP) {
                droppableDiscs.add(DiscContainer(DISC))
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onCreeperDeath(event: EntityDeathEvent) {
        if (event.entity !is Creeper) return

        val disc = event.drops.find {
            it.type.isRecord
        }

        if (disc != null) {
            var discCount: Int = DiscContainer.SOUND_MAP.size

            discCount -= when(SpigotVersion.VERSION) {
                in 16 .. 17 -> 2

                18 -> 1

                in 19..Int.MAX_VALUE -> 0

                else ->3
            }

            val index = generator.nextInt(droppableDiscs.size + discCount)

            if (index < droppableDiscs.size) {
                event.drops.remove(disc)
                event.drops.add(droppableDiscs[index].discItem)
            }
        }
    }
}