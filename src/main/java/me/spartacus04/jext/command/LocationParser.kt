package me.spartacus04.jext.command

import org.bukkit.Location
import org.bukkit.entity.Player

class LocationParser(
    private val stringX: String,
    private val stringY: String,
    private val stringZ: String,
    private val sender: Player
) {
    fun parse(): Location {
        val intX: Int = if (stringX == "~") {
            sender.location.blockX
        } else {
            stringX.toFloat().toInt()
        }

        val intY: Int = if (stringY == "~") {
            sender.location.blockY
        } else {
            stringY.toFloat().toInt()
        }

        val intZ: Int = if (stringZ == "~") {
            sender.location.blockZ
        } else {
            stringZ.toFloat().toInt()
        }

        return Location(sender.world, intX.toDouble(), intY.toDouble(), intZ.toDouble())
    }
}