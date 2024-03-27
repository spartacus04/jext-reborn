package me.spartacus04.jext.gui.items

import me.spartacus04.jext.State
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.controlitem.PageItem

internal class NextPageItem(private val player: Player) : PageItem(true) {
    override fun getItemProvider(gui: PagedGui<*>): ItemProvider {
        val builder = ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)

        builder.setDisplayName(State.LANG.getKey(player, "next-page"))

        if (!gui.hasNextPage())
            builder.addLoreLines(State.LANG.getKey(player, "no-page"))

        return builder
    }
}