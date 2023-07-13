package me.spartacus04.jext.command.adapter

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

abstract class Parameter(val isRequired: Boolean) {

    abstract val name: String?

    /**
     * The function onComplete takes a parameter of type String and a sender of type CommandSender, and returns a list of
     * strings or null.
     *
     * @param parameter A string parameter that represents some input or data.
     * @param sender The `sender` parameter is of type `CommandSender`. It represents the entity that executed the command.
     * It could be a player, console, or any other entity that has the ability to execute commands.
     */
    abstract fun onComplete(parameter: String, sender: CommandSender): List<String>?

    /**
     * The function returns a string representation of an object, with different formatting depending on whether it is
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