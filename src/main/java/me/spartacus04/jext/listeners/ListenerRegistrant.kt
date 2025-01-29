package me.spartacus04.jext.listeners

internal object ListenerRegistrant {
    private val listeners = listOf(
        RecordPacketEvent(),
        ChestOpenEvent(),
        CreeperDeathEvent(),
        DiscUpdateEvent(),
        JukeboxClickEvent(),
        PlayerJoinEvent(),
        ResourceStatusEvent(),
        PrepareCraftingEvent(),
        InventoryMoveItemEvent(),
        BlockBrushEvent(),
        DecoratedPotEvent(),
        CrafterCraftDiscEvent(),
        VaultDispenseEvent(),
        TrialSpawnerDispenseEvent(),
    )

    fun registerListeners() =
        listeners.forEach { it.register() }
}