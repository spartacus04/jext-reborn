package me.spartacus04.jext.discs

import io.github.bananapuncher714.nbteditor.NBTEditor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.spartacus04.jext.JextState.ASSETS_MANAGER
import me.spartacus04.jext.JextState.SCHEDULER
import me.spartacus04.jext.JextState.VERSION
import me.spartacus04.jext.discs.discstopping.DefaultDiscStoppingMethod
import me.spartacus04.jext.discs.discstopping.DiscStoppingMethod
import me.spartacus04.jext.discs.discstopping.NbsDiscStoppingMethod
import me.spartacus04.jext.discs.sources.DiscSource
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.ItemMeta

/**
 * The class `DiscManager` is a utility class that's used to manage and enumerate all the discs in the plugin.
 */
class DiscManager : Iterable<Disc> {
    private val discSources = arrayListOf<DiscSource>()
    private var discs: ArrayList<Disc> = arrayListOf()

    private val discStoppingMethods = arrayListOf(
        DefaultDiscStoppingMethod(),
        NbsDiscStoppingMethod()
    )

    @Suppress("unused")
    /**
     * Registers a disc stopping method to be used when stopping a disc.
     * 
     * @param discStoppingMethod The disc stopping method to register.
     */
    fun registerDiscStoppingMethod(discStoppingMethod: DiscStoppingMethod) {
        discStoppingMethods.add(discStoppingMethod)
    }

    /**
     * Reloads all the discs from the disc sources.
     * 
     * @param onReload The callback to run after the discs have been reloaded.
     */
    fun reloadDiscs(onReload: (() -> Unit)? = null) {
        discs.clear()

        SCHEDULER.runTaskAsynchronously {
            val discs = CoroutineScope(Dispatchers.Default).launch {
                ASSETS_MANAGER.clearCache()
                ASSETS_MANAGER.reloadAssets()

                discSources.forEach {
                    discs.addAll(it.getDiscs())
                }
            }

            discs.invokeOnCompletion { onReload?.invoke() }
        }
    }

    /**
     * Registers a disc source to be used when loading discs.
     * 
     * @param discSources The disc sources to register.
     * @param onReload The callback to run after the discs have been reloaded.
     */
    fun registerDiscSource(vararg discSources: DiscSource, onReload: (() -> Unit)? = null) {
        this.discSources.addAll(discSources)

        reloadDiscs(onReload)
    }

    override fun iterator(): Iterator<Disc> {
        return discs.iterator()
    }

    operator fun get(index: Int): Disc {
        return discs[index]
    }

    operator fun get(namespace: String): Disc? {
        return discs.find { it.namespace == namespace }
    }

    operator fun get(itemMeta: ItemMeta): Disc? {
        return discs.find { it.namespace == DiscPersistentDataContainer(itemMeta).namespaceID }
    }

    fun size() = discs.size

    /**
     * Checks if the disc stopping method can be stopped.
     * 
     * @param discStoppingMethod The disc stopping method to check.
     */
    private fun canStop(discStoppingMethod: DiscStoppingMethod) : Boolean {
        return discStoppingMethod.requires.all {
            Bukkit.getPluginManager().isPluginEnabled(it)
        }
    }

    /**
     * Stops all the discs that are currently playing for a player.
     * 
     * @param player The player to stop the discs for.
     */
    fun stop(player: Player) {
        discStoppingMethods.forEach {
            if(canStop(it)) {
                it.stop(player)
            }
        }
    }

    /**
     * Stops a specified disc that is currently playing for a player.
     * 
     * @param player The player to stop the disc for.
     * @param namespace The namespace of the disc to stop.
     */
    fun stop(player: Player, namespace: String) {
        discStoppingMethods.forEach {
            if(canStop(it)) {
                it.stop(player, namespace)
            }
        }
    }

    /**
     * Stops all the discs that are currently playing at a location.
     * 
     * @param location The location to stop the discs for.
     */
    fun stop(location: Location) {
        discStoppingMethods.forEach {
            if(canStop(it)) {
                it.stop(location)
            }
        }

        mergedStop(location)
    }

    /**
     * Stops a specified disc that is currently playing at a location.
     * 
     * @param location The location to stop the disc for.
     * @param namespace The namespace of the disc to stop.
     */
    fun stop(location: Location, namespace: String) {
        discStoppingMethods.forEach {
            if(canStop(it)) {
                it.stop(location, namespace)
            }
        }

        mergedStop(location)
    }

    private fun mergedStop(location: Location) {
        if(location.block.type != Material.JUKEBOX) return

        if(VERSION <= "1.21") {
            NBTEditor.set(location.block, NBTEditor.getLong(location.block, "RecordStartTick") + 72 * 20, "TickCount")
        } else {
            NBTEditor.set(location.block, (72 * 20).toLong(), "ticks_since_song_started")
        }
    }
}