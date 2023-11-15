package me.spartacus04.jext.commands.adapter

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

/**
 * The abstract class Parameter is used to represent a parameter of a command.
 *
 * @property isRequired The "isRequired" property is a boolean value that represents whether the parameter is required or not.
 * @constructor Creates a Parameter object.
 */
abstract class Parameter(val isRequired: Boolean) {

    /**
     * The abstract property name represents the name of the parameter.
     */
    abstract val name: String?

    /**
     * The abstract method "onComplete" is used to filter a list of strings based on a given parameter and returns the
     * matching strings, or null if there are no matches.
     *
     * @param parameter The parameter is a string that represents the input value
     * @param sender The "sender" parameter is of type CommandSender. It represents the entity that executed the command.
     * @return A list of possible parameter values or null.
     */
    abstract fun onComplete(parameter: String, sender: CommandSender): List<String>?

    /**
     * The function returns a string representation of a parameter, with different formatting depending on whether it is
     * required or not.
     *
     * @return The method is returning a string representation of an object. The returned string depends on the value of
     * the "isRequired" property. If "isRequired" is true, the method returns a string enclosed in square brackets, with
     * the name colored in light purple. If "isRequired" is false, the method returns a string enclosed in angle brackets,
     * with the name colored in dark purple.
     */
    override fun toString(): String {
        return if (isRequired) {
            "[${ChatColor.RESET}${ChatColor.LIGHT_PURPLE}${name}${ChatColor.RESET}]"
        } else {
            "<${ChatColor.RESET}${ChatColor.DARK_PURPLE}${name}${ChatColor.RESET}>"
        }
    }
}