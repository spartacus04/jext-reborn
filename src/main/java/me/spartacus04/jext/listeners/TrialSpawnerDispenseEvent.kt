package me.spartacus04.jext.listeners

import me.spartacus04.jext.JextState.CONFIG
import me.spartacus04.jext.JextState.DISCS
import me.spartacus04.jext.listeners.utils.JextListener
import me.spartacus04.jext.utils.Constants
import me.spartacus04.jext.utils.isRecordFragment
import org.bukkit.Material
import org.bukkit.block.data.type.TrialSpawner
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockDispenseLootEvent
import kotlin.collections.contains

//TODO: test this
@Suppress("UnstableApiUsage")
internal class TrialSpawnerDispenseEvent : JextListener("1.21") {
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
            "chests/trial_chamber/consumables"
        } else {
            "chests/ominous/trial_chamber/consumable"
        }

        DISCS.forEach {
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
                            this.amount = if(CONFIG.DISC_LIMIT.containsKey(lootTable)) CONFIG.DISC_LIMIT[lootTable]!!
                            else if(CONFIG.DISC_LIMIT.containsKey("chests/*")) CONFIG.DISC_LIMIT["chests/*"]!!
                            else 2
                        }
                    }

                    break
                }
            }
        }
    }
}