package me.spartacus04.jext.command.adapter

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ParameterPlayer internal constructor(required: Boolean) : Parameter(required) {
    override val name: String
        get() = "player"

    /**
     * The function `onComplete` takes a parameter and a sender, and returns a list of players and selectors that match the
     * parameter, or null if there are no matches.
     *
     * @param parameter The `parameter` is a string that represents the input provided by the user. It is used to filter
     * the list of players and selectors to find matches based on the input.
     * @param sender The `sender` parameter is of type `CommandSender`. It represents the entity that executed the command.
     * It can be a player, console, or command block.
     * @return a list of strings that match the given parameter. If there are no matches, it returns null.
     */
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
         * The function `getPlayers` returns a list of players based on the given parameter, where the parameter can be
         * "@a" for all players, "@s" for the sender player, "@r" for a random player, or a player name.
         *
         * @param parameter The `parameter` is a string that represents the input parameter for the `getPlayers` function.
         * It is used to determine which players to retrieve based on different conditions.
         * @param sender The `sender` parameter is of type `CommandSender`, which represents the entity that executed the
         * command. It can be either a player or the console.
         * @return The function `getPlayers` returns a list of players based on the given parameter.
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