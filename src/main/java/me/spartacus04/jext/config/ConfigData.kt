package me.spartacus04.jext.config

import com.google.gson.annotations.SerializedName

data class Config (
    @SerializedName("lang")
    var LANGUAGE_FILE: String,

    @SerializedName("force-resource-pack")
    var FORCE_RESOURCE_PACK : Boolean,

    @SerializedName("ignore-failed-download")
    var IGNORE_FAILED_DOWNLOAD : Boolean,

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

data class Messages(
    @SerializedName("now-playing")
    var NOW_PLAYING: String,

    @SerializedName("now-playing-no-author")
    var NOW_PLAYING_NO_AUTHOR: String,

    @SerializedName("resource-pack-decline-kick-message")
    var RESOURCE_PACK_DECLINE_KICK_MESSAGE : String,

    @SerializedName("failed-download-kick-message")
    var FAILED_DOWNLOAD_KICK_MESSAGE : String,

    @SerializedName("disc-command-success")
    var DISC_COMMAND_SUCCESS : String,

    @SerializedName("disc-namespace-not-found")
    var DISC_NAMESPACE_NOT_FOUND : String,

    @SerializedName("disc-received")
    var DISC_RECEIVED : String,

    @SerializedName("disc-given-multiple")
    var DISC_GIVEN_MULTIPLE : String,

    @SerializedName("disc-given")
    var DISC_GIVEN : String,

    @SerializedName("no-disc-given")
    var NO_DISC_GIVEN : String,

    @SerializedName("invalid-location")
    var INVALID_LOCATION : String,

    @SerializedName("wrong-number-format")
    var WRONG_NUMBER_FORMAT : String,

    @SerializedName("cannot-find-player")
    var CANNOT_FIND_PLAYER : String,

    @SerializedName("music-now-playing")
    var MUSIC_NOW_PLAYING : String,

    @SerializedName("played-music-to-multiple")
    var PLAYED_MUSIC_TO_MULTIPLE : String,

    @SerializedName("played-music-to")
    var PLAYED_MUSIC_TO : String,

    @SerializedName("played-music-to-no-one")
    var PLAYED_MUSIC_TO_NO_ONE : String,

    @SerializedName("stopped-music")
    var STOPPED_MUSIC : String,

    @SerializedName("stopped-all-music")
    var STOPPED_ALL_MUSIC : String,

    @SerializedName("stopped-music-for-multiple")
    var STOPPED_MUSIC_FOR_MULTIPLE : String,

    @SerializedName("stopped-music-for")
    var STOPPED_MUSIC_FOR : String,

    @SerializedName("stopped-music-for-no-one")
    var STOPPED_MUSIC_FOR_NO_ONE : String,

    @SerializedName("reloaded")
    var RELOADED: String,
)