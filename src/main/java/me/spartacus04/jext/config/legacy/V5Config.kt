package me.spartacus04.jext.config.legacy

import com.google.gson.annotations.SerializedName
import me.spartacus04.jext.JextState.GSON

@Suppress("PropertyName")
internal data class V5Config (
    @SerializedName("lang")
    var LANGUAGE_MODE: String = "auto",

    @SerializedName("force-resource-pack")
    var FORCE_RESOURCE_PACK : Boolean = false,

    @SerializedName("ignore-failed-download")
    var IGNORE_FAILED_DOWNLOAD : Boolean = true,

    @SerializedName("allow-music-overlapping")
    var ALLOW_MUSIC_OVERLAPPING : Boolean = false,

    @SerializedName("allow-metrics")
    var ALLOW_METRICS : Boolean = true,

    @SerializedName("jukebox-gui")
    var JUKEBOX_GUI : Boolean = false,

    @SerializedName("discs-random-chance")
    var DISCS_RANDOM_CHANCE : Int = 200,

    @SerializedName("fragments-random-chance")
    var FRAGMENTS_RANDOM_CHANCE : Int = 200,

    @SerializedName("disc-loottables-limit")
    var DISC_LIMIT : HashMap<String, Int> = hashMapOf(
        "chests/*" to 2
    ),

    @SerializedName("fragment-loottables-limit")
    var FRAGMENT_LIMIT : HashMap<String, Int> = hashMapOf(
        "chests/*" to 3
    )
) : ConfigMigrator {
    override fun migrateToNext(): String {
        return GSON.toJson(V6Config(
            LANGUAGE_MODE = LANGUAGE_MODE,
            FORCE_RESOURCE_PACK = FORCE_RESOURCE_PACK,
            IGNORE_FAILED_DOWNLOAD = IGNORE_FAILED_DOWNLOAD,
            ALLOW_MUSIC_OVERLAPPING = ALLOW_MUSIC_OVERLAPPING,
            ALLOW_METRICS = ALLOW_METRICS,
            JUKEBOX_BEHAVIOUR = if(JUKEBOX_GUI) "legacy-gui" else "vanilla",
            DISCS_RANDOM_CHANCE = DISCS_RANDOM_CHANCE,
            FRAGMENTS_RANDOM_CHANCE = FRAGMENTS_RANDOM_CHANCE,
            DISC_LIMIT = DISC_LIMIT,
            FRAGMENT_LIMIT = FRAGMENT_LIMIT
        ))
    }

}