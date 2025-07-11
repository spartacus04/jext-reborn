package me.spartacus04.jext

import com.github.retrooper.packetevents.PacketEvents
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder
import me.spartacus04.jext.JextState.CONFIG
import me.spartacus04.jext.JextState.DISCS
import me.spartacus04.jext.JextState.INTEGRATIONS
import me.spartacus04.jext.JextState.LANG
import me.spartacus04.jext.JextState.WEBSERVER
import me.spartacus04.jext.commands.CommandRegistrant
import me.spartacus04.jext.discs.sources.file.FileSource
import me.spartacus04.jext.discs.sources.nbs.NbsSource
import me.spartacus04.jext.gui.JukeboxGui
import me.spartacus04.jext.language.LanguageManager.Companion.DISABLED_MESSAGE
import me.spartacus04.jext.language.LanguageManager.Companion.DOCUMENTATION_LINK
import me.spartacus04.jext.language.LanguageManager.Companion.ENABLED_MESSAGE
import me.spartacus04.jext.language.LanguageManager.Companion.NO_DISCS_FOUND
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

    override fun onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load()
    }

    override fun onEnable() {
        try {
            load()
        } catch (e: Exception) {
            e.printStackTrace()
            server.pluginManager.disablePlugin(this)
        }
    }

    override fun onDisable() {
        WEBSERVER.stop()
        Bukkit.getConsoleSender().sendMessage(DISABLED_MESSAGE)
    }

    private fun load() {
        DISCS.registerDiscSource(FileSource(), NbsSource()) {
            INTEGRATIONS.reloadDefaultIntegrations()
            JukeboxGui.loadFromFile()

            if(DISCS.size() == 0) {
                Bukkit.getConsoleSender().sendMessage(NO_DISCS_FOUND)
                Bukkit.getConsoleSender().sendMessage(DOCUMENTATION_LINK)
            }
        }

        ListenerRegistrant.registerListeners()

        CommandRegistrant.registerCommands()

        Bukkit.getConsoleSender().sendMessage(ENABLED_MESSAGE)

        if(CONFIG.CHECK_FOR_UPDATES) {
            Updater().getVersion {
                if(description.version == "dev") {
                    Bukkit.getConsoleSender().sendMessage("Current upstream version is $it")
                } else if(it != description.version) {
                    Bukkit.getConsoleSender().sendMessage("[§aJEXT§f] ${LANG["en_us", "update-available"]}")
                    Bukkit.getConsoleSender().sendMessage(UPDATE_LINK)
                }
            }
        }
    }
}