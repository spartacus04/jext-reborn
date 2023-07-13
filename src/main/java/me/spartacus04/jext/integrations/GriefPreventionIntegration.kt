package me.spartacus04.jext.integrations

import me.ryanhamshire.GriefPrevention.ClaimPermission
import me.ryanhamshire.GriefPrevention.GriefPrevention
import org.bukkit.block.Block
import org.bukkit.entity.Player

class GriefPreventionIntegration {
    init {
        ClaimPermission.Access
    }
    /**
     * The function checks if a player can interact with a jukebox GUI.
     *
     * @param player The player parameter represents the player who is trying to interact with the jukebox GUI. It could be
     * any player object in the game.
     * @param block The "block" parameter represents the jukebox block that the player is trying to interact with.
     */
    fun canInteractWithJukeboxGui(player: Player, block: Block) : Boolean = canInteract(player, block, ClaimPermission.Inventory)

    /**
     * The function checks if a player can interact with a jukebox block.
     *
     * @param player The player parameter represents the player who wants to interact with the jukebox. It could be an
     * instance of a Player class or any other data structure that represents a player in your code.
     * @param block The "block" parameter represents the jukebox block that the player is trying to interact with.
     */
    fun canInteractWithJukebox(player: Player, block: Block) : Boolean = canInteract(player, block, ClaimPermission.Access)

    /**
     * The function checks if a player can interact with a block based on the claim permission.
     *
     * @param player The "player" parameter represents the player who is trying to interact with the block.
     * @param block The "block" parameter represents the block that the player is trying to interact with. It could be any
     * block in the game world.
     * @param permission The `permission` parameter is of type `ClaimPermission` and represents the permission level that
     * the player needs to have in order to interact with the block.
     * @return a boolean value.
     */
    private fun canInteract(player: Player, block: Block, permission: ClaimPermission) : Boolean {
        val claim = GriefPrevention.instance.dataStore.getClaimAt(block.location, false, null)

        return claim.checkPermission(player, permission, null) == null
    }
}