package me.spartacus04.jext.disc

import me.spartacus04.jext.JextNamespace
import me.spartacus04.jext.config.ConfigData
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootContext
import org.bukkit.loot.LootTable
import java.util.*
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

    /**
     * The function returns the key for a specific loottable in the JextNamespace.
     */
    override fun getKey() = JextNamespace.CREEPER_LOOTTABLE()

    /**
     * The function populates a collection of ItemStacks with random loot items, including discs that can be dropped by
     * creepers.
     *
     * @param random The `random` parameter is an instance of the `Random` class, which is used to generate random numbers.
     * It is nullable, meaning it can be null.
     * @param context The `context` parameter is of type `LootContext` and is used to provide additional information about
     * the loot generation context. It may contain information such as the entity that dropped the loot, the location of
     * the loot generation, or any other relevant data.
     * @return The method is returning a mutable collection of ItemStacks.
     */
    override fun populateLoot(random: Random?, context: LootContext): MutableCollection<ItemStack> {
        val list = ArrayList<ItemStack>()

        list.addAll(ConfigData.DISCS.filter { it.CREEPER_DROP }.map {
            DiscContainer(it).discItem
        })

        list.addAll(creeperDroppableDiscs)

        return arrayListOf(list.random((random ?: Random()).asKotlinRandom()))
    }

    /**
     * The function "fillInventory" does nothing in this class.
     *
     * @param inventory The inventory parameter is an object that represents the inventory that needs to be filled. It
     * could be a player's inventory, a chest's inventory, or any other type of inventory.
     * @param rnd The `rnd` parameter is of type `Random?`, which means it is an optional parameter of type `Random`. The
     * `Random` class is used to generate random numbers and can be used to add randomness to the filling of the inventory.
     * @param context The `context` parameter is of type `LootContext` and represents the context in which the inventory is
     * being filled. It provides information about the current state of the game and can be used to determine what items
     * should be added to the inventory.
     */
    override fun fillInventory(inventory: Inventory, rnd: Random?, context: LootContext) { }
}