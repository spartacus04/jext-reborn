package me.spartacus04.jext.disc

import me.spartacus04.jext.JextNamespace
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

/**
 * This class is used to store the disc data in the item meta.
 *
 * @constructor Creates a new DiscPersistentDataContainer object
 *
 * @param meta The item meta
 */
class DiscPersistentDataContainer(meta: ItemMeta?) {
    private val id = "JEXT"
    private val container: PersistentDataContainer

    init {
        container = meta!!.persistentDataContainer
    }

    /**
     * Sets and gets the disc title from the item meta.
     */
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

    /**
     * Sets and gets the disc author from the item meta.
     */
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

    /**
     * Sets and gets the disc namespace from the item meta.
     */
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
     * Sets a JEXT identifier in the item meta.
     */
    fun setIdentifier() {
        container.set(
            JextNamespace.IDENTIFIER(),
            PersistentDataType.STRING,
            id
        )
    }

    /**
     * Checks if the item meta contains a JEXT identifier.
     *
     * @return A boolean value that represents whether the item meta contains a JEXT identifier or not
     */
    fun checkIdentifier(): Boolean = container.get(
        JextNamespace.IDENTIFIER(),
        PersistentDataType.STRING
    ) == id
}