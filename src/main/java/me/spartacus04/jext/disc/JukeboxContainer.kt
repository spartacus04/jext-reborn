package me.spartacus04.jext.disc

import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.Pane
import com.github.stefvanschie.inventoryframework.pane.PatternPane
import com.github.stefvanschie.inventoryframework.pane.util.Pattern
import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.TileState
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.ItemStack


class JukeboxContainer(val location: Location, val player: HumanEntity) {
    private val gui = ChestGui(6, "Jukebox")
    private val pDC: JukeboxPersistentDataContainer

    init {
        val state = location.block.state as TileState
        pDC = JukeboxPersistentDataContainer(state)

        pDC.discList.add(DiscContainer(DISCS[0]))

        val pattern = Pattern(
            "111111111",
            "100000001",
            "100000001",
            "100000001",
            "100000001",
            "111111111"
        )

        val background = PatternPane(0, 0, 9, 6, pattern)
        background.bindItem('1', GuiItem(ItemStack(Material.BLACK_STAINED_GLASS_PANE)))

        background.setOnClick {
            it.isCancelled = true
        }

        gui.addPane(background)

        val discHolder = OutlinePane(1, 1, 7, 2, Pane.Priority.LOW)

        pDC.discList.forEach {
            discHolder.addItem(GuiItem(it.discItem))
        }

        if(pDC.currentIndex != -1) discHolder.items[pDC.currentIndex].item.addUnsafeEnchantment(Enchantment.MENDING, 1)

        discHolder.setOnClick {
            if(it.currentItem == null || !it.isLeftClick) return@setOnClick

            pDC.currentIndex = if(it.currentItem!!.containsEnchantment(Enchantment.MENDING)) {
                it.currentItem!!.removeEnchantment(Enchantment.MENDING)
                if(pDC.currentIndex != -1) DiscPlayer(DiscContainer(it.currentItem!!)).stop(location)
                -1
            } else {
                it.currentItem!!.addUnsafeEnchantment(Enchantment.MENDING, 1)
                if(pDC.currentIndex != -1) DiscPlayer(DiscContainer(it.currentItem!!)).stop(location)
                if(it.currentItem != null) DiscPlayer(DiscContainer(it.currentItem!!)).play(location)
                it.slot
            }
        }

        gui.addPane(discHolder)

        gui.show(player)
    }
}