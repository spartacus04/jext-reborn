package me.spartacus04.jext.config.legacy

import com.google.gson.annotations.SerializedName

internal data class V2Config (
    @SerializedName("lang")
    var LANGUAGE_FILE: String,

    @SerializedName("force-resource-pack")
    var FORCE_RESOURCE_PACK : Boolean,

    @SerializedName("ignore-failed-download")
    var IGNORE_FAILED_DOWNLOAD : Boolean,

    @SerializedName("allow-music-overlapping")
    var ALLOW_MUSIC_OVERLAPPING : Boolean,
)