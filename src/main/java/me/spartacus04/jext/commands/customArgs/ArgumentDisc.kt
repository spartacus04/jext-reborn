package me.spartacus04.jext.commands.customArgs

import me.spartacus04.colosseum.commandHandling.argument.Argument
import me.spartacus04.colosseum.commandHandling.exceptions.MalformedArgumentException
import me.spartacus04.jext.Jext
import me.spartacus04.jext.discs.Disc
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

class ArgumentDisc(val plugin: Jext) : Argument<Disc>() {
    override fun parse(input: String, sender: CommandSender): Disc {
        return plugin.discs.firstOrNull { it.namespace.equals(input, ignoreCase = true) }
            ?: throw MalformedArgumentException(input, "disc name")
    }

    override fun suggest(
        input: String,
        sender: CommandSender
    ): List<String> {
        return plugin.discs.map { it.namespace }.filter { it.startsWith(input) }
    }

    override fun getParamFormat(isOptional: Boolean): String =
        if (isOptional)
            "${ChatColor.RESET}[${ChatColor.DARK_PURPLE}disc${ChatColor.RESET}]"
        else
            "${ChatColor.RESET}<${ChatColor.LIGHT_PURPLE}disc${ChatColor.RESET}>"
}