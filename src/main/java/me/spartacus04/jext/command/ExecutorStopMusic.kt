package me.spartacus04.jext.command

import me.spartacus04.jext.command.adapter.ExecutorAdapter
import me.spartacus04.jext.command.adapter.ParameterDisc
import me.spartacus04.jext.command.adapter.ParameterPlayer
import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.config.sendJEXTMessage
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

internal class ExecutorStopMusic : ExecutorAdapter("stopmusic") {
    init {
        addParameter(ParameterPlayer(true))
        addParameter(ParameterDisc(false))
    }

    override fun executePlayer(sender: Player, args: Array<String>): Boolean {
        mergedExecute(sender, args)
        return true
    }

    override fun executeCommand(sender: CommandSender, args: Array<String>): Boolean {
        mergedExecute(sender, args)
        return true
    }

    private fun mergedExecute(sender: CommandSender, args: Array<String>) {
        val players = ParameterPlayer.getPlayers(args[0], sender)

        val namespaces: MutableSet<String>

        if (args.size <= 1) {
            namespaces = DISCS.map { it.DISC_NAMESPACE }.toMutableSet()
        } else {
            val disc = ParameterDisc.getDisc(args[1])

            if (disc == null) {
                sender.sendJEXTMessage("disc-namespace-not-found", hashMapOf(
                    "namespace" to args[1]
                ))

                return
            }

            namespaces = HashSet()

            namespaces.add(DiscContainer(disc).namespace)
        }

        for (player in players) {

            for (namespace in namespaces) {

                player.stopSound(namespace, SoundCategory.RECORDS)
                if (namespaces.size == 1) {
                    sender.sendJEXTMessage("stopped-music", hashMapOf(
                        "name" to DISCS.find { it.DISC_NAMESPACE == namespace }!!.TITLE
                    ))
                }
            }

            if (namespaces.size > 1) {
                sender.sendJEXTMessage("stopped-all-music")
            }
        }

        val playerCount = players.size

        if (playerCount >= 2) {
            sender.sendJEXTMessage("stopped-music-for-multiple", hashMapOf(
                "playercount" to playerCount.toString()
            ))
        } else if (playerCount == 1) {
            sender.sendJEXTMessage("stopped-music-for", hashMapOf(
                "player" to players[0].name
            ))
        } else {
            sender.sendJEXTMessage("stopped-music-for-no-one")
        }
    }
}