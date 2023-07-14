package me.spartacus04.jext.config

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import me.spartacus04.jext.config.LanguageManager.Companion.MUSIC_DISC_FORMAT_OLD
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

//region Config
internal data class V3Config (
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
            return !jsonConfig.contains("\"jukebox-gui\"") && !jsonConfig.contains("\"disc-loottables-limit\"")
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

internal data class V4Config (
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

    @SerializedName("jukebox-gui")
    var JUKEBOX_GUI : Boolean
) {
    companion object {
        private fun fromJson(json: String): V4Config {
            val gson = GsonBuilder().setPrettyPrinting().setLenient().create()

            return gson.fromJson(json, V4Config::class.java)
        }

        fun isOldConfig(jsonConfig: String): Boolean {
            return !jsonConfig.contains("\"disc-loottables-limit\"") ||
                    !jsonConfig.contains("\"fragment-loottables-limit\"") ||
                    !jsonConfig.contains("\"discs-random-chance\"") ||
                    !jsonConfig.contains("\"fragments-random-chance\"")
        }

        fun migrateToNewConfig(jsonConfig: String, plugin: JavaPlugin): String {
            val oldconfig = fromJson(jsonConfig)

            return plugin.getResource("config.json")!!.bufferedReader().use {
                val text = it.readText()

                return@use text
                    .replace("\"lang\": \"auto\"", "\"lang\": \"${oldconfig.LANGUAGE_MODE}\"")
                    .replace(
                        "\"force-resource-pack\": true",
                        "\"force-resource-pack\": ${oldconfig.FORCE_RESOURCE_PACK}"
                    )
                    .replace(
                        "\"ignore-failed-download\": false",
                        "\"ignore-failed-download\": ${oldconfig.IGNORE_FAILED_DOWNLOAD}"
                    )
                    .replace(
                        "\"allow-music-overlapping\": false",
                        "\"allow-music-overlapping\": ${oldconfig.ALLOW_MUSIC_OVERLAPPING}"
                    )
                    .replace("\"allow-metrics\": true", "\"allow-metrics\": ${oldconfig.ALLOW_METRICS}")
                    .replace(
                        "\"jukebox-behaviour\": \"vanilla\"",
                        "\"jukebox-behaviour\": \"${if(oldconfig.JUKEBOX_GUI) "legacy-gui" else "vanilla"}\""
                    )
            }
        }
    }
}

internal data class V5Config (
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

    @SerializedName("jukebox-gui")
    var JUKEBOX_GUI : Boolean,

    @SerializedName("discs-random-chance")
    var DISCS_RANDOM_CHANCE : Int,

    @SerializedName("fragments-random-chance")
    var FRAGMENTS_RANDOM_CHANCE : Int,

    @SerializedName("disc-loottables-limit")
    var DISC_LIMIT : HashMap<String, Int>,

    @SerializedName("fragment-loottables-limit")
    var FRAGMENT_LIMIT : HashMap<String, Int>,
) {
    companion object {
        private fun fromJson(json: String): V5Config {
            val gson = GsonBuilder().setPrettyPrinting().setLenient().create()

            return gson.fromJson(json, V5Config::class.java)
        }

        fun isOldConfig(jsonConfig: String): Boolean {
            return !jsonConfig.contains("\"jukebox-behaviour\"")
        }

        fun migrateToNewConfig(jsonConfig: String, plugin: JavaPlugin): String {
            val oldconfig = fromJson(jsonConfig)

            return plugin.getResource("config.json")!!.bufferedReader().use {
                val text = it.readText()

                return@use text.replace("\"lang\": \"auto\"", "\"lang\": \"${oldconfig.LANGUAGE_MODE}\"")
                    .replace(
                        "\"force-resource-pack\": true",
                        "\"force-resource-pack\": ${oldconfig.FORCE_RESOURCE_PACK}"
                    )
                    .replace(
                        "\"ignore-failed-download\": false",
                        "\"ignore-failed-download\": ${oldconfig.IGNORE_FAILED_DOWNLOAD}"
                    )
                    .replace(
                        "\"allow-music-overlapping\": false",
                        "\"allow-music-overlapping\": ${oldconfig.ALLOW_MUSIC_OVERLAPPING}"
                    )
                    .replace("\"allow-metrics\": true", "\"allow-metrics\": ${oldconfig.ALLOW_METRICS}")
                    .replace(
                        "\"jukebox-behaviour\": \"vanilla\"",
                        "\"jukebox-behaviour\": \"${if(oldconfig.JUKEBOX_GUI) "legacy-gui" else "vanilla"}\""
                    )
            }
        }
    }
}
//endregion

//region Discs
internal data class V1Disc(
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

internal data class V2Disc(
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
            return !jsonConfig.contains("duration") || !jsonConfig.contains("fragment-loot-tables")
        }
    }
}

/**
 * Config version manager
 *
 * @constructor Create empty Config version manager
 *///endregion
/**
 * The config version manager is used to update the config file to the latest version
 *
 * @constructor The class is private because it is a singleton
 */
internal class ConfigVersionManager private constructor() {
    companion object {
        /**
         * Updates the config file to the latest version
         *
         * @param file The config file
         * @param plugin The plugin instance
         */
        fun updateConfig(file: File, plugin: JavaPlugin) {
            // Read as text
            val jsonConfig = file.readText()

            if(V3Config.isOldConfig(jsonConfig)) {
                return file.writeText(V3Config.migrateToNewConfig(jsonConfig, plugin))
            }

            if (V4Config.isOldConfig(jsonConfig)) {
                return file.writeText(V4Config.migrateToNewConfig(jsonConfig, plugin))
            }

            if (V5Config.isOldConfig(jsonConfig)) {
                return file.writeText(V5Config.migrateToNewConfig(jsonConfig, plugin))
            }
        }

        /**
         * Checks if the config file is outdated
         *
         * @param file The discs file
         */
        fun updateDiscs(file: File) {
            val jsonConfig = file.readText()

            if(V1Disc.isOldConfig(jsonConfig)) {
                Bukkit.getConsoleSender().sendMessage(MUSIC_DISC_FORMAT_OLD)
            }

            if(V2Disc.isOldConfig(jsonConfig)) {
                Bukkit.getConsoleSender().sendMessage(MUSIC_DISC_FORMAT_OLD)
            }
        }

        /**
         * Updates the language file to the latest version
         *
         * @param file The language file
         * @param lang The language map
         */
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