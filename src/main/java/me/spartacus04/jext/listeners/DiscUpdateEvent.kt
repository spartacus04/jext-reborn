package me.spartacus04.jext.listeners

import me.spartacus04.jext.State.DISCS
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.listeners.utils.JextListener
import me.spartacus04.jext.utils.Constants.FRAGMENT_LIST
import me.spartacus04.jext.utils.Constants.SOUND_MAP
import me.spartacus04.jext.utils.isRecordFragment
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

internal class DiscUpdateEvent : JextListener() {
    @EventHandler(ignoreCancelled = true)
    fun playerJoinEvent(e : PlayerJoinEvent) = updateInventory(e.player.inventory)

    @EventHandler(ignoreCancelled = true)
    fun inventoryOpenEvent(e : InventoryOpenEvent) = updateInventory(e.inventory)

    @EventHandler(ignoreCancelled = true)
    fun pickUpItemEvent(e: EntityPickupItemEvent) {
        e.item.itemStack = updateItem(e.item.itemStack)
    }

    private fun updateInventory(inv: Inventory) {
        inv.contents.forEachIndexed { i, it ->
            if (it != null) {
                inv.setItem(i, updateItem(it))
            }
        }
    }

    private fun updateItem(itemStack: ItemStack) : ItemStack {
        if (itemStack.type.isRecord) {
            if (Disc.isCustomDisc(itemStack)) {
                val disc = Disc.fromItemstack(itemStack)

                return if (disc == null) {
                    val stacks = arrayListOf(
                        SOUND_MAP.keys.map { ItemStack(it) },
                        DISCS.map { it.discItemStack }
                    ).flatten()

                    stacks.random()
                } else {
                    disc.discItemStack
                }
            }
        } else if(itemStack.type.isRecordFragment) {
            if(Disc.isCustomDisc(itemStack)) {
                val disc = Disc.fromItemstack(itemStack)

                return if(disc == null) {
                    val stacks = arrayListOf(
                        FRAGMENT_LIST.map { ItemStack(it) },
                        DISCS.map { it.discItemStack }
                    ).flatten()

                    stacks.random().apply {
                        amount = itemStack.amount
                    }
                } else {
                    disc.discItemStack.apply {
                        amount = itemStack.amount
                    }
                }
            }
        }

        return itemStack
    }
}