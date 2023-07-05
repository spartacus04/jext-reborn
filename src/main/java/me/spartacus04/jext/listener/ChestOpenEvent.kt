package me.spartacus04.jext.listener

import me.spartacus04.jext.config.ConfigData.Companion.CONFIG
import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.config.ConfigData.Companion.VERSION
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.isRecordFragment
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
    data class ChanceStack(val chance: Int, val stack: ItemStack)

    private val discFragmentMap = HashMap<String, ArrayList<ChanceStack>>()

    private val discsMap = hashMapOf(
        LootTables.SIMPLE_DUNGEON.key.key to arrayListOf(
            ChanceStack(215, ItemStack(Material.MUSIC_DISC_13)),
            ChanceStack(215, ItemStack(Material.MUSIC_DISC_CAT)),
        ),
        LootTables.WOODLAND_MANSION.key.key to arrayListOf(
            ChanceStack(218, ItemStack(Material.MUSIC_DISC_13)),
            ChanceStack(218, ItemStack(Material.MUSIC_DISC_CAT)),
        ),
    )

    init {
        if(VERSION >= "1.16") {
            discsMap[LootTables.BASTION_TREASURE.key.key] = arrayListOf(
                ChanceStack(56, ItemStack(Material.MUSIC_DISC_PIGSTEP)),
            )
            discsMap[LootTables.BASTION_OTHER.key.key] = arrayListOf(
                ChanceStack(56, ItemStack(Material.MUSIC_DISC_PIGSTEP)),
            )
            discsMap[LootTables.BASTION_BRIDGE.key.key] = arrayListOf(
                ChanceStack(56, ItemStack(Material.MUSIC_DISC_PIGSTEP)),
            )
            discsMap[LootTables.BASTION_HOGLIN_STABLE.key.key] = arrayListOf(
                ChanceStack(56, ItemStack(Material.MUSIC_DISC_PIGSTEP)),
            )
        }

        if(VERSION >= "1.18") {
            discsMap[LootTables.SIMPLE_DUNGEON.key.key]!!.add(ChanceStack(31, ItemStack(Material.MUSIC_DISC_OTHERSIDE)))
        }

        if(VERSION >= "1.19") {
            discsMap[LootTables.ANCIENT_CITY.key.key] = arrayListOf(
                ChanceStack(161, ItemStack(Material.MUSIC_DISC_13)),
                ChanceStack(161, ItemStack(Material.MUSIC_DISC_CAT)),
                ChanceStack(81, ItemStack(Material.MUSIC_DISC_OTHERSIDE)),
            )

            discFragmentMap[LootTables.ANCIENT_CITY.key.key] = arrayListOf(
                ChanceStack(298, ItemStack(Material.DISC_FRAGMENT_5)),
            )
        }

        DISCS.forEach {
            it.LOOT_TABLES?.forEachIndexed { _, lootTable ->
                if (discsMap.containsKey(lootTable)) {
                    discsMap[lootTable]!!.add(ChanceStack(CONFIG.DISCS_RANDOM_CHANCE, DiscContainer(it).discItem))
                } else {
                    discsMap[lootTable] = arrayListOf(ChanceStack(CONFIG.DISCS_RANDOM_CHANCE, DiscContainer(it).discItem))
                }
            }


            if(VERSION < "1.19") return@forEach

            it.FRAGMENT_LOOT_TABLES?.forEach { lootTable ->
                if (discFragmentMap.containsKey(lootTable)) {
                    discFragmentMap[lootTable]!!.add(ChanceStack(CONFIG.FRAGMENTS_RANDOM_CHANCE, DiscContainer(it).fragmentItem))
                } else {
                    discFragmentMap[lootTable] = arrayListOf(ChanceStack(CONFIG.FRAGMENTS_RANDOM_CHANCE, DiscContainer(it).fragmentItem))
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

        val discMaxAmount = if(CONFIG.DISC_LIMIT.containsKey(key)) CONFIG.DISC_LIMIT[key]!!
        else if(CONFIG.DISC_LIMIT.containsKey("chests/*")) CONFIG.DISC_LIMIT["chests/*"]!!
        else 2

        var discAmount = Random.nextInt(0, discMaxAmount + 1)

        discsMap[key]?.forEach { chanceStack ->
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

        if(VERSION < "1.19") return

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

        discFragmentMap[key]?.forEach { chanceStack ->
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
