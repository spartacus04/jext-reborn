package me.spartacus04.jext.command

import me.spartacus04.jext.config.ConfigData
import me.spartacus04.jext.config.send
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

internal class ExecutorFragmentGive : ExecutorAdapter("discgive") {
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
            ConfigData.LANG.format(sender, "disc-namespace-not-found")
                .replace("%namespace%", args[1])
                .let { sender.send(it) }
            return true
        }

        for (player in players) {
            player.inventory.addItem(DiscContainer(disc).fragmentItem)

            ConfigData.LANG.format(sender, "disc-received")
                .replace("%disc%", disc.TITLE)
                .let { sender.send(it) }
        }

        val playerCount = players.size

        if (playerCount >= 2) {
            ConfigData.LANG.format(sender, "disc-given-multiple")
                .replace("%disc%", disc.TITLE)
                .replace("%playercount%", playerCount.toString())
                .let { sender.send(it) }
        } else if (playerCount == 1) {
            ConfigData.LANG.format(sender, "disc-given")
                .replace("%disc%", disc.TITLE)
                .replace("%player%", players[0].name)
                .let { sender.send(it) }
        } else {
            ConfigData.LANG.format(sender, "no-disc-given")
                .let { sender.send(it) }
        }
        return true
    }
}