package me.spartacus04.jext.command

import me.spartacus04.jext.command.adapter.ExecutorAdapter
import me.spartacus04.jext.command.adapter.ParameterDisc
import me.spartacus04.jext.command.adapter.ParameterNumber
import me.spartacus04.jext.command.adapter.ParameterPlayer
import me.spartacus04.jext.config.sendJEXTMessage
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

internal class ExecutorPlayMusic : ExecutorAdapter("playmusic") {
    init {
        addParameter(ParameterPlayer(true))
        addParameter(ParameterDisc(true))
        addParameter(ParameterNumber(false, 0.5f, 1.0f, 1.5f).setName("pitch"))
        addParameter(ParameterNumber(false, 4.0f, 1.0f, 0.5f).setName("volume"))
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
            sender.sendJEXTMessage("disc-namespace-not-found", hashMapOf(
                "namespace" to args[1]
            ))

            return true
        }

        val discContainer = DiscContainer(disc)

        var pitch = 1.0f
        if (args.size >= 3) {
            pitch = try {
                args[2].toFloat()
            } catch (e: NumberFormatException) {
                sender.sendJEXTMessage("wrong-number-format", hashMapOf(
                    "param" to "pitch"
                ))

                return true
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

                return true
            }
        }

        for (player in players) {
            if (isMusic) {
                player.playSound(player.location, DiscContainer(disc).namespace, SoundCategory.RECORDS, Float.MAX_VALUE, pitch)
                sender.sendJEXTMessage("music-now-playing", hashMapOf(
                    "name" to disc.TITLE
                ))
            } else {
                discContainer.play(player.location, pitch, volume)
            }
        }

        val playerCount = players.size

        if (playerCount >= 2) {
            sender.sendJEXTMessage("played-music-to-multiple", hashMapOf(
                "name" to disc.TITLE,
                "playercount" to playerCount.toString()
            ))
        } else if (playerCount == 1) {
            sender.sendJEXTMessage("played-music-to", hashMapOf(
                "name" to disc.TITLE,
                "player" to players[0].name
            ))
        } else {
            sender.sendJEXTMessage("played-music-to-no-one")
        }

        return true
    }
}