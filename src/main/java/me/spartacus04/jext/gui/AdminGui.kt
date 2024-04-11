package me.spartacus04.jext.gui

import me.spartacus04.jext.State
import me.spartacus04.jext.State.DISCS
import me.spartacus04.jext.State.VERSION
import me.spartacus04.jext.geyser.GeyserIntegration
import me.spartacus04.jext.language.LanguageManager
import org.bukkit.entity.Player
import xyz.xenondevs.invui.inventory.VirtualInventory
import xyz.xenondevs.invui.inventory.event.ItemPostUpdateEvent
import xyz.xenondevs.invui.window.Window

internal class AdminGui(player: Player) {
    private val inv: VirtualInventory

    init {
        try {
            if(GeyserIntegration.GEYSER?.isBedrockPlayer(player) == true) {
                player.sendMessage(LanguageManager.BEDROCK_NOT_SUPPORTED)
            }
        } catch (_: NoClassDefFoundError) { }

        inv = VirtualInventory(
            DISCS.size() * if(VERSION >= "1.19") 2 else 1
        )

        DISCS.forEachIndexed { i, it ->
            inv.setItemSilently(i, it.discItemStack)
        }

        if(VERSION >= "1.19") {
            DISCS.forEachIndexed {i, it ->
                inv.setItemSilently(i + DISCS.size(), it.fragmentItemStack)
            }
        }

        inv.setPostUpdateHandler(this::itemPostUpdateHandler)

        val gui = GuiBuilder().buildGui(player, inv)

        val window = Window.single()
            .setViewer(player)
            .setTitle(State.LANG.getKey(player, "jukebox"))
            .setGui(gui)
            .build()

        window.open()

        inv.notifyWindows()
    }

    private fun itemPostUpdateHandler(event: ItemPostUpdateEvent) {
        if(event.isRemove) {
            inv.setItemSilently(event.slot,
                if(event.slot >= DISCS.size())
                    DISCS[event.slot - DISCS.size()].fragmentItemStack
                else
                    DISCS[event.slot].discItemStack
            )

            return
        }

        inv.setItemSilently(event.slot,
            if(event.slot >= DISCS.size())
                DISCS[event.slot - DISCS.size()].fragmentItemStack
            else
                DISCS[event.slot].discItemStack
        )
    }
}