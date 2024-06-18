package me.spartacus04.jext.listeners

import me.spartacus04.jext.JextState.DISCS
import me.spartacus04.jext.listeners.utils.JextListener
import me.spartacus04.jext.utils.Constants.BRUSH_LOOT_TABLE_ITEMS
import me.spartacus04.jext.utils.Constants.ChanceStack
import org.bukkit.Material
import org.bukkit.block.BrushableBlock
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.random.Random

internal class BlockBrushEvent : JextListener("1.20") {
    @EventHandler
    fun onBlockBrush(event: PlayerInteractEvent) {
        val brushableBlock = getBrushableBlock(event) ?: return

        val items = ArrayList<ChanceStack>()

        DISCS.forEach {
            if(it.lootTables.contains(brushableBlock.lootTable!!.key.key))
                items.add(ChanceStack(it.lootTables[brushableBlock.lootTable!!.key.key]!!, it.discItemStack))

            if(it.fragmentLootTables.contains(brushableBlock.lootTable!!.key.key))
                items.add(ChanceStack(it.fragmentLootTables[brushableBlock.lootTable!!.key.key]!!, it.fragmentItemStack!!))
        }

        val lootTableItems = BRUSH_LOOT_TABLE_ITEMS[brushableBlock.lootTable!!.key.key]!!

        brushableBlock.lootTable = null

        val totalItems = lootTableItems + items.sumOf { it.chance }
        val random = (0 until totalItems).random(Random(brushableBlock.seed))

        if(random >= lootTableItems) {
            var remainingChance = random - lootTableItems

            for(item in items) {
                remainingChance -= item.chance

                if(remainingChance < 0) {
                    brushableBlock.item = item.stack
                    brushableBlock.update(true)
                    break
                }
            }
        }
    }

    private fun getBrushableBlock(event: PlayerInteractEvent) : BrushableBlock? {
        if(event.action != Action.RIGHT_CLICK_BLOCK) return null
        if(event.item == null || event.item!!.type != Material.BRUSH) return null

        val brushableBlock = event.clickedBlock!!.state as? BrushableBlock ?: return null

        if(brushableBlock.lootTable == null) return null

        return brushableBlock
    }
}