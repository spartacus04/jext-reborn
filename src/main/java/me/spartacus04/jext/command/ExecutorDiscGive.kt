package me.spartacus04.jext.command

import me.spartacus04.jext.command.adapter.ExecutorAdapter
import me.spartacus04.jext.command.adapter.ParameterDisc
import me.spartacus04.jext.command.adapter.ParameterPlayer
import me.spartacus04.jext.config.sendJEXTMessage
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * ExecutorDiscGive is a class used to register the "discgive" command to the plugin.
 *
 * @constructor The constructor is empty because the class does not have any properties.
 */
internal class ExecutorDiscGive : ExecutorAdapter("discgive") {
    init {
        addParameter(ParameterPlayer(true))
        addParameter(ParameterDisc(true))
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