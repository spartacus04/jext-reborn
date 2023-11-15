package me.spartacus04.jext.gui.items

import me.spartacus04.jext.State.LANG
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.ScrollGui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.controlitem.ScrollItem

internal class ScrollDownItem(private val player: Player, private val horizontal: Boolean) : ScrollItem(1) {
    override fun getItemProvider(gui: ScrollGui<*>): ItemProvider {
        val builder = ItemBuilder(Material.RED_STAINED_GLASS_PANE)

        builder.setDisplayName(LANG.getKey(player, if(horizontal) "scroll-left" else "scroll-down"))

        if (!gui.canScroll(1))
            builder.addLoreLines(LANG.getKey(player, "cant-scroll-further"))

        return builder
    }
}