package me.spartacus04.jext.integrations

import me.ryanhamshire.GriefPrevention.ClaimPermission
import me.ryanhamshire.GriefPrevention.GriefPrevention
import org.bukkit.block.Block
import org.bukkit.entity.Player

class GriefPreventionIntegration {
    init {
        ClaimPermission.Access
    }
    fun canInteractWithJukeboxGui(player: Player, block: Block) : Boolean = canInteract(player, block, ClaimPermission.Inventory)

    fun canInteractWithJukebox(player: Player, block: Block) : Boolean = canInteract(player, block, ClaimPermission.Access)

    private fun canInteract(player: Player, block: Block, permission: ClaimPermission) : Boolean {
        val claim = GriefPrevention.instance.dataStore.getClaimAt(block.location, false, null)

        return claim.checkPermission(player, permission, null) == null
    }
}