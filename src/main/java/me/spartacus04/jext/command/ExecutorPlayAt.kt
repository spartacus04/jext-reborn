package me.spartacus04.jext.command

import me.spartacus04.jext.Log
import me.spartacus04.jext.config.ConfigData
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscPlayer
import org.bukkit.entity.Player

internal class ExecutorPlayAt : ExecutorAdapter("playat") {
    init {
        addParameter(ParameterLocation(true, ParameterLocation.Axis.X))
        addParameter(ParameterLocation(true, ParameterLocation.Axis.Y))
        addParameter(ParameterLocation(true, ParameterLocation.Axis.Z))
        addParameter(ParameterDisc(true))
        addParameter(ParameterNumber(false, 0.5f, 1.0f, 1.5f).setName("pitch"))
        addParameter(ParameterNumber(false, 4.0f, 1.0f, 0.5f).setName("volume"))
    }

    override fun executePlayer(sender: Player, args: Array<String>): Boolean {
        val location = try {
            LocationParser(args[0], args[1], args[2], sender).parse()
        } catch (e: NumberFormatException) {
            Log().eror().t("Invalid location value!").send(sender)
            return true
        }

        val disc = ConfigData.DISCS.find { it.DISC_NAMESPACE == args[0] }

        if (disc == null) {
            Log().eror().t("Disc with the namespace ").o(args[3]).t(" doesn't exists.").send(sender)
            return true
        }

        val discPlayer = DiscPlayer(DiscContainer(disc))

        if (args.size >= 5) {
            try {
                val pitch = args[4].toFloat()
                discPlayer.setPitch(pitch)
            } catch (e: NumberFormatException) {
                Log().eror().t("Wrong number format for pitch parameter.").send(sender)
                return true
            }
        }

        if (args.size >= 6) {
            try {
                val volume = args[5].toFloat()
                discPlayer.setVolume(volume)
            } catch (e: NumberFormatException) {
                Log().eror().t("Wrong number format for volume parameter.").send(sender)
                return true
            }
        }

        discPlayer.play(location)
        return true
    }
}