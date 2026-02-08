package me.spartacus04.jext.gui.items

import me.spartacus04.colosseum.gui.items.ScrollItemProvider
import me.spartacus04.jext.Jext
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

internal class NextPageItem(scrollAmount: Int, private val player: Player, private val plugin: Jext, inventoryId: Int = 0) : ScrollItemProvider(scrollAmount, inventoryId) {
    override fun getItem(): ItemStack {
        val item = ItemStack(Material.GREEN_STAINED_GLASS_PANE)

        item.itemMeta = item.itemMeta!!.apply {
            this.setDisplayName(plugin.i18nManager!![player, "next-page"]!!)
            if(gui.virtualInventories[inventoryId]!!.providers.last().slot >= gui.virtualInventories[inventoryId]!!.inventory.size - 1) {
                lore = listOf(plugin.i18nManager!![player, "no-page"]!!)
            }
        }

        return item
    }
}