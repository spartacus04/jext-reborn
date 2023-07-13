package me.spartacus04.jext

import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin

/* The above class is an enum class in Kotlin that represents different namespaces and assigns a NamespacedKey to each
namespace value. */
enum class JextNamespace(val key: String) {
    CREEPER_LOOTTABLE("jext.creeper_loottable"),
    AUTHOR("jext.author"),
    NAMESPACE_ID("jext.namespace_id"),
    IDENTIFIER("jext.identifier"),
    TITLE("jext.title");

    private lateinit var namespacedKey: NamespacedKey
    /**
     * The function returns a NamespacedKey.
     */
    operator fun invoke(): NamespacedKey = namespacedKey

    companion object {
        /**
         * The function "registerNamespace" assigns a NamespacedKey to each namespace value in a given JavaPlugin.
         *
         * @param plugin The "plugin" parameter is an instance of the JavaPlugin class. It represents the plugin that is
         * registering the namespaces.
         */
        fun registerNamespace(plugin: JavaPlugin) {
            for (namespace in values()) {
                namespace.namespacedKey = NamespacedKey(plugin, namespace.key)
            }
        }
    }
}