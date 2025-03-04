package me.spartacus04.jext.config.legacy

import com.google.gson.annotations.SerializedName
import me.spartacus04.jext.JextState.GSON

@Suppress("PropertyName")
internal data class V1Config (
    @SerializedName("force-resource-pack")
    var FORCE_RESOURCE_PACK : Boolean = false,

    @SerializedName("resource-pack-decline-kick-message")
    var RESOURCE_PACK_DECLINE_KICK_MESSAGE : String = "",

    @SerializedName("ignore-failed-download")
    var IGNORE_FAILED_DOWNLOAD : Boolean = true,

    @SerializedName("failed-download-kick-message")
    var FAILED_DOWNLOAD_KICK_MESSAGE : String = "",

    @SerializedName("allow-music-overlapping")
    var ALLOW_MUSIC_OVERLAPPING : Boolean = false,
) : ConfigMigrator {
    override fun migrateToNext(): String {
        return GSON.toJson(V2Config(
            FORCE_RESOURCE_PACK = FORCE_RESOURCE_PACK,
            IGNORE_FAILED_DOWNLOAD = IGNORE_FAILED_DOWNLOAD,
            ALLOW_MUSIC_OVERLAPPING = ALLOW_MUSIC_OVERLAPPING
        ))
    }
}