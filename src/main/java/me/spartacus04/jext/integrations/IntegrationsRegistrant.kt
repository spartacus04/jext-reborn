package me.spartacus04.jext.integrations

import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * This class is used to register and manage the plugin's integrations.
 *
 * @constructor The class is a singleton, so the constructor is private.
 */
class IntegrationsRegistrant private constructor() {
    companion object {
        private var WORLDGUARD: WorldGuardIntegration? = null
        private var GRIEF_PREVENTION: GriefPreventionIntegration? = null
        private var GEYSER: GeyserIntegration? = null

        /**
         * The function registers the plugin's integrations.
         */
        internal fun registerIntegrations(plugin: JavaPlugin) {
            try {
                WORLDGUARD = WorldGuardIntegration()
            } catch (_: NoClassDefFoundError) { }

            try {
                GRIEF_PREVENTION = GriefPreventionIntegration()
            } catch (_: NoClassDefFoundError) { }

            try {
                if(GEYSER == null) {
                    GEYSER = GeyserIntegration(plugin)
                }
            } catch (_: NoClassDefFoundError) { }
        }

        /**
         * The function checks if a player has access to interact with a jukebox, taking into account WorldGuard and
         * GriefPrevention plugins.
         *
         * @param player The player parameter represents the player who is trying to access the jukebox.
         * @param block The "block" parameter represents the jukebox block that the player is trying to interact with.
         */
        fun hasJukeboxAccess(player: Player, block: Block): Boolean {
            if(WORLDGUARD?.canInteractWithJukebox(player, block) == false) return false
            return GRIEF_PREVENTION?.canInteractWithJukebox(player, block) != false
        }

        /**
         * The function checks if a player has access to interact with a jukebox GUI, taking into account WorldGuard and
         * GriefPrevention plugins.
         *
         * @param player The player parameter represents the player who is trying to access the jukebox GUI.
         * @param block The "block" parameter represents the jukebox block that the player is trying to interact with.
         */
        fun hasJukeboxGuiAccess(player: Player, block: Block): Boolean {
            if(WORLDGUARD?.canInteractWithJukeboxGui(player, block) == false) return false
            return GRIEF_PREVENTION?.canInteractWithJukeboxGui(player, block) != false
        }

        fun isBedrockPlayer(player: Player) : Boolean {
            return GEYSER?.isBedrockPlayer(player) != false
        }
    }
}