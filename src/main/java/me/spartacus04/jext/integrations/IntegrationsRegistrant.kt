package me.spartacus04.jext.integrations

import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class IntegrationsRegistrant {
    companion object {
        private var WORLDGUARD: WorldGuardIntegration? = null
        private var GRIEF_PREVENTION: GriefPreventionIntegration? = null

        fun registerIntegrations() {
            try {
                WORLDGUARD = WorldGuardIntegration()
            } catch (_: NoClassDefFoundError) { }

            try {
                GRIEF_PREVENTION = GriefPreventionIntegration()
            } catch (_: NoClassDefFoundError) { }
        }

        fun hasJukeboxAccess(player: Player, block: Block) =
            (WORLDGUARD == null || WORLDGUARD!!.canInteractWithJukebox(player, block)) &&
            (GRIEF_PREVENTION == null || GRIEF_PREVENTION!!.canInteractWithJukebox(player, block))

        fun hasJukeboxGuiAccess(player: Player, block: Block) =
            (WORLDGUARD == null || WORLDGUARD!!.canInteractWithJukeboxGui(player, block)) &&
            (GRIEF_PREVENTION == null || GRIEF_PREVENTION!!.canInteractWithJukeboxGui(player, block))
    }
}