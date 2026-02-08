package me.spartacus04.jext.discs

import me.spartacus04.jext.Jext.Companion.INSTANCE
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

/**
 * The object `DiscUtils` is a utility object that's used to build custom itemstacks for the discs.
 */
object DiscUtils {
    private fun getProcessedLores(lores: List<String>, title: String, author: String): ArrayList<String> {
        val lore = ArrayList<String>()

        if(!INSTANCE.config.DISC_MODIFY_ITEM_NAME)
            if(author != "")
                lore.add("${ChatColor.GRAY}$author - $title")
            else
                lore.add("${ChatColor.GRAY}$title")

        lore.addAll(lores)

        return lore
    }

    /**
     * Builds a custom itemstack for the disc.
     * 
     * @param material The material of the itemstack.
     * @param modelData The model data of the itemstack.
     * @param namespace The namespace of the disc.
     * @param lores The lores of the itemstack.
     * @param title The title of the itemstack.
     * @param author The author of the itemstack.
     * 
     * @return The custom itemstack.
     */
    fun buildCustomItemstack(material: Material, modelData: Int, namespace: String, lores: List<String>, title: String, author: String): ItemStack {
        val disc = if(INSTANCE.serverVersion >= "1.21.5") {
            if(material.isRecord) {
                Bukkit.getItemFactory().createItemStack("${material.name.lowercase()}[jukebox_playable=\"minecraft:11\",tooltip_display={hidden_components:[\"minecraft:jukebox_playable\"]}]")
            } else {
                Bukkit.getItemFactory().createItemStack("${material.name.lowercase()}[tooltip_display={hidden_components:[\"minecraft:jukebox_playable\"]}]")
            }
        } else if(INSTANCE.serverVersion >= "1.21") {
            if(material.isRecord) {
                Bukkit.getItemFactory().createItemStack("${material.name.lowercase()}[jukebox_playable={song:\"minecraft:11\",show_in_tooltip:false}]")
            } else {
                Bukkit.getItemFactory().createItemStack("${material.name.lowercase()}[show_in_tooltip=false]")
            }
        } else {
            ItemStack(material)
        }
        val meta = disc.itemMeta

        meta!!.addItemFlags(if(INSTANCE.serverVersion >= "1.19.5")
            ItemFlag.HIDE_ADDITIONAL_TOOLTIP
        else
            ItemFlag.valueOf("HIDE_POTION_EFFECTS")
        )

        meta.setCustomModelData(modelData)

        if (INSTANCE.config.DISC_MODIFY_ITEM_NAME)
            if(author != "") {
                meta.setItemName("${ChatColor.YELLOW}$author - $title")
                meta.setDisplayName("${ChatColor.YELLOW}$author - $title")
            } else {
                meta.setItemName("${ChatColor.YELLOW}$title")
                meta.setDisplayName("${ChatColor.YELLOW}$title")
            }

        // Store custom disc data
        val helper = DiscPersistentDataContainer(meta)
        helper.namespaceID = namespace
        helper.setIdentifier()

        meta.lore = getProcessedLores(lores, title, author)
        disc.itemMeta = meta
        return disc
    }
}