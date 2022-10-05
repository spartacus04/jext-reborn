package me.spartacus04.jext

import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin

enum class JextNamespace(val key: String) {
    AUTHOR("jext.author"), NAMESPACE_ID("jext.namespace_id"), IDENTIFIER("jext.identifier"), TITLE("jext.title");

    private var namespacedKey: NamespacedKey? = null
    fun get(): NamespacedKey? {
        if (namespacedKey == null) throw NullPointerException("Namespace not registered.")
        return namespacedKey
    }

    companion object {
        fun registerNamespace(plugin: JavaPlugin) {
            for (namespace in values()) {
                namespace.namespacedKey = NamespacedKey(plugin, namespace.key)
            }
        }
    }
}