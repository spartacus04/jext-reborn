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
    override fun executePlayer(sender: Player, args: Array<String>): Boolean {
        mergedExecute(sender)
        return true
    }

    override fun executeCommand(sender: CommandSender, args: Array<String>): Boolean {
        mergedExecute(sender)
        return true
    }

    private fun mergedExecute(sender: CommandSender) {
        ConfigManager.load(plugin)
        LanguageManager.load(plugin)
        IntegrationsRegistrant.registerIntegrations()

        sender.sendJEXTMessage("reloaded")
    }
}