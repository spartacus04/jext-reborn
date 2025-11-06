package me.spartacus04.jext.gui

import me.spartacus04.jext.Jext
import me.spartacus04.jext.Jext.Companion.INSTANCE
import org.bukkit.entity.Player
import xyz.xenondevs.invui.inventory.VirtualInventory
import xyz.xenondevs.invui.inventory.event.ItemPostUpdateEvent
import xyz.xenondevs.invui.inventory.event.ItemPreUpdateEvent

internal class AdminGui private constructor(player: Player, inventory: VirtualInventory, inventoryName: String, val plugin: Jext) : BaseGui(player, inventory, inventoryName, plugin) {
    override fun onInit() {
        plugin.discs.forEachIndexed { i, it ->
            inventory.setItemSilently(i, it.discItemStack)
        }

        if(plugin.serverVersion >= "1.19") {
            plugin.discs.forEachIndexed {i, it ->
                inventory.setItemSilently(i + plugin.discs.size(), it.fragmentItemStack)
            }
        }
    }

    override fun onItemPreUpdate(event: ItemPreUpdateEvent) { }

    override fun onItemPostUpdate(event: ItemPostUpdateEvent) {
        inventory.setItemSilently(event.slot,
            if(event.slot >= plugin.discs.size())
                plugin.discs[event.slot - plugin.discs.size()].fragmentItemStack
            else
                plugin.discs[event.slot].discItemStack
        )
    }

    companion object {
        fun open(player: Player) = AdminGui(
            player,
            VirtualInventory(
                INSTANCE.discs.size() * if(INSTANCE.serverVersion >= "1.19") 2 else 1
            ),
            INSTANCE.i18nManager!![player, "jukebox"]!!,
            INSTANCE
        )
    }
}