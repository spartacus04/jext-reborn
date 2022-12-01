package me.spartacus04.jext.command

import me.spartacus04.jext.config.ConfigData.Companion.LANG
import me.spartacus04.jext.config.send
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

internal class ExecutorDiscGive : ExecutorAdapter("discgive") {
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
        val players = ParameterPlayer.getPlayers(args[0], sender)

        val disc = ParameterDisc.getDisc(args[1])

        if (disc == null) {
            sender.send(
                LANG.format(sender, "disc-namespace-not-found")
                    .replace("%namespace%", args[1])
            )
            return true
        }

        for (player in players) {
            player.inventory.addItem(DiscContainer(disc).discItem)

            player.send(
                LANG.format(sender, "disc-received")
                    .replace("%disc%", disc.TITLE)
            )
        }

        val playerCount = players.size

        if (playerCount >= 2) {
            sender.send(
                LANG.format(sender, "disc-given-multiple")
                    .replace("%disc%", disc.TITLE)
                    .replace("%playercount%", playerCount.toString())
            )
        } else if (playerCount == 1) {
            sender.send(
                LANG.format(sender, "disc-given")
                    .replace("%disc%", disc.TITLE)
                    .replace("%player%", players[0].name)
            )
        } else {
            sender.send(
                LANG.format(sender, "no-disc-given")
            )
        }
        return true
    }
}