package me.spartacus04.jext.command.adapter

import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class ParameterLocation internal constructor(required: Boolean, private val axis: Axis) : Parameter(required) {
    internal enum class Axis {
        X, Y, Z
    }

    override val name: String
        get() = axis.toString().lowercase(Locale.getDefault())

    /**
     * The function `onComplete` takes a parameter and a sender, and returns a list of suggestions based on the target
     * block's location.
     *
     * @param parameter The `parameter` parameter is a `String` that represents the input parameter for the `onComplete`
     * function. It is used to provide additional information or context for generating suggestions.
     * @param sender The `sender` parameter is of type `CommandSender`. It represents the entity that executed the command.
     * It can be a player, console, or command block. In this code, it is cast to a `Player` if it is an instance of
     * `Player`, otherwise it returns `null`.
     * @return a list of suggestions as strings.
     */
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
         * The function `parseLocation` takes in three string parameters representing coordinates and a `Player` object,
         * and returns a `Location` object based on the provided coordinates and the player's world.
         *
         * @param x The parameter `x` is a string representing the x-coordinate of a location. It can either be a numeric
         * value or the tilde character (~) which represents the current x-coordinate of the `sender` player.
         * @param y The parameter `y` represents the y-coordinate of a location. It can be either a specific value or the
         * tilde symbol (~) which represents the current y-coordinate of the `sender` player.
         * @param z The parameter "z" in the `parseLocation` function represents the z-coordinate of a location. It is a
         * string that can either be a numeric value or "~" (tilde). If it is "~", it means that the z-coordinate should be
         * set to the z-coordinate of the sender's
         * @param sender The `sender` parameter is of type `Player`. It represents the player who is sending the command or
         * action that requires a location to be parsed.
         * @return The function `parseLocation` returns a `Location` object.
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