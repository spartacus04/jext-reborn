package me.spartacus04.jext.listener

import me.spartacus04.jext.config.ConfigData.Companion.CONFIG
import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscPlayer
import me.spartacus04.jext.jukebox.JukeboxContainer
import me.spartacus04.jext.jukebox.JukeboxPersistentDataContainerManager.Companion.isDiscContainer
import me.spartacus04.jext.jukebox.JukeboxPersistentDataContainerManager.Companion.jukeboxContainers
import me.spartacus04.jext.jukebox.JukeboxPersistentDataContainerManager
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.Jukebox
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

internal class JukeboxEventListener(private val plugin: JavaPlugin) : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onJukeboxInteract(event: PlayerInteractEvent) {
        val block = event.clickedBlock ?: return

        if (event.action != Action.RIGHT_CLICK_BLOCK || block.type != Material.JUKEBOX) return

        if(CONFIG.JUKEBOX_GUI) cJukeboxInteract(event, block)
        else vJukeboxInteract(event, block)
    }

    private fun vJukeboxInteract(event: PlayerInteractEvent, block: Block) {
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

    private fun cJukeboxInteract(event: PlayerInteractEvent, block: Block) {
        event.isCancelled = true

        JukeboxContainer(plugin, block.location, event.player)
    }
    @EventHandler(ignoreCancelled = true)
    fun onJukeboxBreak(event: BlockBreakEvent) {
        // get the jukebox persistent data container
        val loc = event.block.location
        if(jukeboxContainers[loc.world!!.name + loc.blockX + loc.blockY + loc.blockZ] != null) {
            // get discs
            val discs = jukeboxContainers[loc.world!!.name + loc.blockX + loc.blockY + loc.blockZ]!!.discs

            discs.forEach {
                if(isDiscContainer(it.value)) {
                    val disc = DISCS.first { dsc -> dsc.DISC_NAMESPACE == it.value}
                    event.block.world.dropItemNaturally(event.block.location, DiscContainer(disc).discItem)
                }
                else {
                    event.block.world.dropItemNaturally(event.block.location, ItemStack(Material.valueOf(it.value)))
                }
            }

            jukeboxContainers[loc.world!!.name + loc.blockX + loc.blockY + loc.blockZ]?.unsubscribedFuncs?.forEach {
                it.value.invoke()
            }

            jukeboxContainers.remove(loc.world!!.name + loc.blockX + loc.blockY + loc.blockZ)
            JukeboxPersistentDataContainerManager.save()
        }

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