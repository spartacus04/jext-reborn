package me.spartacus04.jext.jukebox

import me.spartacus04.jext.config.ConfigData.Companion.LANG
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.ScrollGui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.controlitem.ScrollItem

class ScrollDownItem(private val player: Player) : ScrollItem(1) {
    override fun getItemProvider(gui: ScrollGui<*>): ItemProvider {
        val builder = ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)

        builder.setDisplayName(LANG.getKey(player, "scroll-down"))

        if (!gui.canScroll(1))
            builder.addLoreLines(LANG.getKey(player, "cant-scroll-further"))

        return builder
    }
}