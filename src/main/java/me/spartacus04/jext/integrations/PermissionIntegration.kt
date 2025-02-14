package me.spartacus04.jext.integrations

import org.bukkit.block.Block
import org.bukkit.entity.Player

/**
 * The "PermissionIntegration" interface represents an integration with another plugin.
 * It is used to check if a player can interact with a jukebox GUI or a jukebox block.
 */
interface PermissionIntegration {
    /**
     * The "id" property represents the id of the integration. It must be unique across all integrations.
     */
    val id: String
    /**
     * The function checks if a player can interact with a jukebox GUI.
     *
     * @param player The player parameter represents the player who is trying to interact with the jukebox GUI.
     * @param block The "block" parameter represents the jukebox block that the player is trying to interact with.
     * 
     * @return Returns true if the player can interact with the jukebox GUI, otherwise false.
     */
    fun hasJukeboxGuiAccess(player: Player, block: Block): Boolean
    /**
     * The function checks if a player can interact with a jukebox block.
     *
     * @param player The player parameter represents the player who wants to interact with the jukebox.
     * @param block The "block" parameter represents the jukebox block that the player is trying to interact with.
     * 
     * @return Returns true if the player can interact with the jukebox block, otherwise false.
     */
    fun hasJukeboxAccess(player: Player, block: Block): Boolean
}
