package me.spartacus04.jext.discs.discplaying

import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * The interface `DiscPlayingMethod` is used to define the methods used to play a disc.
 */
interface DiscPlayingMethod {
    /**
     * Plays the disc at the specified location.
     * 
     * @param location The location to play the disc at.
     * @param namespace The namespace of the disc to play.
     * @param volume The volume of the disc.
     * @param pitch The pitch of the disc.
     */
    fun playLocation(location: Location, namespace: String, volume: Float, pitch: Float)

    /**
     * Plays the disc for the specified player.
     * 
     * @param player The player to play the disc for.
     * @param namespace The namespace of the disc to play.
     * @param volume The volume of the disc.
     * @param pitch The pitch of the disc.
     */
    fun playPlayer(player: Player, namespace: String, volume: Float, pitch: Float)
}