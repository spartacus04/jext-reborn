package me.spartacus04.jext.gui

import me.spartacus04.jext.JextState.DISCS
import me.spartacus04.jext.JextState.LANG
import me.spartacus04.jext.JextState.VERSION
import org.bukkit.entity.Player
import xyz.xenondevs.invui.inventory.VirtualInventory
import xyz.xenondevs.invui.inventory.event.ItemPostUpdateEvent
import xyz.xenondevs.invui.inventory.event.ItemPreUpdateEvent

internal class AdminGui(player: Player) : BaseGui(player) {
    override val inventory = VirtualInventory(
        DISCS.size() * if(VERSION >= "1.19") 2 else 1
    )

    override val inventoryName = LANG.getKey(targetPlayer, "jukebox")


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

    override fun onBedrockItemPreUpdate(event: ItemPreUpdateEvent) { }

    override fun onBedrockItemPostUpdate(event: ItemPostUpdateEvent) = onItemPostUpdate(event)
}