package me.spartacus04.jext.discs.sources.file

import com.google.gson.annotations.SerializedName
import me.spartacus04.jext.Jext
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.discs.DiscUtils
import me.spartacus04.jext.discs.discplaying.DefaultDiscPlayingMethod
import me.spartacus04.jext.utils.Constants.JEXT_DISC_MATERIAL
import me.spartacus04.jext.utils.Constants.JEXT_FRAGMENT_MATERIAL
import org.bukkit.Bukkit

@Suppress("PropertyName")
internal data class FileDisc(
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
    fun toJextDisc(plugin: Jext) : Disc {
        return Disc(
            "jext",
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
                plugin.i18nManager!![Bukkit.getConsoleSender(), "disc-name",
                    "author" to AUTHOR,
                    "title" to TITLE
                ]!!
            } else {
                plugin.i18nManager!![Bukkit.getConsoleSender(), "disc-name-simple",
                    "title" to TITLE
                ]!!
            },
            DURATION,
            CREEPER_DROP,
            LOOT_TABLES,
            FRAGMENT_LOOT_TABLES,
            DefaultDiscPlayingMethod(),
            plugin
        )
    }
}