package me.spartacus04.jext.gui

import me.spartacus04.jext.JextState.DISCS
import me.spartacus04.jext.JextState.LANG
import me.spartacus04.jext.JextState.VERSION
import org.bukkit.entity.Player
import xyz.xenondevs.invui.inventory.VirtualInventory
import xyz.xenondevs.invui.inventory.event.ItemPostUpdateEvent
import xyz.xenondevs.invui.inventory.event.ItemPreUpdateEvent

internal class AdminGui private constructor(player: Player, inventory: VirtualInventory, inventoryName: String) : BaseGui(player, inventory, inventoryName) {
    override fun onInit() {
        DISCS.forEachIndexed { i, it ->
            inventory.setItemSilently(i, it.discItemStack)
        }

        if(VERSION >= "1.19") {
            DISCS.forEachIndexed {i, it ->
                inventory.setItemSilently(i + DISCS.size(), it.fragmentItemStack)
            }
        }
    }

    override fun onItemPreUpdate(event: ItemPreUpdateEvent) { }

    override fun onItemPostUpdate(event: ItemPostUpdateEvent) {
        inventory.setItemSilently(event.slot,
            if(event.slot >= DISCS.size())
                DISCS[event.slot - DISCS.size()].fragmentItemStack
            else
                DISCS[event.slot].discItemStack
        )
    }

    companion object {
        fun open(player: Player) = AdminGui(
            player,
            VirtualInventory(
                DISCS.size() * if(VERSION >= "1.19") 2 else 1
            ),
            LANG.getKey(player, "jukebox")
        )
    }
}