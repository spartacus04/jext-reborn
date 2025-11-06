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
    fun playerJoinEvent(e: PlayerJoinEvent) = updateInventory(e.player.inventory)

    @EventHandler(ignoreCancelled = true)
    fun inventoryOpenEvent(e: InventoryOpenEvent) = updateInventory(e.inventory)

    @EventHandler(ignoreCancelled = true)
    fun pickUpItemEvent(e: EntityPickupItemEvent) {
        e.item.itemStack = updateItem(e.item.itemStack)
    }

    private fun updateInventory(inv: Inventory) {
        // Take a snapshot of the disabled slots if this inventory is a Crafter
        val crafter = if (inv.type == InventoryType.CRAFTER) inv.holder as? Crafter else null
        val crafterSnapshot: BooleanArray? = crafter?.let { c ->
            BooleanArray(9) { i -> c.isSlotDisabled(i) }
        }

        try {
            // Update item by item – do not overwrite the entire contents array
            val size = inv.size
            for (i in 0 until size) {
                val stack = inv.getItem(i) ?: continue
                val updated = updateItem(stack)

                // Only update if there is an actual change
                if (updated != stack) {
                    if (updated.type != stack.type || updated.amount != stack.amount || updated.itemMeta != stack.itemMeta) {
                        inv.setItem(i, updated)
                    }
                }
            }
        } finally {
            // Restore the exact previous disabled state for Crafter slots
            if (crafter != null && crafterSnapshot != null) {
                for (i in 0 until 9) {
                    crafter.setSlotDisabled(i, crafterSnapshot[i])
                }
            }
        }
    }

    private fun updateItem(itemStack: ItemStack): ItemStack {
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
        } else if (VERSION >= "1.19" && itemStack.type.isRecordFragment) {
            if (Disc.isCustomDisc(itemStack)) {
                val disc = Disc.fromItemstack(itemStack)

                return if (disc == null) {
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