package me.spartacus04.jext.command

import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.config.ConfigData.Companion.LANG
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscPlayer
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
        val players = PlayerSelector(sender, args[0]).players ?: return true

        val disc = DISCS.find { it.DISC_NAMESPACE == args[0] }
        if (disc == null) {
            sender.sendMessage(
                "[§aJEXT§f]  ${LANG.DISC_NAMESPACE_NOT_FOUND}"
                    .replace("%namespace%", args[0])
            )
            return true
        }

        val discPlayer = DiscPlayer(DiscContainer(disc))

        var pitch = 1.0f
        if (args.size >= 3) {
            pitch = try {
                args[2].toFloat()
            } catch (e: NumberFormatException) {
                sender.sendMessage(
                    "[§aJEXT§f]  ${LANG.WRONG_NUMBER_FORMAT}"
                        .replace("%param%", "pitch")
                )
                return true
            }
        }

        var isMusic = true

        if (args.size >= 4) {
            isMusic = false

            try {
                val volume = args[3].toFloat()
                discPlayer.setPitch(pitch)
                discPlayer.setVolume(volume)
            } catch (e: NumberFormatException) {
                sender.sendMessage(
                    "[§aJEXT§f]  ${LANG.WRONG_NUMBER_FORMAT}"
                        .replace("%param%", "volume")
                )
                return true
            }
        }

        for (player in players) {
            if (isMusic) {
                player!!.playSound(player.location, DiscContainer(disc).namespace, SoundCategory.RECORDS, Float.MAX_VALUE, pitch)
                player.sendMessage(
                    "[§aJEXT§f]  ${LANG.MUSIC_NOW_PLAYING}"
                        .replace("%name%", disc.TITLE)
                )
            } else {
                discPlayer.play(player!!.location)
            }
        }

        val playerCount = players.size

        if (playerCount >= 2) {
            sender.sendMessage(
                "[§aJEXT§f]  ${LANG.PLAYED_MUSIC_TO_MULTIPLE}"
                    .replace("%name%", disc.TITLE)
                    .replace("%playercount%", playerCount.toString())
            )
        } else if (playerCount == 1) {
            sender.sendMessage(
                "[§aJEXT§f]  ${LANG.PLAYED_MUSIC_TO}"
                    .replace("%name%", disc.TITLE)
                    .replace("%player%", players[0]!!.name)
            )
        } else {
            sender.sendMessage(
                "[§aJEXT§f]  ${LANG.PLAYED_MUSIC_TO_NO_ONE}"
            )
        }

        return true
    }
}