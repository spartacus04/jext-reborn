package me.spartacus04.jext.discs

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object DiscUtils {
    private fun getProcessedLores(lores: List<String>, title: String, author: String): ArrayList<String> {
        val lore = ArrayList<String>()

        if(author != "")
            lore.add("${ChatColor.GRAY}$author - $title")
        else {
            lore.add("${ChatColor.GRAY}$title")
        }

        lore.addAll(lores)

        return lore
    }

    fun buildCustomItemstack(material: Material, modelData: Int, namespace: String, lores: List<String>, title: String, author: String): ItemStack {
        val disc = ItemStack(material)
        val meta = disc.itemMeta

        meta!!.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)

        meta.setCustomModelData(modelData)

        // Store custom disc data
        val helper = DiscPersistentDataContainer(meta)
        helper.namespaceID = namespace
        helper.setIdentifier()

        meta.lore = getProcessedLores(lores, title, author)
        disc.itemMeta = meta
        return disc
    }
}