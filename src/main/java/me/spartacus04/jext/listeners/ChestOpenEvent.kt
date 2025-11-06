package me.spartacus04.jext.listeners

import me.spartacus04.colosseum.listeners.ColosseumListener
import me.spartacus04.jext.Jext
import me.spartacus04.jext.utils.Constants.ChanceStack
import me.spartacus04.jext.utils.Constants.DEFAULT_DISCS_LOOT_TABLE
import me.spartacus04.jext.utils.Constants.DEFAULT_FRAGMENTS_LOOT_TABLE
import me.spartacus04.jext.utils.isRecordFragment
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.Container
import org.bukkit.entity.minecart.StorageMinecart
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.vehicle.VehicleDestroyEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.Lootable
import kotlin.random.Random

internal class ChestOpenEvent(val plugin: Jext) : ColosseumListener(plugin) {

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

        plugin.discs.forEach {
            it.lootTables.forEach { lootTable ->
                if (discsMap.containsKey(lootTable.key)) {
                    discsMap[lootTable.key]!!.add(ChanceStack(lootTable.value, it.discItemStack))
                } else {
                    discsMap[lootTable.key] = arrayListOf(ChanceStack(lootTable.value, it.discItemStack))
                }
            }

            if(plugin.serverVersion < "1.19") return@forEach

            it.fragmentLootTables.forEach { lootTable ->
                if (discFragmentMap.containsKey(lootTable.key)) {
                    discFragmentMap[lootTable.key]!!.add(ChanceStack(lootTable.value, it.fragmentItemStack!!))
                } else {
                    discFragmentMap[lootTable.key] = arrayListOf(ChanceStack(lootTable.value, it.fragmentItemStack!!))
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

            val discMaxAmount = if(plugin.config.DISC_LIMIT.containsKey(key)) plugin.config.DISC_LIMIT[key]!!
            else if(plugin.config.DISC_LIMIT.containsKey("chests/*")) plugin.config.DISC_LIMIT["chests/*"]!!
            else 2

            var discAmount = Random.nextInt(0, discMaxAmount + 1)

            discsMap[key]?.apply{ shuffle(); sortTop() }?.forEach { chanceStack ->
                if (Random.nextInt(0, 1001) < chanceStack.chance) {
                    var size = inventory.size
                    var discSet = false

                    while (size > 0 && discAmount > 0) {

                        val slot = Random.nextInt(0, size)
                        val item = inventory.getItem(slot)

                        if (item == null || item.type == Material.AIR) {
                            inventory.setItem(slot, chanceStack.stack)
                            discSet = true
                            break
                        }

                        size--
                    }

                    if(!discSet) inventory.setItem(Random.nextInt(0, inventory.size), chanceStack.stack)
                }
                discAmount--
            }
        }

        if(plugin.serverVersion >= "1.19" && discFragmentMap.containsKey(key)) {
            if(inventory.any { it != null && it.type.isRecordFragment }) {
                inventory.storageContents = inventory.storageContents.map { itemstack ->
                    if(itemstack != null && itemstack.type.isRecordFragment) {
                        ItemStack(Material.AIR)
                    } else {
                        itemstack
                    }
                }.toTypedArray()
            }

            val fragmentMaxAmount = if(plugin.config.FRAGMENT_LIMIT.containsKey(key)) plugin.config.FRAGMENT_LIMIT[key]!!
            else if(plugin.config.FRAGMENT_LIMIT.containsKey("chests/*")) plugin.config.FRAGMENT_LIMIT["chests/*"]!!
            else 3

            var fragmentAmount = Random.nextInt(0, fragmentMaxAmount + 1)

            discFragmentMap[key]?.apply{ shuffle(); sortTop() }?.forEach { chanceStack ->
                val number = if(fragmentAmount > 3) Random.nextInt(0, 4)
                else Random.nextInt(0, fragmentAmount + 1)

                if (Random.nextInt(0, 1001) < chanceStack.chance) {
                    var size = inventory.size
                    var fragmentSet = false


                    while (size > 0 && fragmentAmount > 0) {
                        val slot = Random.nextInt(0, size)
                        val item = inventory.getItem(slot)

                        if (item == null || item.type == Material.AIR) {
                            inventory.setItem(slot, chanceStack.stack.apply {
                                amount = number
                            })

                            fragmentSet = true

                            break
                        }
                        size--
                    }

                    if(!fragmentSet) inventory.setItem(Random.nextInt(0, inventory.size), chanceStack.stack.apply {
                        amount = number
                    })
                }
                fragmentAmount -= number
            }
        }
    }

    private fun ArrayList<ChanceStack>.sortTop() {
        val top = this.filter { it.chance >= 1000 }
        val bottom = this.filter { it.chance < 1000 }

        this.clear()
        this.addAll(top)
        this.addAll(bottom)
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
        if(e.action != Action.RIGHT_CLICK_BLOCK || e.clickedBlock?.let { isLootable(it) } == false) return

        val inventory = (e.clickedBlock!!.state as Container).inventory
        val key = (e.clickedBlock!!.state as Lootable).lootTable?.key?.key ?: return

        generateItems(inventory, key)
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
        if(!isLootable(e.block)) return

        val inventory = (e.block.state as Container).inventory
        val key = (e.block.state as Lootable).lootTable?.key?.key ?: return

        generateItems(inventory, key)
    }

    @EventHandler(ignoreCancelled = true)
    fun onChestExplore(e : BlockExplodeEvent) {
        if(!isLootable(e.block)) return

        val inventory = (e.block.state as Container).inventory
        val key = (e.block.state as Lootable).lootTable?.key?.key ?: return

        generateItems(inventory, key)
    }

    private fun isLootable(block: Block) = block.type == Material.CHEST || block.type == Material.BARREL

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
        if(e.vehicle !is StorageMinecart) return

        val minecart = e.vehicle as StorageMinecart
        val key = minecart.lootTable?.key?.key ?: return

        generateItems(minecart.inventory, key)
    }
}