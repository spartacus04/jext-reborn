package me.spartacus04.jext.command

import me.spartacus04.jext.command.adapter.ExecutorAdapter
import me.spartacus04.jext.command.adapter.ParameterDisc
import me.spartacus04.jext.command.adapter.ParameterPlayer
import me.spartacus04.jext.config.ConfigData
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

    override fun executePlayer(sender: Player, args: Array<String>): Boolean {
        return mergedExecute(sender, args)
    }

    override fun executeCommand(sender: CommandSender, args: Array<String>): Boolean {
        return mergedExecute(sender, args)
    }

    private fun mergedExecute(sender: CommandSender, args: Array<String>): Boolean {
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
            return true
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
        return true
    }
}