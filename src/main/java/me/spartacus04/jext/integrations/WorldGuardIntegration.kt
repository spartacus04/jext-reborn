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

    fun canInteractWithJukeboxGui(player: Player, block: Block) : Boolean = canInteract(player, block, Flags.CHEST_ACCESS)

    fun canInteractWithJukebox(player: Player, block: Block) : Boolean = canInteract(player, block, Flags.INTERACT)

    private fun canInteract(player: Player, block: Block, flag: StateFlag) : Boolean {
        val wgPlayer = WorldGuardPlugin.inst().wrapPlayer(player)

        if(WorldGuard.getInstance().platform.sessionManager.hasBypass(wgPlayer, wgPlayer.world)) return true

        val containerQuery = WorldGuard.getInstance().platform.regionContainer.createQuery()

        return containerQuery.testState(BukkitAdapter.adapt(block.location), wgPlayer, flag)
    }
}