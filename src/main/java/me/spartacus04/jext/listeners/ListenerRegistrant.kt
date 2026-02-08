package me.spartacus04.jext.listeners

import me.spartacus04.jext.Jext

internal object ListenerRegistrant {
    fun registerListeners(plugin: Jext) {
        val listeners = listOf(
            RecordPacketEvent(plugin),
            ChestOpenEvent(plugin),
            CreeperDeathEvent(plugin),
            DiscUpdateEvent(plugin),
            JukeboxClickEvent(plugin),
            PlayerJoinEvent(plugin),
            ResourceStatusEvent(plugin),
            PrepareCraftingEvent(plugin),
            InventoryMoveItemEvent(plugin),
            BlockBrushEvent(plugin),
            DecoratedPotEvent(plugin),
            CrafterCraftDiscEvent(plugin),
            VaultDispenseEvent(plugin),
            TrialSpawnerDispenseEvent(plugin)
        )

        plugin.colosseumLogger.debug("Registering listeners...")

        val registered = listeners.fold(0) { acc, listener ->
            return@fold try {
                listener.register()
                plugin.colosseumLogger.debug("Registered listener: ${listener::class.simpleName}")
                acc + 1
            } catch (_: Exception) {
                plugin.colosseumLogger.error("Failed to register listener: ${listener::class.simpleName}")
                acc
            }
        }

        plugin.colosseumLogger.debug("Registered $registered/${listeners.size} listeners.")
    }
}