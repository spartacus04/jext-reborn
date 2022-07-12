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

class ConfigVersionManager {
    companion object {
        fun updateConfig(file: File, plugin: JavaPlugin) {
            // Read as text
            val jsonConfig = file.readText()

            if(V1Config.isOldConfig(jsonConfig)) {
                file.writeText(V1Config.migrateToNewConfig(jsonConfig, plugin))
            }
        }
    }
}