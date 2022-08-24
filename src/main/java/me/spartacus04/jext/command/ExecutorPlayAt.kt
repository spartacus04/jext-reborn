package me.spartacus04.jext.command

import me.spartacus04.jext.config.ConfigData.Companion.LANG
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscPlayer
import org.bukkit.entity.Player

internal class ExecutorPlayAt : ExecutorAdapter("playat") {
    init {
        addParameter(ParameterDisc(true))
        addParameter(ParameterLocation(true, ParameterLocation.Axis.X))
        addParameter(ParameterLocation(true, ParameterLocation.Axis.Y))
        addParameter(ParameterLocation(true, ParameterLocation.Axis.Z))
        addParameter(ParameterNumber(false, 0.5f, 1.0f, 1.5f).setName("pitch"))
        addParameter(ParameterNumber(false, 4.0f, 1.0f, 0.5f).setName("volume"))
    }

    override fun executePlayer(sender: Player, args: Array<String>): Boolean {
        val location = try {
            ParameterLocation.parseLocation(args[1], args[2], args[3], sender)
        } catch (e: NumberFormatException) {
            sender.sendMessage(
                "[§aJEXT§f]  ${LANG.INVALID_LOCATION}"
            )
            return true
        }

        val disc = ParameterDisc.getDisc(args[0])

        if (disc == null) {
            sender.sendMessage(
                "[§aJEXT§f]  ${LANG.DISC_NAMESPACE_NOT_FOUND}"
                    .replace("%namespace%", args[0])
            )
            return true
        }

        val discPlayer = DiscPlayer(DiscContainer(disc))

        if (args.size >= 5) {
            try {
                val pitch = args[4].toFloat()
                discPlayer.setPitch(pitch)
            } catch (e: NumberFormatException) {
                sender.sendMessage(
                    "[§aJEXT§f]  ${LANG.WRONG_NUMBER_FORMAT}"
                        .replace("%param%", "pitch")
                )
                return true
            }
        }

        if (args.size >= 6) {
            try {
                val volume = args[5].toFloat()
                discPlayer.setVolume(volume)
            } catch (e: NumberFormatException) {
                sender.sendMessage(
                    "[§aJEXT§f]  ${LANG.WRONG_NUMBER_FORMAT}"
                        .replace("%param%", "volume")
                )
                return true
            }
        }

        discPlayer.play(location)
        return true
    }
}