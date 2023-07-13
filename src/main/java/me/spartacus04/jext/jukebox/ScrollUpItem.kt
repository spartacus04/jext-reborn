package me.spartacus04.jext.jukebox

import me.spartacus04.jext.config.ConfigData.Companion.LANG
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.ScrollGui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.controlitem.ScrollItem

class ScrollUpItem(private val player: Player) : ScrollItem(-1) {
    /**
     * The function returns an ItemProvider object with a red stained glass pane as the item, a display name, and lore
     * lines based on the scroll state of the GUI.
     *
     * @param gui The `gui` parameter is an instance of the `ScrollGui` class. It represents the graphical user interface
     * for scrolling through items.
     * @return An ItemProvider object is being returned.
     */
    override fun getItemProvider(gui: ScrollGui<*>): ItemProvider {
        val builder = ItemBuilder(Material.RED_STAINED_GLASS_PANE)

        builder.setDisplayName(LANG.getKey(player, "scroll-up"))

        if (!gui.canScroll(-1))
            builder.addLoreLines(LANG.getKey(player, "cant-scroll-further"))

        return builder
    }
}