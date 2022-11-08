package me.spartacus04.jext.listener

import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscLootTable
import org.bukkit.Material
import org.bukkit.block.Chest
import org.bukkit.entity.EntityType
import org.bukkit.entity.minecart.StorageMinecart
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.vehicle.VehicleDestroyEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootTables
import kotlin.random.Random

private fun Inventory.containsAny(itemStacks: MutableList<ItemStack>): Boolean {
    itemStacks.forEach {
        if(this.contains(it)) return true
    }

    return false
}

internal class ChestOpenEvent : Listener {
    private val commonDiscs = arrayListOf(Material.MUSIC_DISC_13, Material.MUSIC_DISC_CAT).map { ItemStack(it) }

    private fun generateItems(inventory: Inventory, dungeonDiscs: List<ItemStack>, key: String) {
        if(inventory.containsAny(DiscLootTable.creeperDroppableDiscs)) {
            inventory.storageContents = inventory.storageContents.map { itemstack ->
                if (itemstack == null) null

                else if (!commonDiscs.map { it.type }.contains(itemstack.type)) itemstack

                else {
                    val list = ArrayList<ItemStack>()

                    if(key == LootTables.SIMPLE_DUNGEON.key.key ||
                        key == LootTables.ANCIENT_CITY.key.key ||
                        key == LootTables.WOODLAND_MANSION.key.key)
                        list.addAll(commonDiscs)
                    list.addAll(dungeonDiscs)

                    list.random()
                }
            }.toTypedArray()
        }
        else {
            var discstoadd = when(Random.nextInt(0, 101)) {
                in 0..4 -> 2
                in 5..20 -> 1
                else -> 0
            }

            var size = inventory.size

            while(discstoadd != 0) {
                if(size <= 0) break
                size--

                val randNum = Random.nextInt(inventory.size)
                if(inventory.getItem(randNum) != null) {
                    inventory.setItem(randNum, dungeonDiscs.random())
                    discstoadd--
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onChestOpen(e : PlayerInteractEvent) {
        if(e.action != Action.RIGHT_CLICK_BLOCK || e.clickedBlock?.type != Material.CHEST) return

        val chest = e.clickedBlock!!.state as Chest
        val key = chest.lootTable?.key?.key ?: return

        val dungeonDiscs = DISCS.filter {
            if(it.LOOT_TABLES == null) false
            else it.LOOT_TABLES.contains(key)
        }.map { DiscContainer(it).discItem }.toCollection(ArrayList())

        generateItems(chest.inventory, dungeonDiscs, key)
    }

    @EventHandler(ignoreCancelled = true)
    fun onChestBreak(e : BlockBreakEvent) {
        if(e.block.type != Material.CHEST) return

        val chest = e.block.state as Chest
        val key = chest.lootTable?.key?.key ?: return

        val dungeonDiscs = DISCS.filter {
            if(it.LOOT_TABLES == null) false
            else it.LOOT_TABLES.contains(key)
        }.map { DiscContainer(it).discItem }.toCollection(ArrayList())

        generateItems(chest.inventory, dungeonDiscs, key)
    }

    @EventHandler(ignoreCancelled = true)
    fun onMinecartChestOpen(e : PlayerInteractEntityEvent) {
        if(e.rightClicked !is StorageMinecart) return

        val minecart = e.rightClicked as StorageMinecart
        val key = minecart.lootTable?.key?.key ?: return

        val dungeonDiscs = DISCS.filter {
            if(it.LOOT_TABLES == null) false
            else it.LOOT_TABLES.contains(key)
        }.map { DiscContainer(it).discItem }.toCollection(ArrayList())

        generateItems(minecart.inventory, dungeonDiscs, key)
    }

    @EventHandler(ignoreCancelled = true)
    fun onMinecartChestBreak(e : VehicleDestroyEvent) {
        if(e.vehicle.type != EntityType.MINECART_CHEST) return

        val minecart = e.vehicle as StorageMinecart
        val key = minecart.lootTable?.key?.key ?: return

        val dungeonDiscs = DISCS.filter {
            if(it.LOOT_TABLES == null) false
            else it.LOOT_TABLES.contains(key)
        }.map { DiscContainer(it).discItem }.toCollection(ArrayList())

        generateItems(minecart.inventory, dungeonDiscs, key)
    }

}
