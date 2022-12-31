package me.spartacus04.jext.disc

import me.spartacus04.jext.config.ConfigData
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.collections.ArrayList

class DiscLootTable {
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

        fun getRandomDisc() : ItemStack {
            val list = ArrayList<ItemStack>()

            list.addAll(ConfigData.DISCS.filter { it.CREEPER_DROP }.map {
                DiscContainer(it).discItem
            })

            list.addAll(creeperDroppableDiscs)

            return list.random()
        }
    }
}