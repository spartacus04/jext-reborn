package me.spartacus04.jext.listener

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import com.sk89q.worldguard.protection.flags.Flags
import com.sk89q.worldguard.protection.flags.StateFlag
import me.spartacus04.jext.config.ConfigData.Companion.CONFIG
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscPlayer
import me.spartacus04.jext.jukebox.JukeboxContainer
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.Jukebox
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin

internal class JukeboxEventListener(private val plugin: JavaPlugin) : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onJukeboxInteract(event: PlayerInteractEvent) {
        val block = event.clickedBlock ?: return

        if (event.action != Action.RIGHT_CLICK_BLOCK || block.type != Material.JUKEBOX) return

        if(CONFIG.JUKEBOX_GUI) jukeboxGui(event, block)
        else defaultBehaviour(event, block)
    }

    private fun defaultBehaviour(event: PlayerInteractEvent, block: Block) {
        if(!canInteract(event.player, block, Flags.INTERACT)) return

        val state = block.state as? Jukebox ?: return
        val location = block.location

        if(state.record.type == Material.AIR) {
            try {
                val disc = event.item ?: return
                val discContainer = DiscContainer(disc)

                discContainer.play(location)
            } catch (_: IllegalStateException) { }
        }
        else {
            try {
                val disc = state.record
                val discContainer = DiscContainer(disc)

                DiscPlayer.stop(location, discContainer.namespace)
            } catch (_: IllegalStateException) { }
        }
    }

    private fun jukeboxGui(event: PlayerInteractEvent, block: Block) {
        event.isCancelled = true

        if(!canInteract(event.player, block, Flags.CHEST_ACCESS)) return

        JukeboxContainer.get(plugin, block.location).open(event.player)
    }

    @EventHandler(ignoreCancelled = true)
    fun onJukeboxBreak(event: BlockBreakEvent) {
        val loc = event.block.location

        JukeboxContainer.get(plugin, loc).breakJukebox()

        val block = event.block
        val state = block.state as? Jukebox ?: return

        try {
            val disc = state.record
            val discContainer = DiscContainer(disc)

            DiscPlayer.stop(loc, discContainer.namespace)
        } catch (_: IllegalStateException) { }
    }

    private fun canInteract(player: Player, block: Block, flag: StateFlag) : Boolean {
        if(Bukkit.getServer().pluginManager.getPlugin("WorldGuard") == null) return true

        val wgPlayer = WorldGuardPlugin.inst().wrapPlayer(player)

        if(WorldGuard.getInstance().platform.sessionManager.hasBypass(wgPlayer, wgPlayer.world)) return true

        val containerQuery = WorldGuard.getInstance().platform.regionContainer.createQuery()

        return containerQuery.testState(BukkitAdapter.adapt(block.location), wgPlayer, flag)
    }
}