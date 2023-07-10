package me.spartacus04.jext.disc

import me.spartacus04.jext.JextNamespace
import me.spartacus04.jext.config.ConfigData
import me.spartacus04.jext.config.ConfigData.Companion.VERSION
import me.spartacus04.jext.listener.ChestOpenEvent
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootContext
import org.bukkit.loot.LootTable
import org.bukkit.loot.LootTables
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.asKotlinRandom

class DiscLootTable : LootTable {
    companion object {
        private val creeperDroppableDiscs: MutableList<ItemStack> = arrayListOf(
            ItemStack(Material.MUSIC_DISC_11),
            ItemStack(Material.MUSIC_DISC_13),
            ItemStack(Material.MUSIC_DISC_BLOCKS),
            ItemStack(Material.MUSIC_DISC_CAT),
            ItemStack(Material.MUSIC_DISC_CHIRP),
            ItemStack(Material.MUSIC_DISC_FAR),
            ItemStack(Material.MUSIC_DISC_MALL),
            ItemStack(Material.MUSIC_DISC_MELLOHI),
            ItemStack(Material.MUSIC_DISC_STAL),
            ItemStack(Material.MUSIC_DISC_STRAD),
            ItemStack(Material.MUSIC_DISC_WAIT),
            ItemStack(Material.MUSIC_DISC_WARD),
        )
    }

    override fun getKey() = JextNamespace.CREEPER_LOOTTABLE()

    override fun populateLoot(random: Random?, context: LootContext): MutableCollection<ItemStack> {
        val list = ArrayList<ItemStack>()

        list.addAll(ConfigData.DISCS.filter { it.CREEPER_DROP }.map {
            DiscContainer(it).discItem
        })

        list.addAll(creeperDroppableDiscs)

        return arrayListOf(list.random((random ?: Random()).asKotlinRandom()))
    }

    override fun fillInventory(inventory: Inventory, rnd: Random?, context: LootContext) { }
}