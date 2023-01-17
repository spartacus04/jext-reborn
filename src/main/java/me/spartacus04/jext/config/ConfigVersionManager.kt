package me.spartacus04.jext.config

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import org.bukkit.Bukkit
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
        private fun fromJson(json: String): V2Config {
            val gson = GsonBuilder().setPrettyPrinting().setLenient().create()

            return gson.fromJson(json, V2Config::class.java)
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

data class V3Config (
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
) {
    companion object {
        private fun fromJson(json: String): V3Config {
            val gson = GsonBuilder().setPrettyPrinting().setLenient().create()

            return gson.fromJson(json, V3Config::class.java)
        }

        fun isOldConfig(jsonConfig: String) : Boolean {
            return !jsonConfig.contains("\"jukebox-gui\"")
        }

        fun migrateToNewConfig(jsonConfig: String, plugin: JavaPlugin) : String {
            val oldconfig = fromJson(jsonConfig)

            return plugin.getResource("config.json")!!.bufferedReader().use {
                val text = it.readText()

                return@use text
                    .replace("\"lang\": \"en\"", "\"lang\": \"${oldconfig.LANGUAGE_MODE}\"")
                    .replace("\"force-resource-pack\": true", "\"force-resource-pack\": ${oldconfig.FORCE_RESOURCE_PACK}")
                    .replace("\"ignore-failed-download\": false", "\"ignore-failed-download\": ${oldconfig.IGNORE_FAILED_DOWNLOAD}")
                    .replace("\"allow-music-overlapping\": false", "\"allow-music-overlapping\": ${oldconfig.ALLOW_MUSIC_OVERLAPPING}")
                    .replace("\"allow-metrics\": true", "\"allow-metrics\": ${oldconfig.ALLOW_METRICS}")
            }
        }
    }
}

data class V1Disc(
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
) {
    companion object {
        fun isOldConfig(jsonConfig: String) : Boolean {
            return !jsonConfig.contains("loot-tables")
        }
    }
}

data class V2Disc(
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
    var LORE: List<String>,

    @SerializedName("loot-tables")
    val LOOT_TABLES: List<String>?
) {
    companion object {
        fun isOldConfig(jsonConfig: String) : Boolean {
            return !jsonConfig.contains("duration")
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

            if(V3Config.isOldConfig(jsonConfig)) {
                return file.writeText(V3Config.migrateToNewConfig(jsonConfig, plugin))
            }
        }

        fun updateDiscs(file: File) {
            val jsonConfig = file.readText()

            if(V1Disc.isOldConfig(jsonConfig)) {
                Bukkit.getConsoleSender().sendMessage(
                    "[§aJEXT§f] music disc format is old, you can update it by importing and re-exporting the resource pack in the generator\n§6[§2https://spartacus04.github.io/jext-reborn/§6]"
                )
            }

            if(V2Disc.isOldConfig(jsonConfig)) {
                Bukkit.getConsoleSender().sendMessage(
                    "[§aJEXT§f] music disc format is old, you can update it by importing and re-exporting the resource pack in the generator\n§6[§2https://spartacus04.github.io/jext-reborn/§6]"
                )
            }
        }

        fun updateLang(file: File, lang: Map<String, String>) {
            val gson = GsonBuilder().setLenient().setPrettyPrinting().create()
            val jsonConfig = file.readText()

            val mapType = object : TypeToken<HashMap<String, String>>() {}.type
            val languageMap = gson.fromJson<HashMap<String, String>>(jsonConfig, mapType)

            lang.keys.forEach {
                if(!languageMap.containsKey(it)) {
                    languageMap[it] = lang[it]!!
                }
            }

            file.writeText(gson.toJson(languageMap))
        }
    }
}