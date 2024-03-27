package me.spartacus04.jext.commands.executors

import me.spartacus04.jext.State.LANG
import me.spartacus04.jext.State.PLUGIN
import me.spartacus04.jext.commands.CommandRegistrant.commandRegistry
import me.spartacus04.jext.commands.adapter.ExecutorAdapter
import me.spartacus04.jext.language.LanguageManager.Companion.JEXT_VERSION
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

internal class ExecutorMain : ExecutorAdapter("jext") {
    override fun execute(sender: CommandSender, args: Array<String>) {
        if(args.isEmpty()) {
            sender.sendMessage(LANG.replaceParameters(JEXT_VERSION, hashMapOf(
                "version" to PLUGIN.description.version
            )))

            return
        }

        val subArgs = try {
            args.copyOfRange(1, args.size)
        } catch (_ : Exception) {
            arrayOf()
        }

        val subcommand = commandRegistry.find { it.subCommandString == args[0] } ?: return

        Bukkit.dispatchCommand(sender, "${subcommand.commandString} ${subArgs.joinToString(separator = " ")}")
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String>? {
        if(args.size == 1) {
            return commandRegistry.map { it.subCommandString }
        }

        val commandExecutor = commandRegistry.find {
            it.subCommandString == args[0]
        } ?: return null

        val subArgs = args.copyOfRange(1, args.size)
        return commandExecutor.onTabComplete(sender, command, commandExecutor.commandString, subArgs)
    }
}