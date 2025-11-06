package me.spartacus04.jext.listeners

import me.spartacus04.colosseum.ColosseumPlugin

internal object ListenerRegistrant {
    private val listeners = listOf(
        RecordPacketEvent::class.java,
        ChestOpenEvent::class.java,
        CreeperDeathEvent::class.java,
        DiscUpdateEvent::class.java,
        JukeboxClickEvent::class.java,
        PlayerJoinEvent::class.java,
        ResourceStatusEvent::class.java,
        PrepareCraftingEvent::class.java,
        InventoryMoveItemEvent::class.java,
        BlockBrushEvent::class.java,
        DecoratedPotEvent::class.java,
        CrafterCraftDiscEvent::class.java,
        VaultDispenseEvent::class.java,
        TrialSpawnerDispenseEvent::class.java
    )

    fun registerListeners(plugin: ColosseumPlugin) {
        plugin.colosseumLogger.debug("Registering listeners...")

        listeners.forEach { listener ->
            listener.constructors.first { it.parameters.size == 1 }.newInstance(plugin)
        }

        plugin.colosseumLogger.debug("Registered ${listeners.size} listeners.")
    }
}