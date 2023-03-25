package me.spartacus04.jext.config

import com.google.gson.annotations.SerializedName

data class Disc(
    @SerializedName("title")
    var TITLE: String,

    @SerializedName("author")
    var AUTHOR: String,

    @SerializedName("duration")
    var DURATION: Int = -1,

    @SerializedName("disc-namespace")
    var DISC_NAMESPACE: String,

    @SerializedName("model-data")
    var MODEL_DATA: Int,

    @SerializedName("creeper-drop")
    var CREEPER_DROP: Boolean,

    @SerializedName("lores")
    var LORE: List<String>,

    @SerializedName("loot-tables")
    val LOOT_TABLES: List<String>?,

    @SerializedName("fragment-loot-tables")
    val FRAGMENT_LOOT_TABLES: List<String>?
)