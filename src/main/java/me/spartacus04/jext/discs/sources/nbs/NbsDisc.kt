package me.spartacus04.jext.discs.sources.nbs

import com.google.gson.annotations.SerializedName
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder
import me.spartacus04.colosseum.i18n.ColosseumI18nManager
import me.spartacus04.jext.Jext
import me.spartacus04.jext.Jext.Companion.INSTANCE
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.discs.DiscUtils
import me.spartacus04.jext.discs.discplaying.NbsDiscPlayingMethod
import me.spartacus04.jext.language.DefaultMessages.NBS_NOT_FOUND
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
    fun toJextDisc(plugin: Jext) : Disc? {
        val nbsFile = plugin.dataFolder.resolve("nbs").resolve("$DISC_NAMESPACE.nbs")

        if(!nbsFile.exists()) {
            plugin.colosseumLogger.warn(
                ColosseumI18nManager.replacePlaceholders(NBS_NOT_FOUND, hashMapOf(
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
            if(plugin.serverVersion >= "1.19") DiscUtils.buildCustomItemstack(
                JEXT_FRAGMENT_MATERIAL!!,
                MODEL_DATA,
                DISC_NAMESPACE,
                LORE,
                TITLE,
                AUTHOR
            ) else null,
            DISC_NAMESPACE,
            if(AUTHOR.isNotEmpty()) {
                INSTANCE.i18nManager!![Bukkit.getConsoleSender(), "disc-name",
                    "author" to AUTHOR,
                    "title" to TITLE
                ]!!
            } else {
                INSTANCE.i18nManager!![Bukkit.getConsoleSender(), "disc-name-simple",
                    "title" to TITLE
                ]!!
            },
            ceil(song.length / song.speed).toInt(),
            CREEPER_DROP,
            LOOT_TABLES,
            FRAGMENT_LOOT_TABLES,
            NbsDiscPlayingMethod(song),
            plugin
        )
    }
}