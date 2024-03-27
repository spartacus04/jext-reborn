package me.spartacus04.jext.discs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.spartacus04.jext.JextState.SCHEDULER
import me.spartacus04.jext.discs.sources.DiscSource
import org.bukkit.inventory.meta.ItemMeta

class DiscManager : Iterable<Disc> {
    private val discSources = arrayListOf<DiscSource>()
    private var discs: ArrayList<Disc> = arrayListOf()

    fun reloadDiscs(onReload: (() -> Unit)? = null) {
        discs.clear()

        SCHEDULER.runTaskAsynchronously {
            val discs = CoroutineScope(Dispatchers.Default).launch {
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
}