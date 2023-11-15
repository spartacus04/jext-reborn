package me.spartacus04.jext.commands

import me.spartacus04.jext.commands.adapter.ExecutorAdapter
import me.spartacus04.jext.commands.adapter.ParameterDisc
import me.spartacus04.jext.commands.adapter.ParameterNumber
import me.spartacus04.jext.commands.adapter.ParameterPlayer
import me.spartacus04.jext.utils.sendJEXTMessage
import org.bukkit.command.CommandSender

/**
 * ExecutorDiscGive is a class used to register the "discgive" command to the plugin.
 *
 * @constructor The constructor is empty because the class does not have any properties.
 */
internal class ExecutorDiscGive : ExecutorAdapter("discgive") {
    init {
        addParameter(ParameterPlayer(true))
        addParameter(ParameterDisc(true))
        addParameter(ParameterNumber(false, 1).setName("amount"))
    }

    /**
     * The function `execute` gives the selected players a disc.
     *
     * @param sender The sender who executed the command.
     * @param args The arguments that were passed to the command.
     */
    override fun execute(sender: CommandSender, args: Array<String>) {
        val players = ParameterPlayer.getPlayers(args[0], sender)

        val disc = ParameterDisc.getDisc(args[1])

        if (disc == null) {
            sender.sendJEXTMessage("disc-namespace-not-found", hashMapOf(
                "namespace" to args[1]
            ))

            return
        }

        var amount = 1
        if(args.size >= 3) {
            try {
                amount = args[2].toInt()
            } catch (e: NumberFormatException) {
                sender.sendJEXTMessage("wrong-number-format", hashMapOf(
                    "param" to "amount"
                ))
                return
            }
        }

        for (player in players) {
            for(i in 1..amount) {
                player.inventory.addItem(disc.discItemStack)
            }

            sender.sendJEXTMessage("disc-received", hashMapOf(
                "disc" to disc.displayName
            ))
        }

        val playerCount = players.size

        if (playerCount >= 2) {
            sender.sendJEXTMessage("disc-given-multiple", hashMapOf(
                "disc" to disc.displayName,
                "playercount" to playerCount.toString()
            ))
        } else if (playerCount == 1) {
            sender.sendJEXTMessage("disc-given", hashMapOf(
                "disc" to disc.displayName,
                "player" to players[0].name
            ))
        } else {
            sender.sendJEXTMessage("no-disc-given")
        }
    }
}