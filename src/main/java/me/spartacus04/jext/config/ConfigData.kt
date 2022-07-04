package me.spartacus04.jext.config

import com.google.gson.annotations.SerializedName

data class Config (
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

data class Disc(
    @SerializedName("title")
    var TITLE: String,

    @SerializedName("author")
    var AUTHOR: String,

    @SerializedName("disc-namespace")
    var DISC_NAMESPACE: String,

    @SerializedName("model-data")
    var MODEL_DATA: Int,

    @SerializedName("creeper-drop")
    var CREEPER_DROP: Boolean,

    @SerializedName("lores")
    var LORE: List<String>
)