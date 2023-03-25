package me.spartacus04.jext.command

import me.spartacus04.jext.config.ConfigData.Companion.LANG
import me.spartacus04.jext.config.send
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

open class ExecutorAdapter(commandString: String) : CommandExecutor, TabCompleter {
    private val commandString: String
    private val parameters: MutableList<Parameter>

    init {
        parameters = ArrayList()
        this.commandString = commandString
    }

    @Throws(IllegalArgumentException::class)
    fun addParameter(parameter: Parameter) {
        if (parameters.size > 0) {
            val lastParameter = parameters[parameters.size - 1]
            require(!(!lastParameter.isRequired && parameter.isRequired)) { "Required parameter cannot come after optional parameter!" }
        }
        parameters.add(parameter)
    }

    fun registerTo(plugin: JavaPlugin) {
        val command = plugin.getCommand(commandString)!!

        command.setExecutor(this)
        command.tabCompleter = this

        var usageMessage = "[Usage]:" + LANG.format(Bukkit.getConsoleSender(), "usage", true)
            .replace("%command%", commandString)

        for (parameter in parameters) {
            usageMessage += " $parameter"
        }

        command.usage = usageMessage
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String>? {
        return try {
            val index = args.size - 1
            val parameter = parameters[index]
            parameter.onComplete(args[index], sender)
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!command.testPermissionSilent(sender)) {
            LANG.format(sender, "missing_permission")
                .let { sender.send(it) }

            return true
        }

        if (args.size > parameters.size) return false
        if (args.size < parameters.size) {
            if (args.isEmpty() || parameters[args.size - 1].isRequired) {
                if (parameters[args.size].isRequired) return false
            }
        }

        return if (sender is Player) executePlayer(sender, args) else executeCommand(sender, args)
    }

    open fun executePlayer(sender: Player, args: Array<String>): Boolean {
        LANG.format(sender, "invalid-runner")
            .replace("%runner%", "console")
            .let { sender.send(it) }

        return true
    }

    open fun executeCommand(sender: CommandSender, args: Array<String>): Boolean {
        LANG.format(sender, "invalid-runner")
            .replace("%runner%", "players")
            .let { sender.send(it) }

        return true
    }
}