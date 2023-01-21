package me.spartacus04.jext.listener

import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.disc.DiscContainer
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

internal class ChestOpenEvent : Listener {
    private val discsMap = hashMapOf(
        LootTables.SIMPLE_DUNGEON.key.key to arrayListOf(
            Material.MUSIC_DISC_13,
            Material.MUSIC_DISC_CAT,
            Material.MUSIC_DISC_OTHERSIDE
        ).map { ItemStack(it) }.toCollection(ArrayList()),
        LootTables.ANCIENT_CITY.key.key to arrayListOf(
            Material.MUSIC_DISC_13,
            Material.MUSIC_DISC_CAT,
            Material.MUSIC_DISC_OTHERSIDE
        ).map { ItemStack(it) }.toCollection(ArrayList()),
        LootTables.WOODLAND_MANSION.key.key to arrayListOf(
            Material.MUSIC_DISC_13,
            Material.MUSIC_DISC_CAT
        ).map { ItemStack(it) }.toCollection(ArrayList()),
        LootTables.STRONGHOLD_CORRIDOR.key.key to arrayListOf(
            Material.MUSIC_DISC_OTHERSIDE
        ).map { ItemStack(it) }.toCollection(ArrayList()),
        LootTables.BASTION_TREASURE.key.key to arrayListOf(
            Material.MUSIC_DISC_PIGSTEP
        ).map { ItemStack(it) }.toCollection(ArrayList()),
        LootTables.BASTION_OTHER.key.key to arrayListOf(
            Material.MUSIC_DISC_PIGSTEP
        ).map { ItemStack(it) }.toCollection(ArrayList()),
        LootTables.BASTION_BRIDGE.key.key to arrayListOf(
            Material.MUSIC_DISC_PIGSTEP
        ).map { ItemStack(it) }.toCollection(ArrayList()),
        LootTables.BASTION_HOGLIN_STABLE.key.key to arrayListOf(
            Material.MUSIC_DISC_PIGSTEP
        ).map { ItemStack(it) }.toCollection(ArrayList()),
    )

    init {
        DISCS.forEach {
            it.LOOT_TABLES?.forEach { lootTable ->
                if (discsMap.containsKey(lootTable)) {
                    discsMap[lootTable]!!.add(DiscContainer(it).discItem)
                } else {
                    discsMap[lootTable] = arrayListOf(DiscContainer(it).discItem)
                }
            }
        }
    }

    private fun generateItems(inventory: Inventory, key: String) {
        if(inventory.any { it.type.isRecord }) {
            inventory.storageContents = inventory.storageContents.map { itemstack ->
                return@map if(itemstack.type.isRecord) {
                    discsMap[key]?.random() ?: itemstack
                } else {
                    itemstack
                }
            }.toTypedArray()
        } else {
            var discstoadd = when(Random.nextInt(0, 101)) {
                in 0..4 -> 2
                in 5..20 -> 1
                else -> 0
            }

            var size = inventory.size

            while(discstoadd <= 0 && size > 0) {
                size--

                val randNum = Random.nextInt(inventory.size)
                val randItem = inventory.getItem(randNum)

                if(randItem == null) {
                    inventory.setItem(randNum, discsMap[key]?.random())
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
        if(!discsMap.containsKey(key)) return

        generateItems(chest.blockInventory, key)
    }

    @EventHandler(ignoreCancelled = true)
    fun onChestBreak(e : BlockBreakEvent) {
        if(e.block.type != Material.CHEST) return

        val chest = e.block.state as Chest
        val key = chest.lootTable?.key?.key ?: return
        if(!discsMap.containsKey(key)) return

        generateItems(chest.inventory, key)
    }

    @EventHandler(ignoreCancelled = true)
    fun onMinecartChestOpen(e : PlayerInteractEntityEvent) {
        if(e.rightClicked !is StorageMinecart) return

        val minecart = e.rightClicked as StorageMinecart
        val key = minecart.lootTable?.key?.key ?: return
        if(!discsMap.containsKey(key)) return

        generateItems(minecart.inventory, key)
    }

    @EventHandler(ignoreCancelled = true)
    fun onMinecartChestBreak(e : VehicleDestroyEvent) {
        if(e.vehicle.type != EntityType.MINECART_CHEST) return

        val minecart = e.vehicle as StorageMinecart
        val key = minecart.lootTable?.key?.key ?: return
        if(!discsMap.containsKey(key)) return

        generateItems(minecart.inventory, key)
    }
}
