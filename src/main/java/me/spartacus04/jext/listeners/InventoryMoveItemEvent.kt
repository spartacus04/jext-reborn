package me.spartacus04.jext.listeners

import me.spartacus04.jext.State.CONFIG
import me.spartacus04.jext.config.fields.FieldJukeboxBehaviour
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.language.LanguageManager.Companion.VULNERABLE_MESSAGE
import me.spartacus04.jext.listeners.utils.JextListener
import org.bukkit.Bukkit
import org.bukkit.block.Jukebox
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.InventoryMoveItemEvent

internal class InventoryMoveItemEvent : JextListener("1.19.4") {
    override fun register() {
        try {
            InventoryType.JUKEBOX
            super.register()
        } catch (e: NoSuchFieldError) {
            Bukkit.getConsoleSender().sendMessage(VULNERABLE_MESSAGE)
        }
    }

    @EventHandler
    fun inventoryMoveItemEvent(e: InventoryMoveItemEvent) {
        if(e.isCancelled) return

        when(CONFIG.JUKEBOX_BEHAVIOUR) {
            FieldJukeboxBehaviour.VANILLA -> {
                if(e.destination.type == InventoryType.JUKEBOX) {
                    val jukebox = e.destination.location!!.block.state as Jukebox
                    if (jukebox.isPlaying) return

                    Disc.fromItemstack(e.item)?.play(e.destination.location!!)
                } else if(e.source.type == InventoryType.JUKEBOX) {
                    val disc = Disc.fromItemstack(e.item) ?: return
                    Disc.stop(e.source.location!!, disc.namespace)
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

