package me.spartacus04.jext.disc

import me.spartacus04.jext.JextNamespace
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

class DiscPersistentDataContainer(meta: ItemMeta?) {
    private val id = "JEXT"
    private val container: PersistentDataContainer

    init {
        container = meta!!.persistentDataContainer
    }

    var title: String?
        get() = container.get(
            JextNamespace.TITLE.get()!!,
            PersistentDataType.STRING
        )
        set(value) {
            container.set(
                JextNamespace.TITLE.get()!!,
                PersistentDataType.STRING,
                value!!
            )
        }

    var author: String?
        get() = container.get(
            JextNamespace.AUTHOR.get()!!,
            PersistentDataType.STRING
        )
        set(value) {
            container.set(
                JextNamespace.AUTHOR.get()!!,
                PersistentDataType.STRING,
                value!!
            )
        }

    var namespaceID: String?
        get() = container.get(
            JextNamespace.NAMESPACE_ID.get()!!,
            PersistentDataType.STRING
        )
        set(value) {
            container.set(
                JextNamespace.NAMESPACE_ID.get()!!,
                PersistentDataType.STRING,
                value!!
            )
        }

    fun setIdentifier() {
        container.set(
            JextNamespace.IDENTIFIER.get()!!,
            PersistentDataType.STRING,
            id
        )
    }

    fun checkIdentifier(): Boolean {
        val value = container.get(
            JextNamespace.IDENTIFIER.get()!!,
            PersistentDataType.STRING
        )
        return value == id
    }
}