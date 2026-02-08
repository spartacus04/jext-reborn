package me.spartacus04.jext.listeners

import me.spartacus04.colosseum.listeners.ColosseumListener
import me.spartacus04.colosseum.utils.version.VersionCompatibilityMin
import me.spartacus04.jext.Jext
import me.spartacus04.jext.config.fields.FieldJukeboxBehaviour
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.language.DefaultMessages.VULNERABLE_MESSAGE
import org.bukkit.block.Jukebox
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryType

@VersionCompatibilityMin("1.19.4")
internal class InventoryMoveItemEvent(val plugin: Jext) : ColosseumListener(plugin) {
    override fun register() {
        try {
            InventoryType.JUKEBOX
            super.register()
        } catch (_: NoSuchFieldError) {
            plugin.colosseumLogger.warn(VULNERABLE_MESSAGE)
            throw Error(VULNERABLE_MESSAGE)
        }
    }

    @EventHandler
    fun inventoryMoveItemEvent(e: InventoryMoveItemEvent) {
        if(e.isCancelled) return

        when(plugin.config.JUKEBOX_BEHAVIOUR) {
            FieldJukeboxBehaviour.VANILLA -> {
                if(e.destination.type == InventoryType.JUKEBOX) {
                    val jukebox = e.destination.location!!.block.state as Jukebox
                    if (jukebox.isPlaying) return

                    Disc.fromItemstack(e.item)?.play(e.destination.location!!)
                } else if(e.source.type == InventoryType.JUKEBOX) {
                    val disc = Disc.fromItemstack(e.item) ?: return
                    plugin.discs.stop(e.source.location!!, disc.namespace)
                }
            }
            else -> {
                if(e.source.type == InventoryType.PLAYER && e.destination.type == InventoryType.JUKEBOX) {
                    e.isCancelled = true
                }
            }
        }
    }
}

