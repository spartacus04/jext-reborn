package me.spartacus04.jext.config

import com.google.gson.annotations.SerializedName

/**
 * The Config data class represents a configuration object with various properties for the plugin.
 * @property {String} LANGUAGE_MODE - The `LANGUAGE_MODE` property represents the language mode for the configuration.
 * @property {Boolean} FORCE_RESOURCE_PACK - A boolean value indicating whether to force the resource pack to be used.
 * @property {Boolean} IGNORE_FAILED_DOWNLOAD - The property IGNORE_FAILED_DOWNLOAD is a boolean value that determines
 * whether to ignore failed downloads or not. If set to true, it means that failed downloads will be ignored and a player
 * will be able to join even if FORCE_RESOURCE_PACK is set to true.
 * @property {Boolean} ALLOW_MUSIC_OVERLAPPING - The property ALLOW_MUSIC_OVERLAPPING is a boolean value that determines
 * whether music tracks can overlap or not. If set to true, music tracks can play simultaneously. If set to false, music
 * tracks will not overlap and will stop playing before a new track starts.
 * @property {Boolean} ALLOW_METRICS - The ALLOW_METRICS property is a boolean value that determines whether or not metrics
 * are allowed. If set to true, metrics are allowed; if set to false, metrics are not allowed.
 * @property {String} JUKEBOX_BEHAVIOUR - The property "JUKEBOX_BEHAVIOUR" represents the behavior of a jukebox.
 * It can either be "vanilla", "legacy-gui", or "gui".
 * @property {Int} DISCS_RANDOM_CHANCE - DISCS_RANDOM_CHANCE is an integer property that represents the chance of obtaining
 * a disc randomly in a chest.
 * @property {Int} FRAGMENTS_RANDOM_CHANCE - FRAGMENTS_RANDOM_CHANCE is an integer property that represents the chance of
 * obtaining a fragment in a chest.
 * @property {HashMap<String, Int>} DISC_LIMIT - DISC_LIMIT is a HashMap that stores the limit for each disc in the loot
 * tables. The keys in the HashMap are the disc names (as strings) and the values are the corresponding limits (as
 * integers).
 * @property {HashMap<String, Int>} FRAGMENT_LIMIT - The `FRAGMENT_LIMIT` property is a HashMap that stores the limit for
 * each fragment loottable. The key of the HashMap is a String representing the loottable name, and the value is an Int
 * representing the limit for that loottable.
 */
data class Config (
    @SerializedName("lang")
    var LANGUAGE_MODE: String,

    @SerializedName("force-resource-pack")
    var FORCE_RESOURCE_PACK : Boolean,

    @SerializedName("ignore-failed-download")
    var IGNORE_FAILED_DOWNLOAD : Boolean,

    @SerializedName("allow-music-overlapping")
    var ALLOW_MUSIC_OVERLAPPING : Boolean,

    @SerializedName("allow-metrics")
    var ALLOW_METRICS : Boolean,

    @SerializedName("jukebox-behaviour")
    var JUKEBOX_BEHAVIOUR : String,

    @SerializedName("discs-random-chance")
    var DISCS_RANDOM_CHANCE : Int,

    @SerializedName("fragments-random-chance")
    var FRAGMENTS_RANDOM_CHANCE : Int,

    @SerializedName("disc-loottables-limit")
    var DISC_LIMIT : HashMap<String, Int>,

    @SerializedName("fragment-loottables-limit")
    var FRAGMENT_LIMIT : HashMap<String, Int>,
)