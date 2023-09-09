package me.spartacus04.jext.listener

import me.spartacus04.jext.config.ConfigData.Companion.CONFIG
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscPlayer
import me.spartacus04.jext.integrations.IntegrationsRegistrant
import me.spartacus04.jext.jukebox.JukeboxContainer
import me.spartacus04.jext.jukebox.legacy.LegacyJukeboxContainer
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.Jukebox
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin

/**
 * The class `JukeboxEventListener` is a listener class that is used to handle player interactions with jukeboxes.
 *
 * @property plugin the plugin instance
 * @constructor Creates a new jukebox event listener.
 */
internal class JukeboxEventListener(private val plugin: JavaPlugin) : Listener {
    /**
     * The function `onJukeboxInteract` is an event listener that is called when a player interacts with a jukebox.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    fun onJukeboxInteract(event: PlayerInteractEvent) {
        val block = event.clickedBlock ?: return

        if (event.action != Action.RIGHT_CLICK_BLOCK || block.type != Material.JUKEBOX) return
        if (event.player.isSneaking) return

        when(CONFIG.JUKEBOX_BEHAVIOUR) {
            "legacy-gui" -> legacyJukeboxGui(event, block)
            "gui" -> jukeboxGui(event, block)
            else -> defaultBehaviour(event, block)
        }
    }

    /**
     * The function `defaultBehaviour` handles player interactions with jukeboxes, allowing them to play or stop music
     * discs depending on the state of the jukebox.
     *
     * @param event PlayerInteractEvent - an event that is triggered when a player interacts with an object in the game
     * world.
     * @param block The "block" parameter is the block that the player interacted with. It is of type Block.
     */
    private fun defaultBehaviour(event: PlayerInteractEvent, block: Block) {
        if(!IntegrationsRegistrant.hasJukeboxAccess(event.player, block)) {
            event.isCancelled = true
            return
        }

        val state = block.state as? Jukebox ?: return
        val location = block.location

        if(state.record.type == Material.AIR) {
            try {
                val disc = event.item ?: return
                if(!disc.type.isRecord) return
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

    /**
     * The function `legacyJukeboxGui` cancels the `PlayerInteractEvent`, checks if the player has access to the jukebox
     * GUI, and opens the LegacyJukeboxContainer for the player.
     *
     * @param event The event parameter is of type PlayerInteractEvent. This event is triggered when a player interacts
     * with an object in the game, such as right-clicking on a block.
     * @param block The "block" parameter represents the block that the player interacted with.
     */
    private fun legacyJukeboxGui(event: PlayerInteractEvent, block: Block) {
        event.isCancelled = true

        if(!IntegrationsRegistrant.hasJukeboxGuiAccess(event.player, block)) return

        LegacyJukeboxContainer.get(plugin, block.location).open(event.player)
    }

    /**
     * The function `jukeboxGui` cancels the `PlayerInteractEvent`, checks if the player has access to the Jukebox GUI, and
     * opens the JukeboxContainer for the player.
     *
     * @param event The event parameter is of type PlayerInteractEvent. This event is triggered when a player interacts
     * with an object in the game, such as right-clicking on a block.
     * @param block The "block" parameter represents the block that the player interacted with. It is used to determine if
     * the player has access to the jukebox GUI.
     */
    private fun jukeboxGui(event: PlayerInteractEvent, block: Block) {
        event.isCancelled = true

        if(!IntegrationsRegistrant.hasJukeboxGuiAccess(event.player, block)) return

        JukeboxContainer(event.player, block)
    }

    /**
     * The function `onJukeboxBreak` is an event listener that is called when a player breaks a jukebox.
     *
     * @param event The event parameter is of type BlockBreakEvent. This event is triggered when a player breaks a block.
     */
    @EventHandler(ignoreCancelled = true)
    fun onJukeboxBreak(event: BlockBreakEvent) {
        val loc = event.block.location

        if(CONFIG.JUKEBOX_BEHAVIOUR == "legacy-gui") {
            LegacyJukeboxContainer.get(plugin, loc).breakJukebox()
        } else {
            JukeboxContainer.destroyJukebox(loc).forEach {
                loc.world!!.dropItemNaturally(loc, it)
            }
        }



        val block = event.block
        val state = block.state as? Jukebox ?: return

        try {
            val disc = state.record
            val discContainer = DiscContainer(disc)

            DiscPlayer.stop(loc, discContainer.namespace)
        } catch (_: IllegalStateException) { }
    }
}
