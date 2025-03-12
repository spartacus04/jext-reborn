package me.spartacus04.jext.listeners

import me.spartacus04.jext.JextState.DISCS
import me.spartacus04.jext.JextState.VERSION
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.listeners.utils.JextListener
import me.spartacus04.jext.utils.Constants.FRAGMENT_LIST
import me.spartacus04.jext.utils.Constants.SOUND_MAP
import me.spartacus04.jext.utils.isRecordFragment
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.block.Crafter

internal class DiscUpdateEvent : JextListener() {
    @EventHandler(ignoreCancelled = true)
    fun playerJoinEvent(e : PlayerJoinEvent) = updateInventory(e.player.inventory)

    @EventHandler(ignoreCancelled = true)
    fun inventoryOpenEvent(e : InventoryOpenEvent) = updateInventory(e.inventory)

    @EventHandler(ignoreCancelled = true)
    fun pickUpItemEvent(e: EntityPickupItemEvent) {
        e.item.itemStack = updateItem(e.item.itemStack)
    }

    @Suppress("UnstableApiUsage")
    private fun updateInventory(inv: Inventory) {
        // If the inventory is a crafter, we need to keep track of the disabled slots
        val crafterArr = if(inv.type == InventoryType.CRAFTER) {
            val disabled = arrayListOf<Int>()
            val holder = inv.holder as? Crafter ?: return

            for(i in 0..inv.size-1) {
                if(holder.isSlotDisabled(i)) {
                    disabled.add(i)
                }
            }

            disabled
        } else null

        // We can't use inv.contents.forEachIndexed because it interferes with other inventory plugins
        val contents = inv.contents
        contents.forEachIndexed { i, it ->
            if (it != null) {
                contents[i] = updateItem(it)
            }
        }
        inv.contents = contents

        // Restore the disabled slots
        if(crafterArr != null) {
            val holder = inv.holder as? Crafter ?: return

            for(i in crafterArr) {
                holder.setSlotDisabled(i, true)
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
        } else if(VERSION >= "1.19" && itemStack.type.isRecordFragment) {
            if(Disc.isCustomDisc(itemStack)) {
                val disc = Disc.fromItemstack(itemStack)

                return if(disc == null) {
                    val stacks = arrayListOf(
                        FRAGMENT_LIST.map { ItemStack(it) },
                        DISCS.map { it.fragmentItemStack!! }
                    ).flatten()

                    stacks.random().apply {
                        amount = itemStack.amount
                    }
                } else {
                    disc.fragmentItemStack!!.apply {
                        amount = itemStack.amount
                    }
                }
            }
        }

        return itemStack
    }
}