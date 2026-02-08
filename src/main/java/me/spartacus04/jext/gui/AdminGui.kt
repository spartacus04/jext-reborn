package me.spartacus04.jext.gui

import me.spartacus04.colosseum.gui.Gui
import me.spartacus04.colosseum.gui.virtualInventory.VirtualInventory
import me.spartacus04.colosseum.gui.virtualInventory.VirtualInventoryClickEvent
import me.spartacus04.jext.Jext
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class AdminGui private constructor(size: Int, items: Array<ItemStack?>, val plugin: Jext) : VirtualInventory(size, items) {
    override fun onPostUpdateEvent(clickEvent: VirtualInventoryClickEvent) {
        if(plugin.discs.size() == 0) return

        val disc = plugin.discs[clickEvent.virtualSlot % plugin.discs.size()]
        val isFragment = clickEvent.virtualSlot >= plugin.discs.size()
                && plugin.serverVersion >= "1.19"

        set(clickEvent.virtualSlot, if(isFragment) {
            disc.fragmentItemStack
        } else {
            disc.discItemStack
        })
    }

    companion object {
        fun buildAndOpen(plugin: Jext, player: Player): Gui {
            val discs = if(plugin.serverVersion >= "1.19") plugin.discs.flatMap { listOf(it.discItemStack, it.fragmentItemStack) }
            else plugin.discs.map { it.discItemStack }

            return Gui.buildAndOpen(plugin) {
                setTitle(
                    plugin.i18nManager!![player, "jukebox"]!!
                )
                setJextStructure(
                    player,
                    AdminGui(
                        discs.size,
                        discs.toTypedArray(),
                        plugin
                    )
                )
            }
        }
    }
}