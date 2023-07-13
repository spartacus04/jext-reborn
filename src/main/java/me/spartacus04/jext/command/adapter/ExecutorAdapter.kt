package me.spartacus04.jext.command.adapter

import me.spartacus04.jext.config.ConfigData.Companion.LANG
import me.spartacus04.jext.config.sendJEXTMessage
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

    /**
     * The function adds a parameter to a list, ensuring that required parameters are not placed after optional parameters.
     *
     * @param parameter The `parameter` is an object of type `Parameter`.
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
     * The function registers a command to a JavaPlugin in Kotlin, sets the executor and tab completer, and sets the usage
     * message.
     *
     * @param plugin The `plugin` parameter is an instance of the `JavaPlugin` class. It represents the plugin that you
     * want to register the command to.
     */
    fun registerTo(plugin: JavaPlugin) {
        val command = plugin.getCommand(commandString)!!

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

    /**
     * The `onTabComplete` function in Kotlin takes in command arguments and returns a list of possible completions for the
     * last argument.
     *
     * @param sender The `sender` parameter represents the entity that executed the command. It can be a player, console,
     * or any other entity that has the ability to execute commands.
     * @param command The `command` parameter is of type `Command` and represents the command that is being tab completed.
     * @param alias The `alias` parameter in the `onTabComplete` function is a string that represents the alias used to
     * execute the command. It is used to differentiate between different aliases that may be associated with the same
     * command.
     * @param args An array of strings representing the arguments passed to the command.
     * @return The method is returning a List of Strings.
     */
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

    /**
     * The function checks permissions, validates command arguments, and executes different methods based on the type of
     * sender.
     *
     * @param sender The `sender` parameter represents the entity that executed the command. It can be of type
     * `CommandSender`, which is a common interface for all entities that can execute commands, such as players, the
     * console, or command blocks.
     * @param command The `command` parameter is of type `Command` and represents the command that was executed.
     * @param label The `label` parameter in the `onCommand` function represents the label or alias used to invoke the
     * command. It is a string that identifies the command being executed.
     * @param args An array of strings representing the arguments passed to the command.
     * @return a boolean value.
     */
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

        return if (sender is Player) executePlayer(sender, args) else executeCommand(sender, args)
    }

    /**
     * The function executes a player command and sends an error message if the runner is invalid.
     *
     * @param sender The `sender` parameter represents the player who executed the command. It is of type `Player`.
     * @param args An array of strings representing the arguments passed to the command.
     * @return a boolean value.
     */
    open fun executePlayer(sender: Player, args: Array<String>): Boolean {
        sender.sendJEXTMessage("invalid-runner", hashMapOf(
            "runner" to "console"
        ))

        return true
    }

    /**
     * The function executes a command and sends an error message to the command sender.
     *
     * @param sender The `sender` parameter is of type `CommandSender`, which represents the entity that executed the
     * command. It could be a player, console, or any other entity that has the ability to execute commands.
     * @param args An array of strings representing the arguments passed to the command.
     * @return a boolean value of true.
     */
    open fun executeCommand(sender: CommandSender, args: Array<String>): Boolean {
        sender.sendJEXTMessage("invalid-runner", hashMapOf(
            "runner" to "players"
        ))

        return true
    }
}