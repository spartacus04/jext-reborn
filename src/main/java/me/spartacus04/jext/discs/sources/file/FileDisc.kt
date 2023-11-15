package me.spartacus04.jext.discs.sources.file

import com.google.gson.annotations.SerializedName
import me.spartacus04.jext.State.LANG
import me.spartacus04.jext.State.VERSION
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.discs.DiscPersistentDataContainer
import me.spartacus04.jext.utils.Constants.JEXT_DISC_MATERIAL
import me.spartacus04.jext.utils.Constants.JEXT_FRAGMENT_MATERIAL
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

data class FileDisc(
    @SerializedName("title")
    val TITLE: String = "",

    @SerializedName("author")
    val AUTHOR: String = "",

    @SerializedName("duration")
    val DURATION: Int = -1,

    @SerializedName("disc-namespace")
    val DISC_NAMESPACE: String,

    @SerializedName("model-data")
    val MODEL_DATA: Int,

    @SerializedName("creeper-drop")
    val CREEPER_DROP: Boolean = false,

    @SerializedName("lores")
    val LORE: List<String> = listOf(),

    @SerializedName("loot-tables")
    val LOOT_TABLES: HashMap<String, Int> = HashMap(),

    @SerializedName("fragment-loot-tables")
    val FRAGMENT_LOOT_TABLES: HashMap<String, Int> = HashMap(),
) {
    private fun getProcessedLore(): ArrayList<String> {
        val lore = ArrayList<String>()

        if(AUTHOR != "")
            lore.add("${ChatColor.GRAY}$AUTHOR - $TITLE")
        else {
            lore.add("${ChatColor.GRAY}$TITLE")
        }

        lore.addAll(LORE)

        return lore
    }

    private fun getDiscItemStack(): ItemStack {
        val disc = ItemStack(JEXT_DISC_MATERIAL)
        val meta = disc.itemMeta

        meta!!.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)

        meta.setCustomModelData(MODEL_DATA)

        // Store custom disc data
        val helper = DiscPersistentDataContainer(meta)
        helper.namespaceID = DISC_NAMESPACE
        helper.setIdentifier()

        meta.lore = getProcessedLore()
        disc.itemMeta = meta
        return disc
    }

    private fun getFragmentItemStack(): ItemStack {
        val fragment = ItemStack(JEXT_FRAGMENT_MATERIAL)
        val meta = fragment.itemMeta

        meta!!.setCustomModelData(MODEL_DATA)
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)

        val helper = DiscPersistentDataContainer(meta)
        helper.namespaceID = DISC_NAMESPACE
        helper.setIdentifier()

        meta.lore = getProcessedLore()
        fragment.itemMeta = meta

        return fragment
    }

    fun toJextDisc() : Disc {
        return Disc(
            getDiscItemStack(),
            if(VERSION > "1.19") getFragmentItemStack() else null,
            DISC_NAMESPACE,
            if(AUTHOR.isNotEmpty()) {
                LANG.getKey(Bukkit.getConsoleSender(), "disc-name", hashMapOf(
                    "author" to AUTHOR,
                    "title" to TITLE
                ))
            } else {
                LANG.getKey(Bukkit.getConsoleSender(), "disc-name-simple", hashMapOf(
                    "title" to TITLE
                ))
            },
            DURATION,
            CREEPER_DROP,
            LOOT_TABLES,
            FRAGMENT_LOOT_TABLES
        )
    }
}