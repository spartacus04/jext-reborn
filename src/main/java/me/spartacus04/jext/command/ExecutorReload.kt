package me.spartacus04.jext.command

import me.spartacus04.jext.command.adapter.ExecutorAdapter
import me.spartacus04.jext.config.ConfigManager
import me.spartacus04.jext.config.LanguageManager
import me.spartacus04.jext.config.sendJEXTMessage
import me.spartacus04.jext.integrations.IntegrationsRegistrant
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

internal class ExecutorReload(private val plugin: JavaPlugin) : ExecutorAdapter("jext") {
    /**
     * The function executes a command for a player in Kotlin.
     *
     * @param sender The "sender" parameter is of type "Player", which represents the player who executed the command.
     * @param args An array of strings representing the arguments passed to the command.
     * @return a boolean value of true.
     */
    override fun executePlayer(sender: Player, args: Array<String>): Boolean {
        mergedExecute(sender)
        return true
    }

    /**
     * The function executes a command and returns a boolean value indicating success.
     *
     * @param sender The `sender` parameter is of type `CommandSender`. It represents the entity that executed the command.
     * It could be a player, console, or any other entity that has the ability to execute commands.
     * @param args An array of strings representing the arguments passed to the command.
     * @return a boolean value of true.
     */
    override fun executeCommand(sender: CommandSender, args: Array<String>): Boolean {
        mergedExecute(sender)
        return true
    }

    /**
     * The function `mergedExecute` loads the plugin's configuration and language files, registers integrations, and sends
     * a message to the command sender.
     *
     * @param sender The "sender" parameter is of type CommandSender. It represents the entity that executed the command.
     * It could be a player, console, or any other entity that has the ability to execute commands.
     */
    private fun mergedExecute(sender: CommandSender) {
        ConfigManager.load(plugin)
        LanguageManager.load(plugin)
        IntegrationsRegistrant.registerIntegrations()

        sender.sendJEXTMessage("reloaded")
    }
}