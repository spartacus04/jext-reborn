package me.spartacus04.jext.listeners

import me.spartacus04.jext.JextState.LANG
import me.spartacus04.jext.language.LanguageManager.Companion.FAILED_TO_REGISTER_LISTENER
import me.spartacus04.jext.utils.listOfCatching
import org.bukkit.Bukkit

internal object ListenerRegistrant {
    private val listeners = listOfCatching(
        { RecordPacketEvent() },
        { ChestOpenEvent() },
        { CreeperDeathEvent() },
        { DiscUpdateEvent() },
        { JukeboxClickEvent() },
        { PlayerJoinEvent() },
        { ResourceStatusEvent() },
        { PrepareCraftingEvent() },
        { InventoryMoveItemEvent() },
        { BlockBrushEvent() },
        { DecoratedPotEvent() },
        { CrafterCraftDiscEvent() },
        { VaultDispenseEvent() },
        { TrialSpawnerDispenseEvent() }
    ) {
        Bukkit.getConsoleSender().sendMessage(
            LANG.replaceParameters(
                FAILED_TO_REGISTER_LISTENER,
                hashMapOf(
                    "name" to it.javaClass.simpleName,
                    "error" to it.stackTraceToString()
                )
            )
        )
    }

    fun registerListeners() {
        listeners.forEach {
            try {
                it.register()
            } catch (e: Exception) {
                Bukkit.getConsoleSender().sendMessage(
                    LANG.replaceParameters(
                        FAILED_TO_REGISTER_LISTENER,
                        hashMapOf(
                            "name" to it.javaClass.simpleName,
                            "error" to e.message.toString()
                        )
                    )
                )
            }
        }
    }
}