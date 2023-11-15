package me.spartacus04.jext.commands

import me.spartacus04.jext.State.VERSION
import me.spartacus04.jext.commands.adapter.ExecutorAdapter
import me.spartacus04.jext.commands.adapter.ParameterDisc
import me.spartacus04.jext.commands.adapter.ParameterNumber
import me.spartacus04.jext.commands.adapter.ParameterPlayer
import me.spartacus04.jext.utils.sendJEXTMessage
import org.bukkit.command.CommandSender

/**
 * ExecutorFragmentGive is a class used to register the "fragmentgive" command to the plugin.
 *
 * @constructor The constructor is empty because the class does not have any properties.
 */
internal class ExecutorFragmentGive : ExecutorAdapter("fragmentgive") {
    init {
        addParameter(ParameterPlayer(true))
        addParameter(ParameterDisc(true))
        addParameter(ParameterNumber(false, 1).setName("amount"))
    }

    /**
     * The function `execute` gives the selected players a disc fragment.
     *
     * @param sender The sender who executed the command.
     * @param args The arguments that were passed to the command.
     */
    override fun execute(sender: CommandSender, args: Array<String>) {
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

        var count = 1
        if(args.size >= 3) {
            try {
                count = args[2].toInt()
            } catch (e: NumberFormatException) {
                sender.sendJEXTMessage("wrong-number-format", hashMapOf(
                    "param" to "amount"
                ))
                return
            }
        }

        for (player in players) {
            player.inventory.addItem(disc.fragmentItemStack!!.apply {
                amount = count
            })

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