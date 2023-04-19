package me.spartacus04.jext.listener

import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.jukebox.JukeboxContainer
import me.spartacus04.jext.jukebox.JukeboxEntry
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.java.JavaPlugin
import java.lang.IllegalStateException

internal class JukeboxGuiListener(private val plugin: JavaPlugin) : Listener {
    private fun getJukeboxGui(inv: Inventory): String? {
        return if(!JukeboxContainer.containers.any {
                it.value.inventory == inv
            }) null
        else {
            val key = JukeboxContainer.containers.filterValues { it.inventory == inv }.keys.first()
            key
        }
    }

    @EventHandler
    fun onInventoryClose(e: InventoryCloseEvent) {
        val jukeId = getJukeboxGui(e.inventory) ?: return

        val contents = e.inventory.contents

        contents.forEachIndexed { index, itemStack ->
            if(itemStack != null) {
                try {
                    val container = DiscContainer(itemStack)

                    JukeboxContainer.loadedData[jukeId]?.set(index,
                        JukeboxEntry("jext", container.namespace)
                    )
                } catch (e: IllegalStateException) {
                    JukeboxContainer.loadedData[jukeId]?.set(index,
                        JukeboxEntry("minecraft", itemStack.type.name)
                    )
                }
            } else {
                JukeboxContainer.loadedData[jukeId]?.remove(index)
            }
        }

        JukeboxContainer.save(plugin)
    }

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        val jukeId = getJukeboxGui(e.inventory) ?: return

        val playingSlot = JukeboxContainer.containers[jukeId]?.playingSlot ?: return

        if(e.isRightClick) {
            e.isCancelled = true

            if(e.cursor == e.inventory.getItem(e.slot)) return

            return if(e.slot == playingSlot) {
                stopPlaying(jukeId)
            } else {
                // check if clicked inventory is not player inventory
                if(e.clickedInventory == e.whoClicked.inventory || e.clickedInventory!!.contents[e.slot]?.type?.isRecord != true) return

                if(!jukeId.contains(':')) {
                    JukeboxContainer.containers[jukeId]!!.location = e.whoClicked.location
                }

                playDisc(jukeId, e.slot, JukeboxContainer.containers[jukeId]!!.location)
            }
        }

        if(e.cursor?.type?.isRecord == true && e.currentItem != null) {
            e.isCancelled = true
            return
        }

        if(e.slot == playingSlot) {
            stopPlaying(jukeId)
            e.currentItem?.removeEnchantment(org.bukkit.enchantments.Enchantment.MENDING)
        }

        val oldContents = JukeboxContainer.containers[jukeId]?.createContents()

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

                            JukeboxContainer.loadedData[jukeId]?.set(i,
                                JukeboxEntry("jext", container.namespace)
                            )
                        } catch (e: IllegalStateException) {
                            JukeboxContainer.loadedData[jukeId]?.set(i,
                                JukeboxEntry("minecraft", newContents[i]!!.type.name)
                            )
                        }
                    } else {
                        JukeboxContainer.loadedData[jukeId]?.remove(i)
                    }
                }
            }
        }, 1)
    }

    private fun playDisc(id : String, slot: Int, location: Location) {
        if(!JukeboxContainer.loadedData.containsKey(id)) return
        if(!JukeboxContainer.loadedData[id]!!.containsKey(slot)) return

        if (JukeboxContainer.containers[id]?.playingSlot != -1) {
            stopPlaying(id)
        }

        JukeboxContainer.containers[id]?.playingSlot = slot
        JukeboxContainer.containers[id]?.playingLocation = location

        val duration = JukeboxContainer.loadedData[id]!![slot]!!.play(location)
        JukeboxContainer.containers[id]?.playing = JukeboxContainer.loadedData[id]!![slot]!!

        if(duration.toInt() == 0) return
        Bukkit.getScheduler().runTaskLater(plugin, Runnable { stopPlaying(id) }, duration * 20)
    }

    private fun stopPlaying(id: String) {
        if(
            JukeboxContainer.containers[id]?.playing == null ||
            JukeboxContainer.containers[id]?.playingLocation == null
        ) return

        JukeboxContainer.containers[id]?.playing?.stop(JukeboxContainer.containers[id]?.location!!)

        JukeboxContainer.containers[id]?.playing = null
        JukeboxContainer.containers[id]?.playingSlot = -1
        JukeboxContainer.containers[id]?.playingLocation = null

        JukeboxContainer.containers[id]?.refresh()
    }
}