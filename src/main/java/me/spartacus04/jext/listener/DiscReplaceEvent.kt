package me.spartacus04.jext.listener

import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscContainer.Companion.SOUND_MAP
import me.spartacus04.jext.disc.isRecordFragment
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

internal class DiscReplaceEvent : Listener {
    /**
     * The function `playerJoinEvent` is an event listener that is called when a player joins the server.
     *
     * @param e The `e` parameter is of type `PlayerJoinEvent`. It represents the event that is being listened to.
     * @return Nothing is being returned. The function is of type `Unit`, which means it does not return any value.
     */
    @EventHandler(ignoreCancelled = true)
    fun playerJoinEvent(e : PlayerJoinEvent) = updateInventory(e.player.inventory)

    /**
     * The function `inventoryOpenEvent` is an event listener that is called when a player opens an inventory.
     *
     * @param e The `e` parameter is of type `InventoryOpenEvent`. It represents the event that is being listened to.
     * @return Nothing is being returned. The function is of type `Unit`, which means it does not return any value.
     */
    @EventHandler(ignoreCancelled = true)
    fun inventoryOpenEvent(e : InventoryOpenEvent) = updateInventory(e.inventory)

    /**
     * The function `pickUpItemEvent` is an event listener that is called when a player picks up an item.
     *
     * @param e The `e` parameter is of type `EntityPickupItemEvent`. It represents the event that is being listened to.
     * @return Nothing is being returned. The function is of type `Unit`, which means it does not return any value.
     */
    @EventHandler(ignoreCancelled = true)
    fun pickUpItemEvent(e: EntityPickupItemEvent) {
        e.item.itemStack = updateItem(e.item.itemStack)
    }

    /**
     * The function `updateInventory` is a helper function that updates the inventory of a player.
     *
     * @param inv The `inv` parameter is of type `Inventory`. It represents the inventory that is being updated.
     * @return Nothing is being returned. The function is of type `Unit`, which means it does not return any value.
     */
    private fun updateInventory(inv: Inventory) {
        inv.contents.forEachIndexed { i, it ->
            if (it != null) {
                inv.setItem(i, updateItem(it))
            }
        }
    }

    /**
     * The function `updateItem` is a helper function that updates an item.
     *
     * @param itemStack The `itemStack` parameter is of type `ItemStack`. It represents the item that is being updated.
     * @return The function returns an `ItemStack` object.
     */
    private fun updateItem(itemStack: ItemStack) : ItemStack {
        if (itemStack.type.isRecord) {
            try {
                val container = DiscContainer(itemStack)
                val currentDisc = try {
                    DISCS.first { disc -> disc.DISC_NAMESPACE == container.namespace }
                } catch (_: NoSuchElementException) {
                    null
                }

                // check if any of the discs has a different namespace than the one in the config
                if(currentDisc == null) {
                    val stacks = arrayListOf(
                        SOUND_MAP.keys.map { material -> ItemStack(material) },
                        DISCS.map { disc -> DiscContainer(disc).discItem }
                    ).flatten()

                    return stacks.random()
                } else if(container != DiscContainer(currentDisc)) {
                    return DiscContainer(currentDisc).discItem
                }
            } catch (_: IllegalStateException) { }
        }

        if(itemStack.type.isRecordFragment) {
            try {
                val container = DiscContainer(itemStack)
                val currentDisc = try {
                    DISCS.first { disc -> disc.DISC_NAMESPACE == container.namespace }
                } catch (_: NoSuchElementException) {
                    null
                }

                // check if any of the discs has a different namespace than the one in the config
                if(currentDisc == null) {
                    val stacks = arrayListOf(
                        listOf(ItemStack(Material.DISC_FRAGMENT_5)),
                        DISCS.map { disc -> DiscContainer(disc).fragmentItem }
                    ).flatten()

                    return stacks.random().apply {
                        amount = itemStack.amount
                    }
                } else if(container != DiscContainer(currentDisc)) {
                    return DiscContainer(currentDisc).fragmentItem.apply {
                        amount = itemStack.amount
                    }
                }
            } catch (_: IllegalStateException) { }
        }

        return itemStack
    }
}