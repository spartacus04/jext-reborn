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
            JextNamespace.TITLE(),
            PersistentDataType.STRING
        )
        set(value) {
            container.set(
                JextNamespace.TITLE(),
                PersistentDataType.STRING,
                value!!
            )
        }

    var author: String?
        get() = container.get(
            JextNamespace.AUTHOR(),
            PersistentDataType.STRING
        )
        set(value) {
            container.set(
                JextNamespace.AUTHOR(),
                PersistentDataType.STRING,
                value!!
            )
        }

    var namespaceID: String?
        get() = container.get(
            JextNamespace.NAMESPACE_ID(),
            PersistentDataType.STRING
        )
        set(value) {
            container.set(
                JextNamespace.NAMESPACE_ID(),
                PersistentDataType.STRING,
                value!!
            )
        }

    /**
     * The function sets an identifier value in a container using a specific data type.
     */
    fun setIdentifier() {
        container.set(
            JextNamespace.IDENTIFIER(),
            PersistentDataType.STRING,
            id
        )
    }

    /**
     * The function checks if the identifier stored in the container matches the given id.
     */
    fun checkIdentifier(): Boolean = container.get(
        JextNamespace.IDENTIFIER(),
        PersistentDataType.STRING
    ) == id
}