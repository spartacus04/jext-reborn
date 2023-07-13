package me.spartacus04.jext.command

import me.spartacus04.jext.command.adapter.ExecutorAdapter
import me.spartacus04.jext.command.adapter.ParameterDisc
import me.spartacus04.jext.command.adapter.ParameterPlayer
import me.spartacus04.jext.config.sendJEXTMessage
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

internal class ExecutorDiscGive : ExecutorAdapter("discgive") {
    init {
        addParameter(ParameterPlayer(true))
        addParameter(ParameterDisc(true))
    }

    /**
     * The function executes a command for a player in Kotlin.
     *
     * @param sender The "sender" parameter is of type "Player", which represents the player who executed the command.
     * @param args An array of strings representing the arguments passed to the command.
     * @return The method is returning a boolean value of true.
     */
    override fun executePlayer(sender: Player, args: Array<String>): Boolean {
        mergedExecute(sender, args)
        return true
    }

    /**
     * The function executes a command and returns a boolean value indicating success.
     *
     * @param sender The `sender` parameter is of type `CommandSender`. It represents the entity that executed the command.
     * It could be a player, console, or any other entity that has the ability to execute commands.
     * @param args An array of strings representing the arguments passed to the command.
     * @return a boolean value of true.
     */
    override fun executeCommand(sender: CommandSender, args: Array<String>): Boolean {
        mergedExecute(sender, args)
        return true
    }

    /**
     * The function `mergedExecute` adds a disc item to the inventory of multiple players and sends messages to the sender
     * based on the number of players and the disc.
     *
     * @param sender The `sender` parameter is of type `CommandSender`. It represents the entity that executed the command.
     * It could be a player, console, or any other entity that has the ability to execute commands.
     * @param args args is an array of strings that contains the command arguments. In this case, args[0] is expected to be
     * the player name or selector, and args[1] is expected to be the disc namespace.
     * @return The function `mergedExecute` returns nothing (Unit).
     */
    private fun mergedExecute(sender: CommandSender, args: Array<String>) {
        val players = ParameterPlayer.getPlayers(args[0], sender)

        val disc = ParameterDisc.getDisc(args[1])

        if (disc == null) {
            sender.sendJEXTMessage("disc-namespace-not-found", hashMapOf(
                "namespace" to args[1]
            ))

            return
        }

        for (player in players) {
            player.inventory.addItem(DiscContainer(disc).discItem)

            sender.sendJEXTMessage("disc-received", hashMapOf(
                "disc" to disc.TITLE
            ))
        }

        val playerCount = players.size

        if (playerCount >= 2) {
            sender.sendJEXTMessage("disc-given-multiple", hashMapOf(
                "disc" to disc.TITLE,
                "playercount" to playerCount.toString()
            ))
        } else if (playerCount == 1) {
            sender.sendJEXTMessage("disc-given", hashMapOf(
                "disc" to disc.TITLE,
                "player" to players[0].name
            ))
        } else {
            sender.sendJEXTMessage("no-disc-given")
        }
    }
}