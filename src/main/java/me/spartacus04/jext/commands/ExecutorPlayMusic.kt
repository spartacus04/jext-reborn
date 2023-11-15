package me.spartacus04.jext.commands

import me.spartacus04.jext.commands.adapter.ExecutorAdapter
import me.spartacus04.jext.commands.adapter.ParameterDisc
import me.spartacus04.jext.commands.adapter.ParameterNumber
import me.spartacus04.jext.commands.adapter.ParameterPlayer
import me.spartacus04.jext.utils.sendJEXTMessage
import org.bukkit.*
import org.bukkit.command.CommandSender

/**
 * ExecutorPlayMusic is a class used to register the "playmusic" command to the plugin.
 *
 * @constructor The constructor is empty because the class does not have any properties.
 */
internal class ExecutorPlayMusic : ExecutorAdapter("playmusic") {
    init {
        addParameter(ParameterPlayer(true))
        addParameter(ParameterDisc(true))
        addParameter(ParameterNumber(false, 0.5f, 1.0f, 1.5f).setName("pitch"))
        addParameter(ParameterNumber(false, 4.0f, 1.0f, 0.5f).setName("volume"))
    }

    /**
     * The function `execute` plays a disc to all selected players.
     *
     * @param sender The player who executed the command.
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

        var pitch = 1.0f
        if (args.size >= 3) {
            pitch = try {
                args[2].toFloat()
            } catch (e: NumberFormatException) {
                sender.sendJEXTMessage("wrong-number-format", hashMapOf(
                    "param" to "pitch"
                ))

                return
            }
        }

        var isMusic = true

        var volume = 4.0f
        if (args.size >= 4) {
            isMusic = false

            try {
                volume = args[3].toFloat()
            } catch (e: NumberFormatException) {
                sender.sendJEXTMessage("wrong-number-format", hashMapOf(
                    "param" to "volume"
                ))

                return
            }
        }

        for (player in players) {
            if (isMusic) {
                player.playSound(player.location, disc.namespace, SoundCategory.RECORDS, Float.MAX_VALUE, pitch)
                sender.sendJEXTMessage("music-now-playing", hashMapOf(
                    "name" to disc.displayName
                ))
            } else {
                disc.play(player, pitch, volume)
            }
        }

        val playerCount = players.size

        if (playerCount >= 2) {
            sender.sendJEXTMessage("played-music-to-multiple", hashMapOf(
                "name" to disc.namespace,
                "playercount" to playerCount.toString()
            ))
        } else if (playerCount == 1) {
            sender.sendJEXTMessage("played-music-to", hashMapOf(
                "name" to disc.displayName,
                "player" to players[0].name
            ))
        } else {
            sender.sendJEXTMessage("played-music-to-no-one")
        }
    }
}