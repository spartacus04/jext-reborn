package me.spartacus04.jext.listener

import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.Material
import org.bukkit.block.BrushableBlock
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class BlockBrushEvent : Listener {
    private val items = ArrayList<ItemStack>()

    init {
        DISCS.forEach {
            val container = DiscContainer(it)

            if(it.LOOT_TABLES != null && it.LOOT_TABLES.contains("archaeology/trail_ruins_rare"))
                items.add(container.discItem)

            if(it.FRAGMENT_LOOT_TABLES != null && it.FRAGMENT_LOOT_TABLES.contains("archaeology/trail_ruins_rare"))
                items.add(container.fragmentItem)
        }
    }

    /**
     * The function `onBlockBrush` is an event listener that is called when a player right clicks a block with a brush.
     *
     * @param event The `event` parameter is of type `PlayerInteractEvent`. It represents the event that is being listened to.
     * @return Nothing is being returned. The function is of type `Unit`, which means it does not return any value.
     */
    @EventHandler
    fun onBlockBrush(event: PlayerInteractEvent) {
        if(event.action != Action.RIGHT_CLICK_BLOCK) return
        if(event.item == null || event.item!!.type != Material.BRUSH) return
        if(event.clickedBlock?.state !is BrushableBlock) return

        val brushableBlock = event.clickedBlock!!.state as BrushableBlock

        if(brushableBlock.lootTable == null) return
        brushableBlock.lootTable = null

        val totalItems = lootTableItems + items.size
        val random = (0 until totalItems).random(Random(brushableBlock.seed))

        if(random >= lootTableItems) {
            brushableBlock.item = items[random - lootTableItems]
            brushableBlock.update(true)
        }
    }

    companion object {
        private const val lootTableItems = 12
    }
}