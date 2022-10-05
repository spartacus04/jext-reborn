package me.spartacus04.jext.config

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

data class V1Config (
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
) {
    companion object {
        private fun fromJson(json: String): V1Config {
            val gson = GsonBuilder().setPrettyPrinting().setLenient().create()

            return gson.fromJson(json, V1Config::class.java)
        }

        fun isOldConfig(jsonConfig: String) : Boolean {
            return jsonConfig.contains("\"resource-pack-decline-kick-message\"") &&
                    jsonConfig.contains("\"failed-download-kick-message\"")
        }

        fun migrateToNewConfig(jsonConfig: String, plugin: JavaPlugin) : String {
            val oldconfig = fromJson(jsonConfig)

            return plugin.getResource("config.json")!!.bufferedReader().use {
                val text = it.readText()

                return@use text
                    .replace("\"force-resource-pack\": true", "\"force-resource-pack\": ${oldconfig.FORCE_RESOURCE_PACK}")
                    .replace("\"ignore-failed-download\": false", "\"ignore-failed-download\": ${oldconfig.IGNORE_FAILED_DOWNLOAD}")
                    .replace("\"allow-music-overlapping\": false", "\"allow-music-overlapping\": ${oldconfig.ALLOW_MUSIC_OVERLAPPING}")
            }
        }
    }
}

data class V2Config (
    @SerializedName("lang")
    var LANGUAGE_FILE: String,

    @SerializedName("force-resource-pack")
    var FORCE_RESOURCE_PACK : Boolean,

    @SerializedName("ignore-failed-download")
    var IGNORE_FAILED_DOWNLOAD : Boolean,

    @SerializedName("allow-music-overlapping")
    var ALLOW_MUSIC_OVERLAPPING : Boolean,
) {
    companion object {
        private fun fromJson(json: String): V1Config {
            val gson = GsonBuilder().setPrettyPrinting().setLenient().create()

            return gson.fromJson(json, V1Config::class.java)
        }

        fun isOldConfig(jsonConfig: String) : Boolean {
            return !jsonConfig.contains("\"allow-metrics\"")
        }

        fun migrateToNewConfig(jsonConfig: String, plugin: JavaPlugin) : String {
            val oldconfig = fromJson(jsonConfig)

            return plugin.getResource("config.json")!!.bufferedReader().use {
                val text = it.readText()

                return@use text
                    .replace("\"force-resource-pack\": true", "\"force-resource-pack\": ${oldconfig.FORCE_RESOURCE_PACK}")
                    .replace("\"ignore-failed-download\": false", "\"ignore-failed-download\": ${oldconfig.IGNORE_FAILED_DOWNLOAD}")
                    .replace("\"allow-music-overlapping\": false", "\"allow-music-overlapping\": ${oldconfig.ALLOW_MUSIC_OVERLAPPING}")
            }
        }
    }
}

data class V1Lang (
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
){
    companion object {
        fun isOldConfig(jsonConfig: String) : Boolean {
            return !jsonConfig.contains("\"reloaded\"")
        }

        fun migrateToNewConfig(jsonConfig: String) : String {
            return jsonConfig.replace("\n}", ",\n\t\"reloaded\": \"§6Reloaded configuration!\"\n}")
        }
    }
}

class ConfigVersionManager {
    companion object {
        fun updateConfig(file: File, plugin: JavaPlugin) {
            // Read as text
            val jsonConfig = file.readText()

            if(V1Config.isOldConfig(jsonConfig)) {
                return file.writeText(V1Config.migrateToNewConfig(jsonConfig, plugin))
            }

            if(V2Config.isOldConfig(jsonConfig)) {
                return file.writeText(V2Config.migrateToNewConfig(jsonConfig, plugin))
            }
        }

        fun updateLang(file: File) {
            val jsonConfig = file.readText()

            if(V1Lang.isOldConfig(jsonConfig)) {
                file.writeText(V1Lang.migrateToNewConfig(jsonConfig))
            }
        }
    }
}