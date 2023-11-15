package me.spartacus04.jext.discs

import kotlinx.coroutines.runBlocking
import me.spartacus04.jext.discs.sources.DiscSource
import org.bukkit.inventory.meta.ItemMeta

class DiscManager : Iterable<Disc> {
    private val discSources = arrayListOf<DiscSource>()
    private var discs: ArrayList<Disc> = arrayListOf()

    fun reloadDiscs() {
        discs.clear()

        discSources.forEach {
            runBlocking {
                discs.addAll(it.getDiscs())
            }
        }

        discs.forEach { println(it.namespace) }
    }

    fun registerDiscSource(vararg discSources: DiscSource) {
        this.discSources.addAll(discSources)

        reloadDiscs()
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
}