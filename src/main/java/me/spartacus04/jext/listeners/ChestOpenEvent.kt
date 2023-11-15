package me.spartacus04.jext.listeners

import me.spartacus04.jext.State.CONFIG
import me.spartacus04.jext.State.DISCS
import me.spartacus04.jext.State.VERSION
import me.spartacus04.jext.listeners.utils.JextListener
import me.spartacus04.jext.utils.Constants.DEFAULT_DISCS_LOOT_TABLE
import me.spartacus04.jext.utils.isRecordFragment
import org.bukkit.Material
import org.bukkit.block.Chest
import org.bukkit.entity.EntityType
import org.bukkit.entity.minecart.StorageMinecart
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.vehicle.VehicleDestroyEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import kotlin.random.Random
import me.spartacus04.jext.utils.Constants.ChanceStack
import me.spartacus04.jext.utils.Constants.DEFAULT_FRAGMENTS_LOOT_TABLE

internal class ChestOpenEvent : JextListener() {

    /**
     * The function generates items in an inventory based on certain conditions and probabilities.
     *
     * @param inventory The `inventory` parameter is an object of type `Inventory`. It represents the inventory where the
     * items will be generated and stored.
     * @param key The `key` parameter is a string that represents a specific key or identifier for the inventory. It is
     * used to determine the maximum amount of discs and fragments that can be generated for that specific inventory. The
     * `key` is used to look up values in the `CONFIG.DISC_LIMIT` and `CONFIG.FRAGMENT_LIMIT` maps.
     */
    private fun generateItems(inventory: Inventory, key: String) {
        val discsMap = DEFAULT_DISCS_LOOT_TABLE.toMutableMap()
        val discFragmentMap = DEFAULT_FRAGMENTS_LOOT_TABLE.toMutableMap()

        DISCS.forEach {
            it.lootTables.forEach { lootTable ->
                if (discsMap.containsKey(lootTable.key)) {
                    discsMap[lootTable.key]!!.add(ChanceStack(lootTable.value, DISCS[lootTable.key]!!.discItemStack))
                } else {
                    discsMap[lootTable.key] = arrayListOf(ChanceStack(lootTable.value, DISCS[lootTable.key]!!.discItemStack))
                }
            }

            if(VERSION < "1.19") return@forEach

            it.fragmentLootTables.forEach { lootTable ->
                if (discFragmentMap.containsKey(lootTable.key)) {
                    discFragmentMap[lootTable.key]!!.add(ChanceStack(lootTable.value, DISCS[lootTable.key]!!.fragmentItemStack!!))
                } else {
                    discFragmentMap[lootTable.key] = arrayListOf(ChanceStack(lootTable.value, DISCS[lootTable.key]!!.fragmentItemStack!!))
                }
            }
        }

        if(discsMap.containsKey(key)) {
            if(inventory.any { it != null && it.type.isRecord }) {
                inventory.storageContents = inventory.storageContents.map { itemstack ->
                    if(itemstack != null && itemstack.type.isRecord) {
                        ItemStack(Material.AIR)
                    } else {
                        itemstack
                    }
                }.toTypedArray()
            }

            val discMaxAmount = if(CONFIG.DISC_LIMIT.containsKey(key)) CONFIG.DISC_LIMIT[key]!!
            else if(CONFIG.DISC_LIMIT.containsKey("chests/*")) CONFIG.DISC_LIMIT["chests/*"]!!
            else 2

            var discAmount = Random.nextInt(0, discMaxAmount + 1)

            discsMap[key]?.apply{ shuffle() }?.forEach { chanceStack ->
                if (Random.nextInt(0, 1001) < chanceStack.chance) {
                    var size = inventory.size

                    while (size > 0 && discAmount > 0) {

                        val slot = Random.nextInt(0, size)
                        val item = inventory.getItem(slot)

                        if (item == null || item.type == Material.AIR) {
                            inventory.setItem(slot, chanceStack.stack)
                            discAmount--
                            break
                        }

                        size--
                    }
                }
            }
        }

        if(VERSION >= "1.19" && discFragmentMap.containsKey(key)) {
            if(inventory.any { it != null && it.type.isRecordFragment }) {
                inventory.storageContents = inventory.storageContents.map { itemstack ->
                    if(itemstack != null && itemstack.type.isRecordFragment) {
                        ItemStack(Material.AIR)
                    } else {
                        itemstack
                    }
                }.toTypedArray()
            }

            val fragmentMaxAmount = if(CONFIG.FRAGMENT_LIMIT.containsKey(key)) CONFIG.FRAGMENT_LIMIT[key]!!
            else if(CONFIG.FRAGMENT_LIMIT.containsKey("chests/*")) CONFIG.FRAGMENT_LIMIT["chests/*"]!!
            else 3

            var fragmentAmount = Random.nextInt(0, fragmentMaxAmount + 1)

            discFragmentMap[key]?.apply{ shuffle() }?.forEach { chanceStack ->
                if (Random.nextInt(0, 1001) < chanceStack.chance) {
                    val number = if(fragmentAmount > 3) Random.nextInt(0, 4)
                    else Random.nextInt(0, fragmentAmount + 1)

                    var size = inventory.size

                    while (size > 0 && fragmentAmount > 0) {
                        val slot = Random.nextInt(0, size)
                        val item = inventory.getItem(slot)

                        if (item == null || item.type == Material.AIR) {
                            inventory.setItem(slot, chanceStack.stack.apply {
                                amount = number
                            })

                            fragmentAmount -= number
                            break
                        }
                        size--
                    }
                }
            }
        }
    }

    /**
     * The function is called when a player interacts with a chest. It checks if the chest is a loot chest and if it is,
     * it generates items in the chest.
     *
     * @param e The `e` parameter is an object of type `PlayerInteractEvent`. It represents the event that is being
     * called when a player interacts with a block.
     * @return Nothing is being returned in this code snippet. It is a void function.
     */
    @EventHandler(ignoreCancelled = true)
    fun onChestOpen(e : PlayerInteractEvent) {
        if(e.action != Action.RIGHT_CLICK_BLOCK || e.clickedBlock?.type != Material.CHEST) return

        val chest = e.clickedBlock!!.state as Chest
        val key = chest.lootTable?.key?.key ?: return

        generateItems(chest.blockInventory, key)
    }

    /**
     * The function is called when a player breaks a chest. It checks if the chest is a loot chest and if it is, it
     * generates items in the chest.
     *
     * @param e The `e` parameter is an object of type `BlockBreakEvent`. It represents the event that is being called
     * when a player breaks a block.
     * @return Nothing is being returned in this code snippet. It is a void function.
     */
    @EventHandler(ignoreCancelled = true)
    fun onChestBreak(e : BlockBreakEvent) {
        if(e.block.type != Material.CHEST) return

        val chest = e.block.state as Chest
        val key = chest.lootTable?.key?.key ?: return

        generateItems(chest.inventory, key)
    }

    /**
     * The function is called when a player interacts with a minecart with chest. It checks if the minecart is a loot minecart and if it is, it
     * generates items in the inside.
     *
     * @param e The `e` parameter is an object of type `PlayerInteractEntityEvent`. It represents the event that is being
     * called when a player interacts with an entity.
     */
    @EventHandler(ignoreCancelled = true)
    fun onMinecartChestOpen(e : PlayerInteractEntityEvent) {
        if(e.rightClicked !is StorageMinecart) return

        val minecart = e.rightClicked as StorageMinecart
        val key = minecart.lootTable?.key?.key ?: return

        generateItems(minecart.inventory, key)
    }

    /**
     * The function is called when a player breaks a minecart with chest. It checks if the minecart is a loot minecart and if it is, it
     * generates items in the inside.
     *
     * @param e The `e` parameter is an object of type `VehicleDestroyEvent`. It represents the event that is being called
     * when a player breaks a vehicle.
     */
    @EventHandler(ignoreCancelled = true)
    fun onMinecartChestBreak(e : VehicleDestroyEvent) {
        if(e.vehicle.type != EntityType.MINECART_CHEST) return

        val minecart = e.vehicle as StorageMinecart
        val key = minecart.lootTable?.key?.key ?: return

        generateItems(minecart.inventory, key)
    }
}