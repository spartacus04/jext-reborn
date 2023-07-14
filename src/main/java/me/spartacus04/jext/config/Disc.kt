package me.spartacus04.jext.config

import com.google.gson.annotations.SerializedName

/**
 * The code defines a data class representing a disc with various properties such as title, author,
 * duration, and loot tables.
 * @property {String} TITLE - The title of the disc.
 * @property {String} AUTHOR - The `AUTHOR` property represents the author of the disc.
 * @property {Int} DURATION - The `DURATION` property represents the duration of the disc in seconds.
 * @property {String} DISC_NAMESPACE - The `DISC_NAMESPACE` property represents the namespace of the disc.
 * @property {Int} MODEL_DATA - The `MODEL_DATA` property is of type `Int` and represents the model data of the disc.
 * @property {Boolean} CREEPER_DROP - The property "CREEPER_DROP" is a boolean value that indicates whether the disc can be
 * dropped by a creeper.
 * @property {List<String>} LORE - LORE is a property of type List<String> which represents the list of lores associated
 * with the disc.
 * @property {List<String>?} LOOT_TABLES - LOOT_TABLES is a list of strings that represents the loot tables associated with
 * the disc.
 * @property {List<String>?} FRAGMENT_LOOT_TABLES - FRAGMENT_LOOT_TABLES is a property of the Disc class. It is a list of
 * strings that represents the fragment loot tables associated with the disc.
 */
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