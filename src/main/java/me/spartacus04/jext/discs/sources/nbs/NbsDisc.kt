package me.spartacus04.jext.discs.sources.nbs

import com.google.gson.annotations.SerializedName
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder
import me.spartacus04.jext.JextState.LANG
import me.spartacus04.jext.JextState.PLUGIN
import me.spartacus04.jext.JextState.VERSION
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.discs.DiscPersistentDataContainer
import me.spartacus04.jext.discs.discplaying.NbsDiscPlayingMethod
import me.spartacus04.jext.language.LanguageManager.Companion.NBS_NOT_FOUND
import me.spartacus04.jext.utils.Constants.JEXT_DISC_MATERIAL
import me.spartacus04.jext.utils.Constants.JEXT_FRAGMENT_MATERIAL
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import kotlin.math.ceil

internal data class NbsDisc(
    @SerializedName("title")
    val TITLE: String = "",

    @SerializedName("author")
    val AUTHOR: String = "",

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
    private val nbsFile = PLUGIN.dataFolder.resolve("nbs").resolve("$DISC_NAMESPACE.nbs")

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


    fun toJextDisc() : Disc? {
        if(!nbsFile.exists()) {
            Bukkit.getConsoleSender().sendMessage(
                LANG.replaceParameters(NBS_NOT_FOUND, hashMapOf(
                    "name" to DISC_NAMESPACE
                ))
            )

            return null
        }

        val song = NBSDecoder.parse(nbsFile)

        return Disc(
            "jext-nbs",
            getDiscItemStack(),
            if(VERSION > "1.19") getFragmentItemStack() else null,
            DISC_NAMESPACE,
            if(AUTHOR.isNotEmpty()) {
                LANG.getKey(
                    Bukkit.getConsoleSender(), "disc-name", hashMapOf(
                    "author" to AUTHOR,
                    "title" to TITLE
                ))
            } else {
                LANG.getKey(
                    Bukkit.getConsoleSender(), "disc-name-simple", hashMapOf(
                    "title" to TITLE
                ))
            },
            ceil(song.length / song.speed).toInt(),
            CREEPER_DROP,
            LOOT_TABLES,
            FRAGMENT_LOOT_TABLES,
            NbsDiscPlayingMethod(song)
        )
    }
}