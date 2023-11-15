package me.spartacus04.jext

import me.spartacus04.jext.State.PLUGIN
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin

/**
 * The above class is an enum class that represents different namespaces and assigns a NamespacedKey to each namespace value.
 */
enum class JextNamespace(val key: String) {
    NAMESPACE_ID("jext.namespace_id"),
    IDENTIFIER("jext.identifier");

    private val namespacedKey = NamespacedKey(PLUGIN, key)
    /**
     * The function returns a NamespacedKey.
     */
    operator fun invoke(): NamespacedKey = namespacedKey
}