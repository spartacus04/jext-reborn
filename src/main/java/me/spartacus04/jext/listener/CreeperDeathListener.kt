package me.spartacus04.jext.listener

import me.spartacus04.jext.JextNamespace
import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Creeper
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Skeleton
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootContext
import org.bukkit.loot.LootTable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.asKotlinRandom

class CreeperDeathListener : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onCreeperDeath(event: EntityDeathEvent) {
        val killer = event.entity.lastDamageCause!!.entity
        if (event.entity !is Creeper || killer !is Skeleton) return

        val disc = event.drops.find {
            it.type.isRecord
        }

        if (disc != null) {
            val lootContext = LootContext.Builder(event.entity.location)
                .killer(killer as HumanEntity)
                .lootedEntity(event.entity)
                .build()

            event.drops.remove(disc)
            event.drops.addAll(DiscsLootTable().populateLoot(null, lootContext))
        }
    }
}

class DiscsLootTable : LootTable {
    private val droppableDiscs: MutableList<DiscContainer> = ArrayList()
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

    init {
        for (DISC in DISCS) {
            if (DISC.CREEPER_DROP) {
                droppableDiscs.add(DiscContainer(DISC))
            }
        }
    }
    override fun getKey(): NamespacedKey {
        return JextNamespace.CREEPERLT.get()!!
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

    override fun fillInventory(inventory: Inventory, random: Random?, context: LootContext) {
    }
}