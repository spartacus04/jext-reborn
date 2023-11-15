package me.spartacus04.jext.commands.adapter

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * This class represents a custom player parameter
 *
 * @constructor Creates a new ParameterPlayer object
 *
 * @param required A boolean value that represents whether the parameter is required or not
 */
class ParameterPlayer internal constructor(required: Boolean) : Parameter(required) {
    override val name: String
        get() = "player"

    override fun onComplete(parameter: String, sender: CommandSender): List<String>? {
        val players = Bukkit.getOnlinePlayers().toList().map { it.name }
        val playersAndSelectors = if(sender !is Player) {
            listOf("@a", "@r").plus(players)
        }
        else {
            listOf("@a", "@r", "@s").plus(players)
        }

        val matches = playersAndSelectors.filter { it.startsWith(parameter, true) }

        return matches.ifEmpty { null }
    }

    companion object {
        /**
         * The function returns a list of players based on a given parameter
         *
         * @param parameter The player name or selector
         * @param sender The command sender
         * @return A list of players
         */
        fun getPlayers(parameter: String, sender: CommandSender): List<Player> {
            return when(parameter) {
                "@a", "@A" -> Bukkit.getOnlinePlayers().toList()
                "@s", "@S" -> if(sender is Player) {
                    return listOf(sender)
                }
                else {
                    return emptyList()
                }
                "@r", "@R" -> Bukkit.getOnlinePlayers().shuffled().subList(0, 1)
                else -> {
                    val player =
                        Bukkit.getOnlinePlayers().find { it.name.lowercase() == parameter.lowercase() } ?: return emptyList()

                    return listOf(player)
                }
            }
        }
    }
}