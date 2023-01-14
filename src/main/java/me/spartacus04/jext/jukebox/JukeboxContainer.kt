package me.spartacus04.jext.jukebox

import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.config.ConfigData.Companion.LANG
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.jukebox.JukeboxPersistentDataContainerManager.Companion.jukeboxContainers
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.lang.Exception
import java.lang.IllegalStateException
import java.util.*

class JukeboxContainer : Listener {
    private val id: String
    private lateinit var inventory : Inventory
    private val uuid = UUID.randomUUID()
    private val location: Location
    private lateinit var plugin: JavaPlugin

    constructor(plugin: JavaPlugin, loc: Location, player: HumanEntity) {
        id = loc.world!!.name + loc.blockX + loc.blockY + loc.blockZ
        location = loc
        mergedConstructor(player, plugin)
    }

    constructor(plugin: JavaPlugin, player: HumanEntity) {
        id = player.uniqueId.toString()
        location = player.location
        mergedConstructor(player, plugin)
    }

    private fun mergedConstructor(player: HumanEntity, plugin: JavaPlugin) {
        if(jukeboxContainers[id] == null) {
            jukeboxContainers[id] = JukeboxPersistentDataContainer()
        }

        inventory = Bukkit.createInventory(player, 54, LANG.format(player, "jukebox", true))
        inventory.contents = generateContents()
        jukeboxContainers[id]!!.subscribe(uuid) { inventory.contents = generateContents() }
        jukeboxContainers[id]!!.unsubscribedFuncs[uuid] = {
            try {
                HandlerList.unregisterAll(this)
                inventory.viewers.forEach {
                    it.closeInventory()
                }
            } catch (_: Exception) { }
        }

        Bukkit.getPluginManager().registerEvents(this, plugin)
        this.plugin = plugin

        player.openInventory(inventory)
    }

    private fun generateContents(): Array<ItemStack?> {
        val contents = arrayOfNulls<ItemStack>(54)

        jukeboxContainers[id]!!.discs.forEach { (index, disc) ->
            if(JukeboxPersistentDataContainerManager.isDiscContainer(disc)) {
                contents[index] = DiscContainer(DISCS.first { it.DISC_NAMESPACE == disc }).discItem
            }
            else {
                contents[index] = ItemStack(Material.matchMaterial(disc)!!)
            }
        }

        if(jukeboxContainers[id]!!.playingDisc != -1) {
            val item = contents[jukeboxContainers[id]!!.playingDisc] ?: return contents
            item.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.MENDING, 1)
            contents[jukeboxContainers[id]!!.playingDisc] = item
        }

        return contents
    }

    @EventHandler
    fun onInventoryClose(e: InventoryCloseEvent) {
        if(e.inventory == inventory) {
            jukeboxContainers[id]!!.unsubscribe(uuid)
        }
    }

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        if(e.inventory != inventory) return

        if(e.isRightClick) {
            e.isCancelled = true

            return if(e.slot == jukeboxContainers[id]!!.playingDisc) {
                jukeboxContainers[id]!!.stopPlaying()
            } else {
                // check if clicked inventory is not player inventory
                if(e.clickedInventory == e.whoClicked.inventory || !e.clickedInventory!!.contents[e.slot].type.isRecord) return
                else jukeboxContainers[id]!!.playDisc(e.slot, location)
            }
        }

        if(e.slot == jukeboxContainers[id]!!.playingDisc) {
            jukeboxContainers[id]!!.stopPlaying()
            e.currentItem?.removeEnchantment(org.bukkit.enchantments.Enchantment.MENDING)
        }

        // get inventory contents
        val oldContents = generateContents()

        Bukkit.getScheduler().runTaskLater(plugin, object : Runnable {
            override fun run() {
                val newContents = e.view.topInventory.contents

                if(newContents.any { it != null && !it.type.isRecord }) {
                    e.isCancelled = true
                    return
                }

                oldContents.forEachIndexed { i, itemStack ->
                    if(itemStack != newContents[i]) {
                        if(itemStack == null) {
                            try {
                                jukeboxContainers[id]!!.addDisc(DiscContainer(newContents[i]!!), i)
                            } catch (e: IllegalStateException) {
                                jukeboxContainers[id]!!.addDisc(newContents[i], i)
                            }
                        }
                        else {
                            jukeboxContainers[id]!!.removeDisc(i)
                        }
                    }
                }
            }
        }, 1)
    }
}
