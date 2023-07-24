package me.spartacus04.jext.integrations

import me.ryanhamshire.GriefPrevention.ClaimPermission
import me.ryanhamshire.GriefPrevention.GriefPrevention
import org.bukkit.block.Block
import org.bukkit.entity.Player
import java.lang.NullPointerException

/**
 * This class is used to integrate with the GriefPrevention plugin.
 *
 * @constructor Creates a new GriefPreventionIntegration object
 */
internal class GriefPreventionIntegration {
    init {
        ClaimPermission.Access
    }
    /**
     * The function checks if a player can interact with a jukebox GUI.
     *
     * @param player The player parameter represents the player who is trying to interact with the jukebox GUI.
     * @param block The "block" parameter represents the jukebox block that the player is trying to interact with.
     */
    fun canInteractWithJukeboxGui(player: Player, block: Block) : Boolean = canInteract(player, block, ClaimPermission.Inventory)

    /**
     * The function checks if a player can interact with a jukebox block.
     *
     * @param player The player parameter represents the player who wants to interact with the jukebox.
     * @param block The "block" parameter represents the jukebox block that the player is trying to interact with.
     */
    fun canInteractWithJukebox(player: Player, block: Block) : Boolean = canInteract(player, block, ClaimPermission.Access)

    /**
     * The function checks if a player can interact with a block based on the claim permission.
     *
     * @param player The "player" parameter represents the player who is trying to interact with the block.
     * @param block The "block" parameter represents the block that the player is trying to interact with.
     * @param permission The `permission` parameter is of type `ClaimPermission` and represents the permission level that
     * the player needs to have in order to interact with the block.
     * @return a boolean value.
     */
    private fun canInteract(player: Player, block: Block, permission: ClaimPermission) : Boolean {
        return try {
            val claim = GriefPrevention.instance.dataStore.getClaimAt(block.location, false, null)

            claim.checkPermission(player, permission, null) == null
        } catch (e: NullPointerException) {
            true
        }
    }
}