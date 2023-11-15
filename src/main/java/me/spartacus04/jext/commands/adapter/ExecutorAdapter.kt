package me.spartacus04.jext.commands.adapter

import me.spartacus04.jext.State.LANG
import me.spartacus04.jext.State.PLUGIN
import me.spartacus04.jext.utils.sendJEXTMessage
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

/**
 * The ExecutorAdapter class is a utility class used to register, tab complete and execute commands and its parameters.
 *
 * @constructor The constructor requires a string representing the command name.
 *
 * @param commandString The `commandString` parameter is a string representing the command name.
 */
open class ExecutorAdapter(commandString: String) : CommandExecutor, TabCompleter {

    /**
     * The `commandString` property is a string representing the command name.
     */
    private val commandString: String

    /**
     * The `parameters` property is a mutable list of parameters.
     */
    private val parameters: MutableList<Parameter>

    init {
        parameters = ArrayList()
        this.commandString = commandString
    }

    /**
     * Adds a parameter to the command.
     *
     * @param parameter The `parameter` parameter is an instance of the `Parameter` class. It represents the parameter that you want to add to the command.
     */
    @Throws(IllegalArgumentException::class)
    fun addParameter(parameter: Parameter) {
        if (parameters.size > 0) {
            val lastParameter = parameters[parameters.size - 1]
            require(!(!lastParameter.isRequired && parameter.isRequired)) { "Required parameter cannot come after optional parameter!" }
        }
        parameters.add(parameter)
    }

    /**
     * register is a function that registers the command to the plugin.
     */
    fun register() {
        val command = PLUGIN.getCommand(commandString)!!

        command.setExecutor(this)
        command.tabCompleter = this

        var usageMessage = LANG.getKey(Bukkit.getConsoleSender(), "usage", hashMapOf(
            "command" to commandString
        ))

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
            sender.sendJEXTMessage("missing-permission")

            return true
        }

        if (args.size > parameters.size) return false
        if (args.size < parameters.size) {
            if (args.isEmpty() || parameters[args.size - 1].isRequired) {
                if (parameters[args.size].isRequired) return false
            }
        }

        if(execute(sender, args) == null) {
            if (sender is Player) executePlayer(sender, args) else executeConsole(sender, args)
        }

        return true
    }

    /**
     * This function is called when the command is executed by a player.
     *
     * @param sender The `sender` parameter is of type `Player`, which represents the player who executed the command.
     * @param args An array of strings representing the arguments passed to the command.
     */
    open fun executePlayer(sender: Player, args: Array<String>) {
        sender.sendJEXTMessage("invalid-runner", hashMapOf(
            "runner" to "console"
        ))
    }

    /**
     * This function is called when the command is executed by the console.
     *
     * @param sender The `sender` parameter is of type `CommandSender`, which represents the console who executed the command.
     * @param args An array of strings representing the arguments passed to the command.
     */
    open fun executeConsole(sender: CommandSender, args: Array<String>) {
        sender.sendJEXTMessage("invalid-runner", hashMapOf(
            "runner" to "players"
        ))
    }

    /**
     * This function is called when the command is executed.
     *
     * @param sender The `sender` parameter is of type `CommandSender`, which represents the console who executed the command.
     * @param args An array of strings representing the arguments passed to the command.
     */
    open fun execute(sender: CommandSender, args: Array<String>) : Unit? = null
}