package me.spartacus04.jext.commands.adapter

import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

/**
 * This class represents a location parameter
 *
 * @property axis The axis of the location parameter
 * @constructor Create empty Parameter location
 *
 * @param required A boolean value that represents whether the parameter is required or not
 */
class ParameterLocation internal constructor(required: Boolean, private val axis: Axis) : Parameter(required) {

    /**
     * This enum represents the axis of the location parameter
     *
     * @constructor Create empty Axis
     */
    internal enum class Axis {
        /**
         * X
         *
         * @constructor
         */
        X,
        /**
         * Y
         *
         * @constructor
         */
        Y,
        /**
         * Z
         *
         * @constructor
         */
        Z
    }

    override val name: String
        get() = axis.toString().lowercase(Locale.getDefault())

    override fun onComplete(parameter: String, sender: CommandSender): List<String> {
        val suggestions: MutableList<String> = ArrayList()

        suggestions.add("~")

        val player = (if (sender is Player) sender else null) ?: return suggestions.toList()
        val block = player.getTargetBlockExact(4) ?: return suggestions.toList()

        val location = block.location

        when (axis) {
            Axis.X -> {
                val a = location.blockX
                suggestions.add(a.toString())
            }
            Axis.Y -> {
                val a = location.blockY
                suggestions.add(a.toString())
            }
            Axis.Z -> {
                val a = location.blockZ
                suggestions.add(a.toString())
            }
        }
        return suggestions.toList()
    }

    companion object {
        /**
         * The function "parseLocation" parses the location parameters and returns a Location object
         *
         * @param x The x coordinate
         * @param y The y coordinate
         * @param z The z coordinate
         * @param sender The "sender" parameter is of type Player, which represents the player who executed the command.
         * @return A Location object
         */
        fun parseLocation(x: String, y: String, z: String, sender: Player) : Location {
            val intX: Int = if (x == "~") {
                sender.location.blockX
            } else {
                x.toFloat().toInt()
            }

            val intY: Int = if (y == "~") {
                sender.location.blockY
            } else {
                y.toFloat().toInt()
            }

            val intZ: Int = if (z == "~") {
                sender.location.blockZ
            } else {
                z.toFloat().toInt()
            }

            return Location(sender.world, intX.toDouble(), intY.toDouble(), intZ.toDouble())
        }
    }
}