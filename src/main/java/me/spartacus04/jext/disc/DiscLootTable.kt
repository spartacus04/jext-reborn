package me.spartacus04.jext.disc

import me.spartacus04.jext.JextNamespace
import me.spartacus04.jext.config.ConfigData
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Creeper
import org.bukkit.entity.Skeleton
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootContext
import org.bukkit.loot.LootTable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.asKotlinRandom

class DiscLootTable : LootTable {
    private val droppableDiscs: MutableList<DiscContainer> = ArrayList()

    init {
        for (DISC in ConfigData.DISCS) {
            if (DISC.CREEPER_DROP) {
                droppableDiscs.add(DiscContainer(DISC))
            }
        }
    }

    override fun getKey(): NamespacedKey {
        return JextNamespace.CREEPERLT.get()!!
    }

    fun getRandomDisc(baseDiscs: List<Material>) : ItemStack {
        val list = ArrayList<ItemStack>()

        list.addAll(droppableDiscs.map {
            it.discItem
        })

        list.addAll(baseDiscs.map { ItemStack(it) })

        return list.random()
    }

    override fun populateLoot(random: Random?, context: LootContext): MutableCollection<ItemStack> {
        val list = ArrayList<ItemStack>()

        list.addAll(droppableDiscs.map {
            it.discItem
        })

        if(context.killer !is Skeleton || context.lootedEntity !is Creeper) return list

        list.addAll(creeperDroppableDiscs)

        return arrayListOf(list.random(random?.asKotlinRandom() ?: Random().asKotlinRandom()))
    }

    override fun fillInventory(inventory: Inventory, random: Random?, context: LootContext) { }

    companion object {
        val creeperDroppableDiscs: MutableList<ItemStack> = arrayListOf(
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
}