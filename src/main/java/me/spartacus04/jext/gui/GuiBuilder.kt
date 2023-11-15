package me.spartacus04.jext.gui

import me.spartacus04.jext.State.CONFIG
import me.spartacus04.jext.config.fields.FieldGuiStyle
import me.spartacus04.jext.gui.items.NextPageItem
import me.spartacus04.jext.gui.items.PreviousPageItem
import me.spartacus04.jext.gui.items.ScrollDownItem
import me.spartacus04.jext.gui.items.ScrollUpItem
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.ScrollGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.inventory.Inventory
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem

class GuiBuilder {
    private val border = SimpleItem(ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§r"))

    fun buildGui(player: Player, inventory: Inventory) : Gui.Builder<*, *> {
        return when(CONFIG.GUI_STYLE) {
            FieldGuiStyle.SCROLL_VERTICAL -> ScrollGui.inventories()
                .setStructure(
                    "x x x x x x x x u",
                    "x x x x x x x x #",
                    "x x x x x x x x #",
                    "x x x x x x x x #",
                    "x x x x x x x x #",
                    "x x x x x x x x d"
                )
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('u', ScrollUpItem(player, false))
                .addIngredient('d', ScrollDownItem(player, false))
                .addIngredient('#', border)
                .setContent(listOf(inventory))

            FieldGuiStyle.SCROLL_HORIZONTAL -> ScrollGui.inventories()
                .setStructure(
                    "x x x x x x x x x",
                    "x x x x x x x x x",
                    "x x x x x x x x x",
                    "x x x x x x x x x",
                    "x x x x x x x x x",
                    "u # # # # # # # d"
                )
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_VERTICAL)
                .addIngredient('u', ScrollUpItem(player, true))
                .addIngredient('d', ScrollDownItem(player, true))
                .addIngredient('#', border)
                .setContent(listOf(inventory))

            FieldGuiStyle.PAGE_HORIZONTAL -> PagedGui.inventories()
                .setStructure(
                    "x x x x x x x x x",
                    "x x x x x x x x x",
                    "x x x x x x x x x",
                    "x x x x x x x x x",
                    "x x x x x x x x x",
                    "u # # # # # # # d"
                )
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_VERTICAL)
                .addIngredient('u', PreviousPageItem(player))
                .addIngredient('d', NextPageItem(player))
                .addIngredient('#', border)
                .setContent(listOf(inventory))

            FieldGuiStyle.PAGE_VERTICAL -> PagedGui.inventories()
                .setStructure(
                    "x x x x x x x x u",
                    "x x x x x x x x #",
                    "x x x x x x x x #",
                    "x x x x x x x x #",
                    "x x x x x x x x #",
                    "x x x x x x x x d"
                )
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_VERTICAL)
                .addIngredient('u', PreviousPageItem(player))
                .addIngredient('d', NextPageItem(player))
                .addIngredient('#', border)
                .setContent(listOf(inventory))
        }


    }
}