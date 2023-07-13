package me.spartacus04.jext.listener

import me.spartacus04.jext.config.ConfigData.Companion.LANG
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.jukebox.legacy.LegacyJukeboxContainer
import me.spartacus04.jext.jukebox.legacy.LegacyJukeboxEntry
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.plugin.java.JavaPlugin
import java.lang.IllegalStateException
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval

@Suppress("deprecation")
@ScheduledForRemoval(inVersion = "1.3")
@Deprecated("This is part of the legacy jukebox gui system. It's going to be removed in the next major update.")
internal class JukeboxGuiListener(private val plugin: JavaPlugin) : Listener {
    /**
     * The function `getJukeboxGui` returns the key of a LegacyJukeboxContainer if its inventory matches the given
     * inventory, otherwise it returns null.
     *
     * @param inv The parameter `inv` is of type `Inventory`.
     * @return The function `getJukeboxGui` returns a String value.
     */
    private fun getJukeboxGui(inv: Inventory): String? {
        return if(!LegacyJukeboxContainer.containers.any {
                it.value.inventory == inv
            }) null
        else {
            val key = LegacyJukeboxContainer.containers.filterValues { it.inventory == inv }.keys.first()
            key
        }
    }

    /**
     * The function `onInventoryClose` is called when a player closes an inventory. It checks if the inventory is a
     * jukebox inventory and if so, it saves the data to the config.
     */
    @EventHandler
    fun onInventoryClose(e: InventoryCloseEvent) {
        val jukeId = getJukeboxGui(e.inventory) ?: return

        val contents = e.inventory.contents

        contents.forEachIndexed { index, itemStack ->
            if(itemStack != null) {
                try {
                    val container = DiscContainer(itemStack)

                    LegacyJukeboxContainer.loadedData[jukeId]?.set(index,
                        LegacyJukeboxEntry("jext", container.namespace)
                    )
                } catch (e: IllegalStateException) {
                    LegacyJukeboxContainer.loadedData[jukeId]?.set(index,
                        LegacyJukeboxEntry("minecraft", itemStack.type.name)
                    )
                }
            } else {
                LegacyJukeboxContainer.loadedData[jukeId]?.remove(index)
            }
        }

        LegacyJukeboxContainer.save(plugin)
    }

    /**
     * The function `onInventoryClick` is called when a player clicks in an inventory. It checks if the inventory is a
     * jukebox inventory and if so, it handles the click.
     * If the player clicks on the currently playing disc, the disc will stop playing.
     * If the player clicks on a disc, the disc will start playing.
     * If the player clicks on a disc in his inventory, the disc will be added to the jukebox.
     * If the player clicks on a disc in the jukebox, the disc will be removed from the jukebox.
     */
    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        val jukeId = getJukeboxGui(e.inventory) ?: return

        val playingSlot = LegacyJukeboxContainer.containers[jukeId]?.playingSlot ?: return

        if(e.isRightClick) {
            e.isCancelled = true

            if(e.cursor == e.inventory.getItem(e.slot)) return

            return if(e.slot == playingSlot) {
                stopPlaying(jukeId)
            } else {
                // check if clicked inventory is not player inventory
                if(e.clickedInventory == e.whoClicked.inventory || e.clickedInventory!!.contents[e.slot]?.type?.isRecord != true) return

                if(!jukeId.contains(':')) {
                    LegacyJukeboxContainer.containers[jukeId]!!.location = e.whoClicked.location
                }

                playDisc(jukeId, e.slot, LegacyJukeboxContainer.containers[jukeId]!!.location)
            }
        }

        if(e.cursor?.type?.isRecord == true && e.currentItem != null) {
            e.isCancelled = true
            return
        }

        if(e.slot == playingSlot) {
            stopPlaying(jukeId)
            e.currentItem?.removeEnchantment(Enchantment.MENDING)
        }

        val oldContents = LegacyJukeboxContainer.containers[jukeId]?.createContents()

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

            oldContents?.forEachIndexed { i, itemStack ->
                if(itemStack != newContents[i]) {
                    if(itemStack == null) {
                        try {
                            val container = DiscContainer(newContents[i]!!)

                            LegacyJukeboxContainer.loadedData[jukeId]?.set(i,
                                LegacyJukeboxEntry("jext", container.namespace)
                            )
                        } catch (e: IllegalStateException) {
                            LegacyJukeboxContainer.loadedData[jukeId]?.set(i,
                                LegacyJukeboxEntry("minecraft", newContents[i]!!.type.name)
                            )
                        }
                    } else {
                        LegacyJukeboxContainer.loadedData[jukeId]?.remove(i)
                    }
                }
            }
        }, 1)
    }

    /**
     * The function `playDisc` plays a music disc in a jukebox container at a specified slot and location, and stops any
     * currently playing disc in the container.
     *
     * @param id The `id` parameter is a unique identifier for a LegacyJukeboxContainer. It is used to retrieve the
     * container and its data from the `LegacyJukeboxContainer` class.
     * @param slot The `slot` parameter represents the slot number in the LegacyJukeboxContainer's inventory where the disc
     * will be played.
     * @param location The `location` parameter is of type `Location` and represents the location where the disc is being
     * played. It is used to play the disc at the specified location.
     * @return In this code, the function `playDisc` returns nothing (`Unit` in Kotlin).
     */
    private fun playDisc(id : String, slot: Int, location: Location) {
        if(!LegacyJukeboxContainer.loadedData.containsKey(id)) return
        if(!LegacyJukeboxContainer.loadedData[id]!!.containsKey(slot)) return

        if (LegacyJukeboxContainer.containers[id]?.playingSlot != -1) {
            stopPlaying(id)
        }

        LegacyJukeboxContainer.containers[id]?.playingSlot = slot
        LegacyJukeboxContainer.containers[id]?.playingLocation = location

        val duration = LegacyJukeboxContainer.loadedData[id]!![slot]!!.play(location)
        LegacyJukeboxContainer.containers[id]?.playing = LegacyJukeboxContainer.loadedData[id]!![slot]!!

        LegacyJukeboxContainer.containers[id]?.inventory!!.setItem(
            slot,
            LegacyJukeboxContainer.containers[id]?.inventory!!.getItem(slot).apply {
                this ?: return

                addUnsafeEnchantment(Enchantment.MENDING, 1)

                itemMeta = itemMeta?.apply {
                    addItemFlags(ItemFlag.HIDE_ENCHANTS)

                    lore = (lore ?: ArrayList()).apply {
                        add(LANG["en_us", "playing"])
                    }
                }
            }
        )

        if(duration.toInt() == 0) return
        Bukkit.getScheduler().runTaskLater(plugin, Runnable { stopPlaying(id) }, duration * 20)
    }

    /**
     * The function `stopPlaying` stops the currently playing music in a LegacyJukeboxContainer and resets its state.
     *
     * @param id The parameter `id` is a String that represents the identifier of a LegacyJukeboxContainer.
     * @return In the given code, the function `stopPlaying` returns nothing (Unit).
     */
    private fun stopPlaying(id: String) {
        if(
            LegacyJukeboxContainer.containers[id]?.playing == null ||
            LegacyJukeboxContainer.containers[id]?.playingLocation == null
        ) return

        val playingSlot = LegacyJukeboxContainer.containers[id]!!.playingSlot

        if(playingSlot != -1) {
            LegacyJukeboxContainer.containers[id]?.inventory?.setItem(
                playingSlot,
                LegacyJukeboxContainer.containers[id]!!.playing!!.getItemstack()
            )
        }

        LegacyJukeboxContainer.containers[id]?.playing?.stop(LegacyJukeboxContainer.containers[id]?.location!!)

        LegacyJukeboxContainer.containers[id]?.playing = null
        LegacyJukeboxContainer.containers[id]?.playingSlot = -1
        LegacyJukeboxContainer.containers[id]?.playingLocation = null

        LegacyJukeboxContainer.containers[id]?.refresh()
    }
}