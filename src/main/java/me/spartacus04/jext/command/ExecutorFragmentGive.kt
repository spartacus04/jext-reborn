package me.spartacus04.jext.command

import me.spartacus04.jext.command.adapter.ExecutorAdapter
import me.spartacus04.jext.command.adapter.ParameterDisc
import me.spartacus04.jext.command.adapter.ParameterPlayer
import me.spartacus04.jext.config.ConfigData.Companion.VERSION
import me.spartacus04.jext.config.sendJEXTMessage
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

internal class ExecutorFragmentGive : ExecutorAdapter("fragmentgive") {
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
     * The function `mergedExecute` takes in a command sender and an array of arguments, checks if the version is
     * supported, retrieves players based on the first argument, retrieves a disc based on the second argument, adds the
     * disc to each player's inventory, and sends messages to the sender based on the results.
     *
     * @param sender The `sender` parameter is of type `CommandSender`, which represents the entity that executed the
     * command. It could be a player, console, or a command block.
     * @param args - args[0]: The first argument passed to the function, which represents the players to give the disc to.
     * @return Nothing is being returned in this code. The function `mergedExecute` is a void function, meaning it does not
     * return any value.
     */
    private fun mergedExecute(sender: CommandSender, args: Array<String>) {
        if(VERSION < "1.19") {
            sender.sendJEXTMessage("command-not-supported", hashMapOf(
                "command" to "fragmentgive",
                "reason" to "not supported in < 1.19"
            ))
        }

        val players = ParameterPlayer.getPlayers(args[0], sender)

        val disc = ParameterDisc.getDisc(args[1])

        if (disc == null) {
            sender.sendJEXTMessage("disc-namespace-not-found", hashMapOf(
                "namespace" to args[1]
            ))
            return
        }

        for (player in players) {
            player.inventory.addItem(DiscContainer(disc).fragmentItem)

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