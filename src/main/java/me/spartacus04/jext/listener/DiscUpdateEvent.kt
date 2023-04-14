package me.spartacus04.jext.listener

import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscContainer.Companion.SOUND_MAP
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

internal class DiscUpdateEvent : Listener {
    @EventHandler(ignoreCancelled = true)
    fun playerJoinEvent(e : PlayerJoinEvent) {
        updateInventory(e.player.inventory)
    }

    @EventHandler(ignoreCancelled = true)
    fun inventoryOpenEvent(e : InventoryOpenEvent) {
        updateInventory(e.inventory)
    }

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
            try {
                val container = DiscContainer(itemStack)

                // check if any of the discs has a different namespace than the one in the config
                if(DISCS.any { disc-> disc.DISC_NAMESPACE == container.namespace }) {
                    // check if title, author and lore is same
                    val disc = DISCS.first { disc -> disc.DISC_NAMESPACE == container.namespace }

                    if (disc.LORE != container.getProcessedLores()) {
                        return DiscContainer(disc).discItem.apply {
                            itemMeta = itemMeta?.apply {
                                setDisplayName(itemStack.itemMeta?.displayName)
                            }
                        }
                    }
                } else {
                    val stacks = arrayListOf(
                        SOUND_MAP.keys.map { material -> ItemStack(material) },
                        DISCS.map { disc -> DiscContainer(disc).discItem }
                    ).flatten()

                    return stacks.random()
                }
            } catch (_: IllegalStateException) { }
        }

        if(itemStack.type.isRecordFragment) {
            try {
                val container = DiscContainer(itemStack)

                // check if any of the discs has a different namespace than the one in the config
                if(DISCS.any { disc-> disc.DISC_NAMESPACE == container.namespace }) {
                    // check if title, author and lore is same
                    val disc = DISCS.first { disc -> disc.DISC_NAMESPACE == container.namespace }

                    if (disc.LORE != container.getProcessedLores()) {
                        return DiscContainer(disc).fragmentItem.apply {
                            amount = itemStack.amount
                            itemMeta = itemMeta?.apply {
                                setDisplayName(itemStack.itemMeta?.displayName)
                            }
                        }
                    }
                } else {
                    val stacks = arrayListOf(
                        SOUND_MAP.keys.map { material -> ItemStack(material) },
                        DISCS.map { disc -> DiscContainer(disc).fragmentItem }
                    ).flatten()

                    return stacks.random().apply {
                        amount = itemStack.amount
                    }
                }
            } catch (_: IllegalStateException) { }
        }

        return itemStack
    }
}