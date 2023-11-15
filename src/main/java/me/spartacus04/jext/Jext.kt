package me.spartacus04.jext

import me.spartacus04.jext.State.CONFIG
import me.spartacus04.jext.State.DISCS
import me.spartacus04.jext.State.INTEGRATIONS
import me.spartacus04.jext.State.LANG
import me.spartacus04.jext.commands.CommandRegistrant
import me.spartacus04.jext.discs.sources.file.FileSource
import me.spartacus04.jext.gui.JukeboxGuiContainer
import me.spartacus04.jext.language.LanguageManager.Companion.DISABLED_MESSAGE
import me.spartacus04.jext.language.LanguageManager.Companion.ENABLED_MESSAGE
import me.spartacus04.jext.language.LanguageManager.Companion.UPDATE_LINK
import me.spartacus04.jext.listeners.ListenerRegistrant
import me.spartacus04.jext.utils.Updater
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

/**
 * The class `Jext` is the main class of the plugin. It extends the `JavaPlugin` class, which is the main class of all
 * Bukkit plugins.
 *
 * @constructor Creates a new Jext plugin.
 */
@Suppress("unused")
internal class Jext : JavaPlugin() {
    override fun onEnable() {
        try {
            load()
        } catch (e: Exception) {
            e.printStackTrace()
            server.pluginManager.disablePlugin(this)
        }
    }

    override fun onDisable() {
        Bukkit.getConsoleSender().sendMessage(DISABLED_MESSAGE)
    }

    private fun load() {
        DISCS.registerDiscSource(FileSource())
        ListenerRegistrant.registerListeners()
        INTEGRATIONS.reloadDefaultIntegrations()

        CommandRegistrant.registerCommands()

        Bukkit.getConsoleSender().sendMessage(ENABLED_MESSAGE)

        if(CONFIG.CHECK_FOR_UPDATES) {
            Updater().getVersion {
                if(it != description.version) {
                    Bukkit.getConsoleSender().sendMessage(LANG["en_us", "update-detected"])
                    Bukkit.getConsoleSender().sendMessage(UPDATE_LINK)
                }
            }
        }

        JukeboxGuiContainer.loadFromFile()
    }
}