package me.spartacus04.jext.discs.discstopping

import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * The interface `DiscStoppingMethod` is used to define the methods used to stop a disc.
 */
interface DiscStoppingMethod {
    /**
     * Defines the list of required plugins for the disc stopping method.
     */
    val requires: List<String>

    /**
     * Stops the disc for the specified player.
     * 
     * @param player The player to stop the disc for.
     */
    fun stop(player: Player)

    /**
     * Stops the disc for the specified player with the specified namespace.
     * 
     * @param player The player to stop the disc for.
     * @param namespace The namespace of the disc to stop.
     */
    fun stop(player: Player, namespace: String)

    /**
     * Stops the disc for the specified location.
     * 
     * @param location The location to stop the disc for.
     */
    fun stop(location: Location)

    /**
     * Stops the disc for the specified location with the specified namespace.
     * 
     * @param location The location to stop the disc for.
     * @param namespace The namespace of the disc to stop.
     */
    fun stop(location: Location, namespace: String)
}