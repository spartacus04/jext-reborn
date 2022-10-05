package me.spartacus04.jext.command

import me.spartacus04.jext.Log
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

abstract class Parameter(val isRequired: Boolean) {

    abstract val name: String?

    abstract fun onComplete(parameter: String, sender: CommandSender): List<String>?

    override fun toString(): String {
        val sms = Log().rst(if (isRequired) "[" else "<").rst().rst(if (isRequired) "]" else ">")
        return sms.text(if (isRequired) ChatColor.LIGHT_PURPLE.toString() + name else ChatColor.DARK_PURPLE.toString() + name)
    }
}