package me.spartacus04.jext.command

import me.spartacus04.jext.command.adapter.ExecutorAdapter
import me.spartacus04.jext.config.ConfigManager
import me.spartacus04.jext.config.LanguageManager
import me.spartacus04.jext.config.sendJEXTMessage
import me.spartacus04.jext.integrations.IntegrationsRegistrant
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

/**
 * ExecutorReload is a class used to register the "jext" command to the plugin.
 *
 * @property plugin The plugin instance used to reload the plugin.
 * @constructor plugin is used to reload the plugin.
 */
internal class ExecutorReload(private val plugin: JavaPlugin) : ExecutorAdapter("jext") {

    /**
     * The function `execute` reloads the plugin.
     *
     * @param sender The sender who executed the command.
     * @param args The arguments that were passed to the command.
     */
    override fun execute(sender: CommandSender, args: Array<String>) {
        ConfigManager.load(plugin)
        LanguageManager.load(plugin)
        IntegrationsRegistrant.registerIntegrations(plugin)

        sender.sendJEXTMessage("reloaded")
    }
}