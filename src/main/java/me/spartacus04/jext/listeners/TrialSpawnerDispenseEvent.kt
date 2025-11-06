package me.spartacus04.jext.listeners

import me.spartacus04.colosseum.listeners.ColosseumListener
import me.spartacus04.colosseum.utils.version.VersionCompatibilityMin
import me.spartacus04.jext.Jext
import me.spartacus04.jext.utils.Constants
import me.spartacus04.jext.utils.isRecordFragment
import org.bukkit.Material
import org.bukkit.block.data.type.TrialSpawner
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockDispenseLootEvent
import kotlin.collections.contains

@Suppress("UnstableApiUsage")
@VersionCompatibilityMin("1.21")
internal class TrialSpawnerDispenseEvent(val plugin: Jext) : ColosseumListener(plugin) {
    @EventHandler
    fun onVaultDispenseItem(e: BlockDispenseLootEvent) {
        if(e.block.type != Material.TRIAL_SPAWNER) return

        if(e.dispensedLoot[0].type != Material.TRIAL_KEY && e.dispensedLoot[0].type != Material.OMINOUS_TRIAL_KEY) {
            generateLoot(e, (e.block.blockData as TrialSpawner).isOminous)
        }
    }

    private fun generateLoot(e: BlockDispenseLootEvent, ominous: Boolean) {
        val items = ArrayList<Constants.ChanceStack>()
        val lootTable = if(ominous) {
            "spawners/ominous/trial_chamber/consumables"
        } else {
            "spawners/trial_chamber/consumables"
        }

        plugin.discs.forEach {
            if(it.lootTables.contains(lootTable))
                items.add(Constants.ChanceStack(it.lootTables[lootTable]!!, it.discItemStack))

            if(it.fragmentLootTables.contains(lootTable))
                items.add(
                    Constants.ChanceStack(
                        it.fragmentLootTables[lootTable]!!,
                        it.fragmentItemStack!!
                    )
                )
        }

        val lootTableItems = Constants.WEIGHTED_LOOT_TABLE_ITEMS[lootTable]!!

        val totalItems = lootTableItems + items.sumOf { it.chance }
        val random = (0 until totalItems).random()

        if(random >= lootTableItems) {
            var remainingChance = random - lootTableItems

            for(item in items) {
                remainingChance -= item.chance

                if(remainingChance < 0) {
                    e.dispensedLoot[0] = item.stack.apply {
                        if(this.type.isRecordFragment) {
                            this.amount = if(plugin.config.DISC_LIMIT.containsKey(lootTable)) plugin.config.DISC_LIMIT[lootTable]!!
                            else if(plugin.config.DISC_LIMIT.containsKey("chests/*")) plugin.config.DISC_LIMIT["chests/*"]!!
                            else 2
                        }
                    }

                    break
                }
            }
        }
    }
}