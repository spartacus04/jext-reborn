package me.spartacus04.jext.integrations

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import com.sk89q.worldguard.protection.flags.Flags
import com.sk89q.worldguard.protection.flags.StateFlag
import org.bukkit.block.Block
import org.bukkit.entity.Player

internal class WorldGuardIntegration : Integration {
    override val id = "worldguard"

    init {
        Flags.CHEST_ACCESS
    }

    override fun hasJukeboxAccess(player: Player, block: Block): Boolean = canInteract(player, block, Flags.CHEST_ACCESS)

    override fun hasJukeboxGuiAccess(player: Player, block: Block): Boolean = canInteract(player, block, Flags.BUILD, Flags.INTERACT, Flags.USE)

    private fun canInteract(player: Player, block: Block, vararg flags: StateFlag) : Boolean {
        val wgPlayer = WorldGuardPlugin.inst().wrapPlayer(player)

        if(WorldGuard.getInstance().platform.sessionManager.hasBypass(wgPlayer, wgPlayer.world)) return true

        val containerQuery = WorldGuard.getInstance().platform.regionContainer.createQuery()

        return flags.any {
            containerQuery.testState(BukkitAdapter.adapt(block.location), wgPlayer, it)
        }
    }
}