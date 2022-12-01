package me.spartacus04.jext.listener

import me.spartacus04.jext.config.ConfigData.Companion.CONFIG
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscPlayer
import me.spartacus04.jext.disc.JukeboxContainer
import org.bukkit.Material
import org.bukkit.block.Jukebox
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin

internal class JukeboxEventListener() : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onJukeboxInteract(event: PlayerInteractEvent) {
        if(!CONFIG.DISC_HOLDER_BEHAVIOUR) return discholderBehaviour(event)
        jukeboxBehaviour(event)
    }

    private fun jukeboxBehaviour(event: PlayerInteractEvent) {
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

    private fun discholderBehaviour(event: PlayerInteractEvent) {
        val block = event.clickedBlock ?: return

        if (event.action != Action.RIGHT_CLICK_BLOCK || block.type != Material.JUKEBOX) return

        if (block.state !is Jukebox) return

        JukeboxContainer(block.location, event.player)
        //TODO: implement jukeboxholder behaviour
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