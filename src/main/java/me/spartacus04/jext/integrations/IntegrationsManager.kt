package me.spartacus04.jext.integrations

import me.spartacus04.jext.State
import me.spartacus04.jext.integrations.unique.GeyserIntegration
import org.bukkit.block.Block
import org.bukkit.entity.Player

class IntegrationsManager {
    private val integrations = ArrayList<Integration>()

    private fun registerIntegration(integration: Integration) {
        if(integrations.any { it.id == integration.id }) throw IllegalArgumentException("Integration with id ${integration.id} is already registered!")

        integrations.add(integration)
    }

    fun registerIntegrations(vararg integrations: Integration) {
        integrations.forEach { registerIntegration(it) }
    }

    fun hasJukeboxAccess(player: Player, block: Block): Boolean = integrations.all { it.hasJukeboxAccess(player, block) }

    fun hasJukeboxGuiAccess(player: Player, block: Block): Boolean = integrations.all { it.hasJukeboxGuiAccess(player, block) }

    fun reloadDefaultIntegrations() {
        // remove "worldguard" and "griefprevention"
        integrations.removeIf { it.id == "worldguard" || it.id == "griefprevention" }

        try {
            registerIntegration(WorldGuardIntegration())
        } catch(_: NoClassDefFoundError) { }

        try {
            registerIntegration(GriefPreventionIntegration())
        } catch (_: NoClassDefFoundError) { }

        try {
            if(GeyserIntegration.GEYSER == null) {
                GeyserIntegration.GEYSER = GeyserIntegration()
            }
        } catch (_ : NoClassDefFoundError) { }
    }
}