package me.spartacus04.jext.discs.sources.nbs

import com.google.gson.annotations.SerializedName
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder
import me.spartacus04.jext.JextState.LANG
import me.spartacus04.jext.JextState.PLUGIN
import me.spartacus04.jext.JextState.VERSION
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.discs.DiscUtils
import me.spartacus04.jext.discs.discplaying.NbsDiscPlayingMethod
import me.spartacus04.jext.language.LanguageManager.Companion.NBS_NOT_FOUND
import me.spartacus04.jext.utils.Constants.JEXT_DISC_MATERIAL
import me.spartacus04.jext.utils.Constants.JEXT_FRAGMENT_MATERIAL
import org.bukkit.Bukkit
import kotlin.math.ceil

@Suppress("PropertyName")
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
    fun toJextDisc() : Disc? {
        val nbsFile = PLUGIN.dataFolder.resolve("nbs").resolve("$DISC_NAMESPACE.nbs")

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
            DiscUtils.buildCustomItemstack(
                JEXT_DISC_MATERIAL,
                MODEL_DATA,
                DISC_NAMESPACE,
                LORE,
                TITLE,
                AUTHOR
            ),
            if(VERSION >= "1.19") DiscUtils.buildCustomItemstack(
                JEXT_FRAGMENT_MATERIAL!!,
                MODEL_DATA,
                DISC_NAMESPACE,
                LORE,
                TITLE,
                AUTHOR
            ) else null,
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