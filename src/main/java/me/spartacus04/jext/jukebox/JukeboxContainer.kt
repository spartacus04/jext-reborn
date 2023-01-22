package me.spartacus04.jext.jukebox

import me.spartacus04.jext.config.ConfigData.Companion.LANG
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.HumanEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.lang.IllegalStateException

class JukeboxContainer : Listener {
    val id: String
    private lateinit var inventory : Inventory
    val location: Location
    val plugin: JavaPlugin
    val player: HumanEntity
    private lateinit var dataContainer: JukeboxPersistentDataContainer

    constructor(plugin: JavaPlugin, loc: Location, player: HumanEntity) {
        id = "${loc.world!!.name}:${loc.blockX}:${loc.blockY}:${loc.blockZ}"
        location = loc
        this.plugin = plugin
        this.player = player

        mergedConstructor(player, plugin)
    }

    constructor(plugin: JavaPlugin, player: HumanEntity) {
        id = player.uniqueId.toString()
        location = player.location
        this.plugin = plugin
        this.player = player

        mergedConstructor(player, plugin)
    }

    private fun mergedConstructor(player: HumanEntity, plugin: JavaPlugin) {
        inventory = Bukkit.createInventory(player, 54, LANG.format(player, "jukebox", true))
        dataContainer = JukeboxPersistentDataContainer.get(this)

        refresh()

        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    fun open(player: HumanEntity) {
        player.openInventory(inventory)
    }

    private fun createContents() : Array<ItemStack?> {
        val contents = arrayOfNulls<ItemStack>(54)

        dataContainer.getDiscs().forEach {
            contents[it.key] = it.value.getItemstack()
        }

        if(dataContainer.slot != -1) {
            val item = contents[dataContainer.slot]

            if(item != null) {
                item.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.MENDING, 1)

                item.itemMeta?.run {
                    addItemFlags(ItemFlag.HIDE_ENCHANTS)

                    lore = (lore ?: ArrayList()).apply {
                        add(LANG.format(player, "playing", true))
                    }
                }

                contents[dataContainer.slot] = item
            }
        }

        return contents
    }

    fun refresh() {
        inventory.contents = createContents()
    }

    fun close() {
        HandlerList.unregisterAll(this)
        inventory.viewers.forEach {
            it.closeInventory()
        }
    }

    fun breakJukebox() {
        val contents = createContents().filterNotNull()

        contents.forEach {
            location.world!!.dropItemNaturally(location, it)
        }

        JukeboxPersistentDataContainer.breakJukebox(id)
    }

    @EventHandler
    fun onInventoryClose(e: InventoryCloseEvent) {
        if(e.inventory != inventory) return

        // save jukebox contents
        val contents = e.inventory.contents

        contents.forEachIndexed { index, itemStack ->
            if(itemStack != null) {
                dataContainer.addDisc(DiscContainer(itemStack), index)
            } else {
                dataContainer.removeDisc(index)
            }
        }

        JukeboxPersistentDataContainer.remove(this)
    }

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        if(e.inventory != inventory) return

        if(e.isRightClick) {
            e.isCancelled = true

            return if(e.slot == dataContainer.slot) {
                dataContainer.stopPlaying()
            } else {
                // check if clicked inventory is not player inventory
                if(e.clickedInventory == e.whoClicked.inventory || e.clickedInventory!!.contents[e.slot]?.type?.isRecord != true) return

                dataContainer.playDisc(e.slot, location)
            }
        }

        if(e.slot == dataContainer.slot) {
            dataContainer.stopPlaying()
            e.currentItem?.removeEnchantment(org.bukkit.enchantments.Enchantment.MENDING)
        }

        val oldContents = createContents()

        if(e.currentItem != null && !e.currentItem!!.type.isRecord) {
            e.isCancelled = true
            return
        }

        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            val newContents = e.view.topInventory.contents

            if(e.currentItem != null && !e.currentItem!!.type.isRecord) {
                e.isCancelled = true
                return@Runnable
            }

            oldContents.forEachIndexed { i, itemStack ->
                if(itemStack != newContents[i]) {
                    if(itemStack == null) {
                        try {
                            dataContainer.addDisc(DiscContainer(newContents[i]!!), i)
                        } catch (e: IllegalStateException) {
                            dataContainer.addDisc(newContents[i]!!, i)
                        }
                    } else {
                        dataContainer.removeDisc(i)
                    }
                }
            }
        }, 1)
    }
}