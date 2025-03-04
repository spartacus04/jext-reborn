package me.spartacus04.jext.integrations

import org.bukkit.block.Block
import org.bukkit.entity.Player

/**
 * The "PermissionsIntegrationManager" class is used to manage the permissions integrations.
 */
class PermissionsIntegrationManager {
    private val permissionIntegrations = ArrayList<PermissionIntegration>()

    private fun registerIntegration(permissionIntegration: PermissionIntegration) {
        if(permissionIntegrations.any { it.id == permissionIntegration.id }) throw IllegalArgumentException("Integration with id ${permissionIntegration.id} is already registered!")

        permissionIntegrations.add(permissionIntegration)
    }

    /**
     * Registers the specified permission integrations.
     * 
     * @param permissionIntegrations The permission integrations to register.
     */
    @Suppress("unused")
    fun registerIntegrations(vararg permissionIntegrations: PermissionIntegration) {
        permissionIntegrations.forEach { registerIntegration(it) }
    }

    /**
     * Checks if the player has access to the jukebox.
     * 
     * @param player The player to check.
     * @param block The block to check.
     * 
     * @return Returns true if the player has access to the jukebox, otherwise false.
     */
    fun hasJukeboxAccess(player: Player, block: Block): Boolean = permissionIntegrations.all { it.hasJukeboxAccess(player, block) }

    /**
     * Checks if the player has access to the jukebox GUI.
     * 
     * @param player The player to check.
     * @param block The block to check.
     * 
     * @return Returns true if the player has access to the jukebox GUI, otherwise false.
     */
    fun hasJukeboxGuiAccess(player: Player, block: Block): Boolean = permissionIntegrations.all { it.hasJukeboxGuiAccess(player, block) }


    /**
     * Reloads the default integrations.
     */
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
