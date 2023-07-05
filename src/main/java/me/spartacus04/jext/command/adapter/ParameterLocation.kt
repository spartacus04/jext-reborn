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