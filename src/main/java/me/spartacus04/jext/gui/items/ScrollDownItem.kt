package me.spartacus04.jext.gui.items

import me.spartacus04.colosseum.gui.items.ScrollItemProvider
import me.spartacus04.jext.Jext
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

internal class ScrollDownItem(val horizontal: Boolean, scrollAmount: Int, private val player: Player, private val plugin: Jext, inventoryId: Int = 0) : ScrollItemProvider(scrollAmount, inventoryId) {
    override fun getItem(): ItemStack {
        val item = ItemStack(Material.RED_STAINED_GLASS_PANE)

        item.itemMeta = item.itemMeta!!.apply {
            this.setDisplayName(plugin.i18nManager!![player, if(horizontal) "scroll-right" else "scroll-down"]!!)
            if(!gui.canScroll(inventoryId, scrollAmount)) {
                lore = listOf(plugin.i18nManager!![player, "cant-scroll-further"]!!)
            }
        }

        return item
    }
}