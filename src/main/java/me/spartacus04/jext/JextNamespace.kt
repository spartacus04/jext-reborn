package me.spartacus04.jext

import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin

enum class JextNamespace(val key: String) {
    CREEPER_LOOTTABLE("jext.creeper_loottable"),
    AUTHOR("jext.author"),
    NAMESPACE_ID("jext.namespace_id"),
    IDENTIFIER("jext.identifier"),
    TITLE("jext.title");

    private lateinit var namespacedKey: NamespacedKey
    operator fun invoke(): NamespacedKey = namespacedKey

    companion object {
        fun registerNamespace(plugin: JavaPlugin) {
            for (namespace in values()) {
                namespace.namespacedKey = NamespacedKey(plugin, namespace.key)
            }
        }
    }
}