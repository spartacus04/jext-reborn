package me.spartacus04.jext.integrations

import org.bukkit.block.Block
import org.bukkit.entity.Player

class IntegrationsRegistrant {
    companion object {
        private var WORLDGUARD: WorldGuardIntegration? = null
        private var GRIEF_PREVENTION: GriefPreventionIntegration? = null

        /**
         * The function "registerIntegrations" attempts to initialize two integration objects, "WORLDGUARD" and
         * "GRIEF_PREVENTION", catching any "NoClassDefFoundError" exceptions that may occur.
         */
        fun registerIntegrations() {
            try {
                WORLDGUARD = WorldGuardIntegration()
            } catch (_: NoClassDefFoundError) { }

            try {
                GRIEF_PREVENTION = GriefPreventionIntegration()
            } catch (_: NoClassDefFoundError) { }
        }

        /**
         * The function checks if a player has access to interact with a jukebox, taking into account WorldGuard and
         * GriefPrevention plugins.
         *
         * @param player The player parameter represents the player who is trying to access the jukebox.
         * @param block The "block" parameter represents the jukebox block that the player is trying to interact with.
         */
        fun hasJukeboxAccess(player: Player, block: Block) =
            (WORLDGUARD == null || WORLDGUARD!!.canInteractWithJukebox(player, block)) &&
            (GRIEF_PREVENTION == null || GRIEF_PREVENTION!!.canInteractWithJukebox(player, block))

        /**
         * The function checks if a player has access to interact with a jukebox GUI, taking into account WorldGuard and
         * GriefPrevention plugins.
         *
         * @param player The player parameter represents the player who is trying to access the jukebox GUI.
         * @param block The "block" parameter represents the jukebox block that the player is trying to interact with.
         */
        fun hasJukeboxGuiAccess(player: Player, block: Block) =
            (WORLDGUARD == null || WORLDGUARD!!.canInteractWithJukeboxGui(player, block)) &&
            (GRIEF_PREVENTION == null || GRIEF_PREVENTION!!.canInteractWithJukeboxGui(player, block))
    }
}