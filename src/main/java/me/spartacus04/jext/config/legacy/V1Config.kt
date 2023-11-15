package me.spartacus04.jext.config.legacy

import com.google.gson.annotations.SerializedName

internal data class V1Config (
    @SerializedName("force-resource-pack")
    var FORCE_RESOURCE_PACK : Boolean,

    @SerializedName("resource-pack-decline-kick-message")
    var RESOURCE_PACK_DECLINE_KICK_MESSAGE : String,

    @SerializedName("ignore-failed-download")
    var IGNORE_FAILED_DOWNLOAD : Boolean,

    @SerializedName("failed-download-kick-message")
    var FAILED_DOWNLOAD_KICK_MESSAGE : String,

    @SerializedName("allow-music-overlapping")
    var ALLOW_MUSIC_OVERLAPPING : Boolean,
)