package me.spartacus04.jext.config

import com.google.gson.annotations.SerializedName

/**
 * Config is a data class used to store the plugin's config.
 *
 * @property LANGUAGE_MODE Can be set to `auto`, `custom`, `silent` or a custom locale. If set to auto the plugin will display itself to the player in his language locale (for example if someone is playing in italian, the plugin messages are going to be in italian for him). The console locale is always set to `en_us`. If set to custom the plugin will generate a language file in his config, which can be edited by the user to make custom messages. These custom language is applied to all players regardless of their locale (except for console). If set to silent the plugin won't display any messages in chat. If set to a custom locale each player will see the plugin in that language regardless of their locale
 * @property FORCE_RESOURCE_PACK If set to true the plugin will kick the player if they reject the server resource pack.
 * @property IGNORE_FAILED_DOWNLOAD If set to true the plugin will kick the player if the resource pack download fails.
 * @property ALLOW_MUSIC_OVERLAPPING If set to true and a player tries to play a record near another jukebox that's already playing custom music, the second jukebox won't play any music.
 * @property ALLOW_METRICS If set to true anonymous stats about your server will be sent to the bstats api. Please don't disable this, as watching how many people use the plugin helps me stay motivated
 * @property JUKEBOX_BEHAVIOUR When set to gui or legacy-gui it will add a custom jukebox gui when clicking on a jukebox.
 * @property DISCS_RANDOM_CHANCE Can be set to an integer value from 1 to 1000, sets the chance for custom discs to spawn in a dungeon
 * @property FRAGMENTS_RANDOM_CHANCE Can be set to an integer value from 1 to 1000, sets the chance for custom fragments to spawn in a dungeon
 * @property DISC_LIMIT Sets how many discs can be found in a certain loottable. chests/ * sets the chance for all discs. If none is set the default value is 2
 * @property FRAGMENT_LIMIT Sets how many discs can be found in a certain loottable. chests/ ì* sets the chance for all discs. If none is set the default value is 3
 * @constructor Creates a new Config object
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