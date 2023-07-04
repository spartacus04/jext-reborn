package me.spartacus04.jext

import me.spartacus04.jext.command.CommandsRegistrant
import me.spartacus04.jext.config.ConfigData.Companion.CONFIG
import me.spartacus04.jext.config.ConfigData.Companion.LANG
import me.spartacus04.jext.config.ConfigData.Companion.PLUGIN
import me.spartacus04.jext.config.ConfigManager
import me.spartacus04.jext.config.LanguageManager
import me.spartacus04.jext.config.LanguageManager.Companion.DISABLED_MESSAGE
import me.spartacus04.jext.config.LanguageManager.Companion.DISCS_NOT_FOUND
import me.spartacus04.jext.config.LanguageManager.Companion.ENABLED_MESSAGE
import me.spartacus04.jext.config.LanguageManager.Companion.UPDATE_DETECTED
import me.spartacus04.jext.listener.ListenersRegistrant
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.FileNotFoundException

class Jext : JavaPlugin() {
    override fun onEnable() {
        try {
            load()
            Bukkit.getConsoleSender().sendMessage(ENABLED_MESSAGE)
        } catch (e: Exception) {
            e.printStackTrace()
            server.pluginManager.disablePlugin(this)
        }
    }

    override fun onDisable() {
        Bukkit.getConsoleSender().sendMessage(DISABLED_MESSAGE)
    }

    private fun load() {
        PLUGIN = this
        ServerVersion.load(this)
        JextNamespace.registerNamespace(this)
        ConfigManager.load(this)
        LanguageManager.load(this)
        CommandsRegistrant.registerCommands(this)
        ListenersRegistrant.registerListeners(this)

        Updater(this).getVersion {
            if(it != description.version) {
                Bukkit.getConsoleSender().sendMessage(UPDATE_DETECTED)
            }
        }

        // Start metrics if allowed
        if(!CONFIG.ALLOW_METRICS) return

        Metrics(this, 16571)
    }
}