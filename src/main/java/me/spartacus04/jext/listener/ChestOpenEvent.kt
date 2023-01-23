package me.spartacus04.jext.listener

import me.spartacus04.jext.SpigotVersion.Companion.VERSION
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

private val Material.isRecordFragment: Boolean
    get() {
        return when (this) {
            Material.DISC_FRAGMENT_5 -> true
            else -> false
        }
    }

internal class ChestOpenEvent : Listener {
    data class ChanceStack(val chance: Int, val stack: ItemStack)

    private val discFragmentMap = hashMapOf(
        LootTables.ANCIENT_CITY.key.key to arrayListOf(
            ChanceStack(298, ItemStack(Material.DISC_FRAGMENT_5)),
        )
    )

    private val discsMap = hashMapOf(
        LootTables.SIMPLE_DUNGEON.key.key to arrayListOf(
            ChanceStack(215, ItemStack(Material.MUSIC_DISC_13)),
            ChanceStack(215, ItemStack(Material.MUSIC_DISC_CAT)),
            ChanceStack(31, ItemStack(Material.MUSIC_DISC_OTHERSIDE)),
        ),
        LootTables.ANCIENT_CITY.key.key to arrayListOf(
            ChanceStack(161, ItemStack(Material.MUSIC_DISC_13)),
            ChanceStack(161, ItemStack(Material.MUSIC_DISC_CAT)),
            ChanceStack(81, ItemStack(Material.MUSIC_DISC_OTHERSIDE)),
        ),
        LootTables.WOODLAND_MANSION.key.key to arrayListOf(
            ChanceStack(218, ItemStack(Material.MUSIC_DISC_13)),
            ChanceStack(218, ItemStack(Material.MUSIC_DISC_CAT)),
        ),
        LootTables.STRONGHOLD_CORRIDOR.key.key to arrayListOf(
            ChanceStack(25, ItemStack(Material.MUSIC_DISC_OTHERSIDE)),
        ),
        LootTables.BASTION_TREASURE.key.key to arrayListOf(
            ChanceStack(56, ItemStack(Material.MUSIC_DISC_PIGSTEP)),
        ),
        LootTables.BASTION_OTHER.key.key to arrayListOf(
            ChanceStack(56, ItemStack(Material.MUSIC_DISC_PIGSTEP)),
        ),
        LootTables.BASTION_BRIDGE.key.key to arrayListOf(
            ChanceStack(56, ItemStack(Material.MUSIC_DISC_PIGSTEP)),
        ),
        LootTables.BASTION_HOGLIN_STABLE.key.key to arrayListOf(
            ChanceStack(56, ItemStack(Material.MUSIC_DISC_PIGSTEP)),
        ),
    )

    init {
        DISCS.forEach {
            it.LOOT_TABLES?.forEachIndexed { _, lootTable ->
                val item = DiscContainer(it).discItem.type

                if(item == Material.MUSIC_DISC_PIGSTEP && VERSION < 16) return@forEach
                if(item == Material.MUSIC_DISC_OTHERSIDE && VERSION < 18) return@forEach
                if(item == Material.MUSIC_DISC_5 && VERSION < 19) return@forEach

                if (discsMap.containsKey(lootTable)) {
                    discsMap[lootTable]!!.add(ChanceStack(200, DiscContainer(it).discItem))
                } else {
                    discsMap[lootTable] = arrayListOf(ChanceStack(200, DiscContainer(it).discItem))
                }
            }


            if(VERSION < 19) return@forEach

            it.FRAGMENT_LOOT_TABLES?.forEach { lootTable ->
                if (discFragmentMap.containsKey(lootTable)) {
                    discFragmentMap[lootTable]!!.add(ChanceStack(200, DiscContainer(it).fragmentItem))
                } else {
                    discFragmentMap[lootTable] = arrayListOf(ChanceStack(200, DiscContainer(it).fragmentItem))
                }
            }
        }
    }

    private fun generateItems(inventory: Inventory, key: String) {
        if(inventory.any { it != null && it.type.isRecord }) {
            inventory.storageContents = inventory.storageContents.map { itemstack ->
                if(itemstack != null && itemstack.type.isRecord) {
                    ItemStack(Material.AIR)
                } else {
                    itemstack
                }
            }.toTypedArray()
        }

        if(inventory.any { it != null && it.type.isRecordFragment }) {
            inventory.storageContents = inventory.storageContents.map { itemstack ->
                if(itemstack != null && itemstack.type.isRecordFragment) {
                    ItemStack(Material.AIR)
                } else {
                    itemstack
                }
            }.toTypedArray()
        }

        discsMap[key]?.forEach { chanceStack ->
            if (Random.nextInt(0, 1001) < chanceStack.chance) {
                var size = inventory.size

                while (size > 0) {
                    val slot = Random.nextInt(0, size)
                    val item = inventory.getItem(slot)

                    if (item == null || item.type == Material.AIR) {
                        inventory.setItem(slot, chanceStack.stack)
                        break
                    }

                    size--
                }
            }
        }

        discFragmentMap[key]?.forEach { chanceStack ->
            if (Random.nextInt(0, 1001) < chanceStack.chance) {
                val number = Random.nextInt(0, 4)
                var size = inventory.size

                while (size > 0) {
                    val slot = Random.nextInt(0, size)
                    val item = inventory.getItem(slot)

                    if (item == null || item.type == Material.AIR) {
                        inventory.setItem(slot, chanceStack.stack.apply {
                            amount = number
                        })

                        break
                    }

                    size--
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
