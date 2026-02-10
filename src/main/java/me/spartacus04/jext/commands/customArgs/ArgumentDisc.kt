package me.spartacus04.jext.commands.customArgs

import me.spartacus04.colosseum.commandHandling.argument.Argument
import me.spartacus04.colosseum.commandHandling.exceptions.MalformedArgumentException
import me.spartacus04.jext.Jext
import me.spartacus04.jext.discs.Disc
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

/**
 * An argument for disc names. It will parse the input and return the corresponding [Disc] object.
 * It also provides suggestions for disc names based on the input.
 * Requires Colosseum API to be used in a command.
 *
 * @param plugin The instance of the Jext plugin, used to access the list of discs.
 */
class ArgumentDisc(val plugin: Jext) : Argument<Disc>() {
    /**
     * Parses the input string and returns the corresponding [Disc] object.
     * If no disc with the given name is found, it throws a [MalformedArgumentException].
     *
     * @param input The input string to parse, expected to be the namespace of a disc.
     * @param sender The sender of the command, not used in this argument but required by the interface.
     *
     * @return The [Disc] object corresponding to the input string.
     */
    override fun parse(input: String, sender: CommandSender): Disc {
        return plugin.discs.firstOrNull { it.namespace.equals(input, ignoreCase = true) }
            ?: throw MalformedArgumentException(input, "disc name")
    }

    /**
     * Provides suggestions for disc names based on the input. It returns a list of disc namespaces that start with the input string.
     *
     * @param input The current input string for which suggestions are needed.
     * @param sender The sender of the command, not used in this argument but required by the interface.
     *
     * @return A list of disc namespaces that start with the input string, used for tab-completion suggestions.
     */
    override fun suggest(
        input: String,
        sender: CommandSender
    ): List<String> {
        return plugin.discs.map { it.namespace }.filter { it.startsWith(input) }
    }

    /**
     * Returns the format string for this argument, which is used in command usage messages. It indicates that the argument is a disc name and uses colour coding to differentiate between optional and required arguments.
     *
     * @param isOptional A boolean indicating whether the argument is optional or required, used to determine the format of the returned string.
     * @return A formatted string representing the argument format, with colour coding to indicate whether it is optional or required.
     */
    override fun getParamFormat(isOptional: Boolean): String =
        if (isOptional)
            "${ChatColor.RESET}[${ChatColor.DARK_PURPLE}disc${ChatColor.RESET}]"
        else
            "${ChatColor.RESET}<${ChatColor.LIGHT_PURPLE}disc${ChatColor.RESET}>"
}