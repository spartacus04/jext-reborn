package me.spartacus04.jext.discs.discplaying

import org.bukkit.Location
import org.bukkit.entity.Player

interface DiscPlayingMethod {
    fun playLocation(location: Location, namespace: String, volume: Float, pitch: Float)

    fun playPlayer(player: Player, namespace: String, volume: Float, pitch: Float)
}