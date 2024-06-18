package me.spartacus04.jext.listeners

import me.spartacus04.jext.JextState.CONFIG
import me.spartacus04.jext.JextState.DISCS
import me.spartacus04.jext.JextState.INTEGRATIONS
import me.spartacus04.jext.config.fields.FieldJukeboxBehaviour
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.gui.JukeboxGui
import me.spartacus04.jext.listeners.utils.JextListener
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.Jukebox
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent

internal class JukeboxClickEvent : JextListener() {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    fun onJukeboxInteract(event: PlayerInteractEvent) {
        val block = event.clickedBlock ?: return
        
        if (event.action != Action.RIGHT_CLICK_BLOCK || block.type != Material.JUKEBOX) return
        if (event.player.isSneaking) return

        if(!event.player.hasPermission("jext.usejukebox")){
            event.isCancelled = true
            return
        }

        when(CONFIG.JUKEBOX_BEHAVIOUR) {
            FieldJukeboxBehaviour.VANILLA -> defaultBehaviour(event, block)
            else -> jukeboxGui(event, block)
        }
    }

    private fun defaultBehaviour(event: PlayerInteractEvent, block: Block) {
        if(!INTEGRATIONS.hasJukeboxAccess(event.player, block)) {
            event.isCancelled = true
            return
        }
        
        val state = block.state as? Jukebox ?: return
        val location = block.location

        if(state.record.type == Material.AIR) {
            val disc = event.item ?: return
            if(!disc.type.isRecord) return


            Disc.fromItemstack(disc)?.play(location)
        }
        else {
            Disc.fromItemstack(state.record)?.namespace?.let {
                DISCS.stop(location, it)
            }
        }
    }

    private fun jukeboxGui(event: PlayerInteractEvent, block: Block) {
        event.isCancelled = true
        if(!INTEGRATIONS.hasJukeboxGuiAccess(event.player, block)) {
            return
        }
        JukeboxGui(event.player, block)
    }

    @EventHandler(ignoreCancelled = true)
    fun onJukeboxBreak(event: BlockBreakEvent) {
        val loc = event.block.location

        JukeboxGui.destroyJukebox(loc).forEach {
            loc.world!!.dropItemNaturally(loc, it)
        }

        val block = event.block
        val state = block.state as? Jukebox ?: return

        Disc.fromItemstack(state.record)?.namespace?.let {
            DISCS.stop(loc, it)
        }
    }
}
