package me.spartacus04.jext.config.legacy

import com.google.gson.annotations.SerializedName
import me.spartacus04.colosseum.ColosseumPlugin

@Suppress("PropertyName")
internal data class V2Config (
    @SerializedName("lang")
    var LANGUAGE_FILE: String = "auto",

    @SerializedName("force-resource-pack")
    var FORCE_RESOURCE_PACK : Boolean = false,

    @SerializedName("ignore-failed-download")
    var IGNORE_FAILED_DOWNLOAD : Boolean = true,

    @SerializedName("allow-music-overlapping")
    var ALLOW_MUSIC_OVERLAPPING : Boolean = false,
) : ConfigMigrator {
    override fun migrateToNext(plugin: ColosseumPlugin): String {
        return plugin.gson.toJson(V3Config(
            LANGUAGE_MODE = LANGUAGE_FILE,
            FORCE_RESOURCE_PACK = FORCE_RESOURCE_PACK,
            IGNORE_FAILED_DOWNLOAD = IGNORE_FAILED_DOWNLOAD,
            ALLOW_MUSIC_OVERLAPPING = ALLOW_MUSIC_OVERLAPPING
        ))
    }

}