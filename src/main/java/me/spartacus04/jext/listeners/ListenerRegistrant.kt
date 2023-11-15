package me.spartacus04.jext.listeners

internal object ListenerRegistrant {
    fun registerListeners() {

        RecordPacketEvent().register()

        ChestOpenEvent().register()
        CreeperDeathEvent().register()
        DiscUpdateEvent().register()
        JukeboxClickEvent().register()
        PlayerJoinEvent().register()
        ResourceStatusEvent().register()

        PrepareCraftingEvent().register()

        InventoryMoveItemEvent().register()

        BlockBrushEvent().register()
    }
}