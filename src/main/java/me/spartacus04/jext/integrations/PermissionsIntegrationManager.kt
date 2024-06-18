package me.spartacus04.jext.integrations

import org.bukkit.block.Block
import org.bukkit.entity.Player

class PermissionsIntegrationManager {
    private val permissionIntegrations = ArrayList<PermissionIntegration>()

    private fun registerIntegration(permissionIntegration: PermissionIntegration) {
        if(permissionIntegrations.any { it.id == permissionIntegration.id }) throw IllegalArgumentException("Integration with id ${permissionIntegration.id} is already registered!")

        permissionIntegrations.add(permissionIntegration)
    }

    fun registerIntegrations(vararg permissionIntegrations: PermissionIntegration) {
        permissionIntegrations.forEach { registerIntegration(it) }
    }

    fun hasJukeboxAccess(player: Player, block: Block): Boolean = permissionIntegrations.all { it.hasJukeboxAccess(player, block) }

    fun hasJukeboxGuiAccess(player: Player, block: Block): Boolean = permissionIntegrations.all { it.hasJukeboxGuiAccess(player, block) }

    fun reloadDefaultIntegrations() {
        permissionIntegrations.removeIf { it.id == "worldguard" || it.id == "griefprevention" }

        try {
            registerIntegration(WorldGuardPermissionIntegration())
        } catch(_: NoClassDefFoundError) { }

        try {
            registerIntegration(GriefPreventionPermissionIntegration())
        } catch (_: NoClassDefFoundError) { }
    }
}
