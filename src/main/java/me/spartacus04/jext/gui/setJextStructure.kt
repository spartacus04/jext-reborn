package me.spartacus04.jext.gui

import me.spartacus04.colosseum.gui.Gui
import me.spartacus04.colosseum.gui.virtualInventory.VirtualInventory
import me.spartacus04.jext.Jext
import me.spartacus04.jext.config.fields.FieldGuiStyle
import me.spartacus04.jext.gui.items.NextPageItem
import me.spartacus04.jext.gui.items.PreviousPageItem
import me.spartacus04.jext.gui.items.ScrollDownItem
import me.spartacus04.jext.gui.items.ScrollUpItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun Gui.Builder.setJextStructure(player: Player, inventory: VirtualInventory) : Gui.Builder {
    if(plugin !is Jext) {
        throw IllegalStateException("Plugin must be an instance of Jext to use JextGuis")
    }

    this.setPlayer(player)

    val border = ItemStack(Material.BLACK_STAINED_GLASS_PANE).apply {
        this.itemMeta = this.itemMeta!!.apply {
            setDisplayName("§r")
        }
    }

    val plg = plugin as Jext

    return when(plg.config.GUI_STYLE) {
        FieldGuiStyle.SCROLL_VERTICAL -> this
            .setStructure(
                "x x x x x x x x u",
                "x x x x x x x x #",
                "x x x x x x x x #",
                "x x x x x x x x #",
                "x x x x x x x x #",
                "x x x x x x x x d"
            )
            .setBinding('#', border)
            .setBinding('u', ScrollUpItem(
                false,
                -8,
                player,
                plg
            ))
            .setBinding('d', ScrollDownItem(
                false,
                8,
                player,
                plg
            ))
            .setBinding(
                'x',
                inventory,
                Gui.Builder.DirectionMarker.HORIZONTAL
            )

        FieldGuiStyle.SCROLL_HORIZONTAL -> this
            .setStructure(
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x",
                "u # # # # # # # d"
            )
            .setBinding('#', border)
            .setBinding('u', ScrollUpItem(
                true,
                -9,
                player,
                plg
            ))
            .setBinding('d', ScrollDownItem(
                true,
                9,
                player,
                plg
            ))
            .setBinding(
                'x',
                inventory,
                Gui.Builder.DirectionMarker.HORIZONTAL
            )

        FieldGuiStyle.PAGE_HORIZONTAL -> this
            .setStructure(
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x",
                "u # # # # # # # d"
            )
            .setBinding('#', border)
            .setBinding('u', PreviousPageItem(
                    -45,
                    player,
                    plg
            ))
            .setBinding('d', NextPageItem(
                45,
                player,
                plg
            ))
            .setBinding(
                'x',
                inventory,
                Gui.Builder.DirectionMarker.HORIZONTAL
            )

        FieldGuiStyle.PAGE_VERTICAL -> this
            .setStructure(
                "x x x x x x x x u",
                "x x x x x x x x #",
                "x x x x x x x x #",
                "x x x x x x x x #",
                "x x x x x x x x #",
                "x x x x x x x x d"
            )
            .setBinding('#', border)
            .setBinding('u', PreviousPageItem(
                -48,
                player,
                plg
            ))
            .setBinding('d', NextPageItem(
                48,
                player,
                plg
            ))
            .setBinding(
                'x',
                inventory,
                Gui.Builder.DirectionMarker.HORIZONTAL
            )
    }
}