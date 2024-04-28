package me.spartacus04.jext.discs.discstopping

import org.bukkit.Location
import org.bukkit.entity.Player

interface DiscStoppingMethod {
    val requires: List<String>

    fun stop(player: Player)

    fun stop(player: Player, namespace: String)

    fun stop(location: Location)

    fun stop(location: Location, namespace: String)
}