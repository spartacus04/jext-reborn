package me.spartacus04.jext.config.legacy

import com.google.gson.annotations.SerializedName
import me.spartacus04.jext.JextState.GSON
import me.spartacus04.jext.config.fields.FieldJukeboxBehaviour
import me.spartacus04.jext.config.fields.FieldLanguageMode

@Suppress("PropertyName")
internal data class V6Config (
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

    @SerializedName("jukebox-behaviour")
    var JUKEBOX_BEHAVIOUR : String = "vanilla",

    @SerializedName("discs-random-chance")
    var DISCS_RANDOM_CHANCE : Int = 200,

    @SerializedName("fragments-random-chance")
    var FRAGMENTS_RANDOM_CHANCE : Int = 200,

    @SerializedName("disc-loottables-limit")
    var DISC_LIMIT : HashMap<String, Int> = hashMapOf(),

    @SerializedName("fragment-loottables-limit")
    var FRAGMENT_LIMIT : HashMap<String, Int> = hashMapOf(),
) : ConfigMigrator {
    override fun migrateToNext(): String {
        return GSON.toJson(V7Config(
            LANGUAGE_MODE = try {
                FieldLanguageMode.fromString(LANGUAGE_MODE)
            } catch (_: IllegalArgumentException) { FieldLanguageMode.AUTO },
            JUKEBOX_BEHAVIOUR = try {
                FieldJukeboxBehaviour.fromString(JUKEBOX_BEHAVIOUR)
            } catch (_: IllegalArgumentException) {
                if(JUKEBOX_BEHAVIOUR == "legacy-gui")
                    FieldJukeboxBehaviour.GUI
                else
                    FieldJukeboxBehaviour.VANILLA
            },
            DISABLE_MUSIC_OVERLAP = !ALLOW_MUSIC_OVERLAPPING,
            DISC_LIMIT = DISC_LIMIT,
            FRAGMENT_LIMIT = FRAGMENT_LIMIT,
            FORCE_RESOURCE_PACK = FORCE_RESOURCE_PACK,
            ALLOW_METRICS = ALLOW_METRICS
        ))
    }
}