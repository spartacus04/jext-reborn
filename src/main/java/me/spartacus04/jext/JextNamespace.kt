package me.spartacus04.jext

import me.spartacus04.jext.Jext.Companion.INSTANCE
import org.bukkit.NamespacedKey

/**
 * The above class is an enum class that represents different namespaces and assigns a NamespacedKey to each namespace value.
 */
internal enum class JextNamespace(key: String) {
    NAMESPACE_ID("jext.namespace_id"),
    IDENTIFIER("jext.identifier");

    private val namespacedKey = NamespacedKey(INSTANCE, key)
    /**
     * The function returns a NamespacedKey.
     */
    operator fun invoke(): NamespacedKey = namespacedKey
}