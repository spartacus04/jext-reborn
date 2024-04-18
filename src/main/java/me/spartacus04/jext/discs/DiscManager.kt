package me.spartacus04.jext.discs

import io.github.bananapuncher714.nbteditor.NBTEditor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.spartacus04.jext.JextState.ASSETS_MANAGER
import me.spartacus04.jext.JextState.SCHEDULER
import me.spartacus04.jext.discs.discstopping.DefaultDiscStoppingMethod
import me.spartacus04.jext.discs.discstopping.DiscStoppingMethod
import me.spartacus04.jext.discs.sources.DiscSource
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.ItemMeta

class DiscManager : Iterable<Disc> {
    private val discSources = arrayListOf<DiscSource>()
    private var discs: ArrayList<Disc> = arrayListOf()

    private val discStoppingMethods = arrayListOf<DiscStoppingMethod>(
        DefaultDiscStoppingMethod()
    )

    fun registerDiscStoppingMethod(discStoppingMethod: DiscStoppingMethod) {
        discStoppingMethods.add(discStoppingMethod)
    }

    fun reloadDiscs(onReload: (() -> Unit)? = null) {
        discs.clear()

        SCHEDULER.runTaskAsynchronously {
            val discs = CoroutineScope(Dispatchers.Default).launch {
                ASSETS_MANAGER.reloadAssets()

                discSources.forEach {
                    discs.addAll(it.getDiscs())
                }
            }

            discs.invokeOnCompletion { onReload?.invoke() }
        }
    }

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

    private fun canStop(discStoppingMethod: DiscStoppingMethod) : Boolean {
        return discStoppingMethod.requires.all {
            Bukkit.getPluginManager().isPluginEnabled(it)
        }
    }

    fun stop(player: Player) {
        discStoppingMethods.forEach {
            if(canStop(it)) {
                it.stop(player)
            }
        }
    }

    fun stop(player: Player, namespace: String) {
        discStoppingMethods.forEach {
            if(canStop(it)) {
                it.stop(player, namespace)
            }
        }
    }

    fun stop(location: Location) {
        discStoppingMethods.forEach {
            if(canStop(it)) {
                it.stop(location)
            }
        }

        if(location.block.type != Material.JUKEBOX) return

        NBTEditor.set(location.block, NBTEditor.getLong(location.block, "RecordStartTick") + 72 * 20, "TickCount")
    }

    fun stop(location: Location, namespace: String) {
        discStoppingMethods.forEach {
            if(canStop(it)) {
                it.stop(location, namespace)
            }
        }

        if(location.block.type != Material.JUKEBOX) return

        NBTEditor.set(location.block, NBTEditor.getLong(location.block, "RecordStartTick") + 72 * 20, "TickCount")
    }
}