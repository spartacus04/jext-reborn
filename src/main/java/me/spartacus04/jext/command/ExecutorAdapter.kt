package me.spartacus04.jext.command

import me.spartacus04.jext.Log
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

internal open class ExecutorAdapter(commandString: String) : CommandExecutor, TabCompleter {
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

        val usageSMS = Log("Usage").info().t("/").a(commandString)

        for (parameter in parameters) {
            usageSMS.rst(" ").rst(parameter.toString())
        }
        command.usage = usageSMS.text()
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
            PERMISSION_MESSAGE.send(sender)
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
        DEFAULT_MESSAGE.send(sender, "console")
        return true
    }

    open fun executeCommand(sender: CommandSender, args: Array<String>): Boolean {
        DEFAULT_MESSAGE.send(sender, "players")
        return true
    }

    companion object {
        private val DEFAULT_MESSAGE = Log().info().t("This command is only for ").t().t(".")
        private val PERMISSION_MESSAGE = Log().eror().t("You do not have permission to use this command.")
    }
}