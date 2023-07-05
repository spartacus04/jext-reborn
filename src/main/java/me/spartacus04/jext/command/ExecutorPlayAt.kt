package me.spartacus04.jext.command

import me.spartacus04.jext.command.adapter.ExecutorAdapter
import me.spartacus04.jext.command.adapter.ParameterDisc
import me.spartacus04.jext.command.adapter.ParameterLocation
import me.spartacus04.jext.command.adapter.ParameterNumber
import me.spartacus04.jext.config.sendJEXTMessage
import me.spartacus04.jext.disc.DiscContainer
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
            sender.sendJEXTMessage("invalid-location")
            return true
        }

        val disc = ParameterDisc.getDisc(args[0])

        if (disc == null) {
            sender.sendJEXTMessage("disc-namespace-not-found", hashMapOf(
                "namespace" to args[0]
            ))

            return true
        }

        val discContainer = DiscContainer(disc)

        var pitch = 1.0f
        if (args.size >= 5) {
            try {
                pitch = args[4].toFloat()
            } catch (e: NumberFormatException) {
                sender.sendJEXTMessage("wrong-number-format", hashMapOf(
                    "param" to "pitch"
                ))
                return true
            }
        }

        var volume = 4.0f
        if (args.size >= 6) {
            try {
                volume = args[5].toFloat()
            } catch (e: NumberFormatException) {
                sender.sendJEXTMessage("wrong-number-format", hashMapOf(
                    "param" to "volume"
                ))
                return true
            }
        }

        discContainer.play(location, volume, pitch)
        return true
    }
}