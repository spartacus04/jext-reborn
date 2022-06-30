package me.spartacus04.jext.command

import me.spartacus04.jext.Log
import me.spartacus04.jext.config.ConfigData.Companion.DISCS
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
            Log().eror().t("Disc with the namespace ").o(args[1]).t(" doesn't exists.").send(sender)
            return true
        }

        val discPlayer = DiscPlayer(DiscContainer(disc))

        var pitch = 1.0f
        if (args.size >= 3) {
            pitch = try {
                args[2].toFloat()
            } catch (e: NumberFormatException) {
                Log().eror().t("Wrong number format for pitch parameter.").send(sender)
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
                Log().eror().t("Wrong number format for volume parameter.").send(sender)
                return true
            }
        }

        for (player in players) {
            if (isMusic) {
                player!!.playSound(player.location, DiscContainer(disc).namespace, SoundCategory.RECORDS, Float.MAX_VALUE, pitch)
                Log().info().t("Music ").p().t(" is playing now.").send(player, disc)
            } else {
                discPlayer.play(player!!.location)
            }
        }

        val playerCount = players.size

        if (playerCount >= 2) {
            Log().warn().t("Played music ").o().t(" to ").o().t(" players!").send(sender, disc, playerCount)
        } else if (playerCount == 1) {
            Log().okay().t("Played music ").o().t(" to ").o(players[0]!!.displayName).t(".").send(sender, disc)
        } else {
            Log().eror().t("Played music to no player, something went wrong!").send(sender)
        }

        return true
    }
}