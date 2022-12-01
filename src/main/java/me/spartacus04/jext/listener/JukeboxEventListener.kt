package me.spartacus04.jext.listener

import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscPlayer
import org.bukkit.Material
import org.bukkit.block.Jukebox
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent

internal class JukeboxEventListener : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onJukeboxInteract(event: PlayerInteractEvent) {
        val block = event.clickedBlock ?: return

        if (event.action != Action.RIGHT_CLICK_BLOCK || block.type != Material.JUKEBOX) return

        val state = block.state as? Jukebox ?: return
        val location = block.location

        if(state.record.type == Material.AIR) {
            try {
                val disc = event.item ?: return

                val discContainer = DiscContainer(disc)

                val discPlayer = DiscPlayer(discContainer)

                discPlayer.play(location)
            } catch (_: IllegalStateException) { }
        }
        else {
            try {
                val disc = state.record

                val discContainer = DiscContainer(disc)

                val discPlayer = DiscPlayer(discContainer)

                discPlayer.stop(location)
            } catch (_: IllegalStateException) { }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onJukeboxBreak(event: BlockBreakEvent) {
        val block = event.block
        val state = block.state as? Jukebox ?: return

        try {
            val disc = state.record

            val discContainer = DiscContainer(disc)

            val discPlayer = DiscPlayer(discContainer)

            discPlayer.stop(block.location)
        } catch (_: IllegalStateException) { }
    }
}