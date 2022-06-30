package me.spartacus04.jext.command

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class ParameterPlayer internal constructor(required: Boolean) : Parameter(required) {
    override val name: String
        get() = "player"

    override fun onComplete(parameter: String, sender: CommandSender?): List<String>? {
        val players: List<Player> = ArrayList(Bukkit.getOnlinePlayers())

        val selectors: Set<String> = PlayerSelector.selectorStrings
        val matches: MutableList<String> = ArrayList()

        for (selector in selectors) {
            if (matches.size >= 12) break

            if (selector.startsWith(parameter.lowercase(Locale.getDefault()))) {
                matches.add(selector)
            }
        }

        for (player in players) {
            if (matches.size >= 12) break

            val name = player.name
            if (name.lowercase(Locale.getDefault()).startsWith(parameter.lowercase(Locale.getDefault()))) {
                matches.add(name)
            }
        }
        return if (matches.size > 0) matches.toList() else null
    }
}