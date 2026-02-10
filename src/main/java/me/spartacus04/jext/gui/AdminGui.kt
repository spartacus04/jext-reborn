package me.spartacus04.jext.gui

import me.spartacus04.colosseum.gui.Gui
import me.spartacus04.colosseum.gui.virtualInventory.VirtualInventory
import me.spartacus04.colosseum.gui.virtualInventory.VirtualInventoryInteractEvent
import me.spartacus04.jext.Jext
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

internal class AdminGui private constructor(size: Int, items: Array<ItemStack?>, val plugin: Jext) : VirtualInventory(size, items) {
    val originalDiscs = items.clone()

    override fun onPostUpdateEvent(event: VirtualInventoryInteractEvent) {
        event.slotChanges.forEach {
            set(it.virtualSlot, originalDiscs[it.virtualSlot])
        }
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