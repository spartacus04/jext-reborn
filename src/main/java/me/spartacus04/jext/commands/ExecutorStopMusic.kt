package me.spartacus04.jext.commands

import me.spartacus04.jext.State.DISCS
import me.spartacus04.jext.commands.adapter.ExecutorAdapter
import me.spartacus04.jext.commands.adapter.ParameterDisc
import me.spartacus04.jext.commands.adapter.ParameterPlayer
import me.spartacus04.jext.utils.sendJEXTMessage
import org.bukkit.*
import org.bukkit.command.CommandSender

/**
 * ExecutorStopMusic is a class used to register the "stopmusic" command to the plugin.
 *
 * @constructor The constructor is empty because the class does not have any properties.
 */
internal class ExecutorStopMusic : ExecutorAdapter("stopmusic") {
    init {
        addParameter(ParameterPlayer(true))
        addParameter(ParameterDisc(false))
    }

    /**
     * The function `execute` stops the music for the specified players.
     *
     * @param sender The sender who executed the command.
     * @param args The arguments that were passed to the command.
     */
    override fun execute(sender: CommandSender, args: Array<String>) {
        val players = ParameterPlayer.getPlayers(args[0], sender)

        val namespaces: MutableSet<String>

        if (args.size <= 1) {
            namespaces = DISCS.map { it.namespace }.toMutableSet()
        } else {
            val disc = ParameterDisc.getDisc(args[1])

            if (disc == null) {
                sender.sendJEXTMessage("disc-namespace-not-found", hashMapOf(
                    "namespace" to args[1]
                ))

                return
            }

            namespaces = HashSet()

            namespaces.add(disc.namespace)
        }

        for (player in players) {

            for (namespace in namespaces) {

                player.stopSound(namespace, SoundCategory.RECORDS)
                if (namespaces.size == 1) {
                    sender.sendJEXTMessage("stopped-music", hashMapOf(
                        "name" to DISCS[namespace]!!.displayName
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