package me.spartacus04.jext.integrations

import me.ryanhamshire.GriefPrevention.ClaimPermission
import me.ryanhamshire.GriefPrevention.GriefPrevention
import org.bukkit.block.Block
import org.bukkit.entity.Player

internal class GriefPreventionIntegration : Integration {
    override val id = "griefprevention"

    init {
        ClaimPermission.Access
    }

    override fun hasJukeboxAccess(player: Player, block: Block): Boolean = canInteract(player, block, ClaimPermission.Inventory)

    override fun hasJukeboxGuiAccess(player: Player, block: Block): Boolean = canInteract(player, block, ClaimPermission.Access)

    private fun canInteract(player: Player, block: Block, permission: ClaimPermission) : Boolean {
        return try {
            val claim = GriefPrevention.instance.dataStore.getClaimAt(block.location, false, null)

            claim.checkPermission(player, permission, null) == null
        } catch (e: NullPointerException) {
            true
        }
    }
}