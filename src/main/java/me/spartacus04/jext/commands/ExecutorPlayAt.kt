package me.spartacus04.jext.commands

import me.spartacus04.jext.commands.adapter.ExecutorAdapter
import me.spartacus04.jext.commands.adapter.ParameterDisc
import me.spartacus04.jext.commands.adapter.ParameterLocation
import me.spartacus04.jext.commands.adapter.ParameterNumber
import me.spartacus04.jext.utils.sendJEXTMessage
import org.bukkit.entity.Player

/**
 * ExecutorPlayAt is a class used to register the "playat" command to the plugin.
 *
 * @constructor The constructor is empty because the class does not have any properties.
 */
internal class ExecutorPlayAt : ExecutorAdapter("playat") {
    init {
        addParameter(ParameterDisc(true))
        addParameter(ParameterLocation(true, ParameterLocation.Axis.X))
        addParameter(ParameterLocation(true, ParameterLocation.Axis.Y))
        addParameter(ParameterLocation(true, ParameterLocation.Axis.Z))
        addParameter(ParameterNumber(false, 0.5f, 1.0f, 1.5f).setName("pitch"))
        addParameter(ParameterNumber(false, 4.0f, 1.0f, 0.5f).setName("volume"))
    }

    /**
     * The function `executePlayer` plays a disc at a specific location.
     *
     * @param sender The player who executed the command.
     * @param args The arguments that were passed to the command.
     */
    override fun executePlayer(sender: Player, args: Array<String>) {
        val location = try {
            ParameterLocation.parseLocation(args[1], args[2], args[3], sender)
        } catch (e: NumberFormatException) {
            sender.sendJEXTMessage("invalid-location")
            return
        }

        val disc = ParameterDisc.getDisc(args[0])

        if (disc == null) {
            sender.sendJEXTMessage("disc-namespace-not-found", hashMapOf(
                "namespace" to args[0]
            ))

            return
        }

        var pitch = 1.0f
        if (args.size >= 5) {
            try {
                pitch = args[4].toFloat()
            } catch (e: NumberFormatException) {
                sender.sendJEXTMessage("wrong-number-format", hashMapOf(
                    "param" to "pitch"
                ))
                return
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
                return
            }
        }

        disc.play(location, volume, pitch)
        return
    }
}