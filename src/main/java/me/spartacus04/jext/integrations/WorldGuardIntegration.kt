package me.spartacus04.jext.integrations

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import com.sk89q.worldguard.protection.flags.Flags
import com.sk89q.worldguard.protection.flags.StateFlag
import org.bukkit.block.Block
import org.bukkit.entity.Player

class WorldGuardIntegration {

    init {
        Flags.CHEST_ACCESS
    }

    /**
     * The function checks if a player can interact with a jukebox GUI.
     *
     * @param player The player parameter represents the player who is trying to interact with the jukebox GUI. It could be
     * the local player or any other player in the game.
     * @param block The "block" parameter represents the block that the player is interacting with. In this case, it is
     * assumed to be a jukebox block.
     */
    fun canInteractWithJukeboxGui(player: Player, block: Block) : Boolean = canInteract(player, block, Flags.CHEST_ACCESS)

    /**
     * The function checks if a player can interact with a jukebox block.
     *
     * @param player The player parameter represents the player who wants to interact with the jukebox. It could be an
     * instance of a Player class or any other data structure that represents a player in your game or application.
     * @param block The "block" parameter represents the jukebox block that the player is trying to interact with.
     */
    fun canInteractWithJukebox(player: Player, block: Block) : Boolean = canInteract(player, block, Flags.INTERACT)

    /**
     * The function checks if a player can interact with a block based on WorldGuard permissions.
     *
     * @param player The player parameter represents the player who is trying to interact with the block.
     * @param block The "block" parameter represents the block that the player is trying to interact with.
     * @param flag The `flag` parameter represents a specific state flag that you want to test for. State flags in
     * WorldGuard define various properties or conditions that can be associated with a block or region. Examples of state
     * flags include `build`, `interact`, `use`, `pvp`, etc.
     * @return a boolean value.
     */
    private fun canInteract(player: Player, block: Block, flag: StateFlag) : Boolean {
        val wgPlayer = WorldGuardPlugin.inst().wrapPlayer(player)

        if(WorldGuard.getInstance().platform.sessionManager.hasBypass(wgPlayer, wgPlayer.world)) return true

        val containerQuery = WorldGuard.getInstance().platform.regionContainer.createQuery()

        return containerQuery.testState(BukkitAdapter.adapt(block.location), wgPlayer, flag)
    }
}