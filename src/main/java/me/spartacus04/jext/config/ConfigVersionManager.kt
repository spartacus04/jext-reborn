package me.spartacus04.jext.config

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import me.spartacus04.jext.config.LanguageManager.Companion.MUSIC_DISC_FORMAT_OLD
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

//region Config
/**
 * The above code defines a data class called V3Config, which represents an old configuration object with various
 * properties and methods for handling JSON configuration data.
 * @property {String} LANGUAGE_MODE - The `LANGUAGE_MODE` property represents the language mode for the configuration. It
 * is a string value that determines the language used in the configuration.
 * @property {Boolean} FORCE_RESOURCE_PACK - A boolean value indicating whether to force the resource pack to be used.
 * @property {Boolean} IGNORE_FAILED_DOWNLOAD - This property determines whether failed downloads should be ignored or not.
 * If set to true, failed downloads will be ignored. If set to false, failed downloads will not be ignored.
 * @property {Boolean} ALLOW_MUSIC_OVERLAPPING - A boolean value indicating whether music overlapping is allowed or not.
 * @property {Boolean} ALLOW_METRICS - A boolean value indicating whether metrics are allowed or not.
 */
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
        /**
         * The function `fromJson` takes a JSON string as input and uses Gson library to deserialize it into an instance of
         * the `V3Config` class.
         *
         * @param json The `json` parameter is a string that represents a JSON object.
         * @return an instance of the `V3Config` class.
         */
        private fun fromJson(json: String): V3Config {
            val gson = GsonBuilder().setPrettyPrinting().setLenient().create()

            return gson.fromJson(json, V3Config::class.java)
        }

        /**
         * The function checks if a given JSON configuration string does not contain specific keys.
         *
         * @param jsonConfig The `jsonConfig` parameter is a string that represents a JSON configuration.
         * @return a boolean value.
         */
        fun isOldConfig(jsonConfig: String) : Boolean {
            return !jsonConfig.contains("\"jukebox-gui\"") && !jsonConfig.contains("\"disc-loottables-limit\"")
        }

        /**
         * The function `migrateToNewConfig` takes a JSON configuration string and a JavaPlugin object, and replaces
         * specific values in a text file with values from the JSON configuration.
         *
         * @param jsonConfig The `jsonConfig` parameter is a string representation of a JSON object that contains the old
         * configuration values. It is used to retrieve the old configuration values that need to be migrated to the new
         * configuration.
         * @param plugin The `plugin` parameter is an instance of the `JavaPlugin` class. It represents a plugin in a
         * Java-based application or framework. It provides various methods and resources related to the plugin, such as
         * accessing plugin resources, managing configuration files, and interacting with the application or framework.
         * @return The function `migrateToNewConfig` returns a string.
         */
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

/**
 * The above code defines a data class called V4Config with properties and methods for handling JSON
 * configuration.
 * @property {String} LANGUAGE_MODE - The `LANGUAGE_MODE` property represents the language mode for the V4Config. It is a
 * String value that determines the language used by the configuration.
 * @property {Boolean} FORCE_RESOURCE_PACK - A boolean value indicating whether to force the resource pack.
 * @property {Boolean} IGNORE_FAILED_DOWNLOAD - The property `IGNORE_FAILED_DOWNLOAD` is a boolean that determines whether
 * to ignore failed downloads or not. If set to `true`, it means that failed downloads will be ignored, and if set to
 * `false`, it means that failed downloads will not be ignored.
 * @property {Boolean} ALLOW_MUSIC_OVERLAPPING - The property `ALLOW_MUSIC_OVERLAPPING` is a boolean value that determines
 * whether music overlapping is allowed or not. If it is set to `true`, music can overlap. If it is set to `false`, music
 * cannot overlap.
 * @property {Boolean} ALLOW_METRICS - The `ALLOW_METRICS` property is a boolean value that determines whether metrics
 * tracking is allowed or not. If it is set to `true`, metrics tracking is enabled. If it is set to `false`, metrics
 * tracking is disabled.
 * @property {Boolean} JUKEBOX_GUI - JUKEBOX_GUI is a boolean property that represents whether the jukebox behavior is set
 * to "legacy-gui" or "vanilla". If JUKEBOX_GUI is true, it means the jukebox behavior is set to "legacy-gui", otherwise it
 * is set to "
 */
data class V4Config (
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
        /**
         * The function `fromJson` takes a JSON string as input and uses Gson library to deserialize it into an instance of
         * the `V4Config` class.
         *
         * @param json The `json` parameter is a string that represents a JSON object. It is the input that will be
         * deserialized into a `V4Config` object.
         * @return an instance of the `V4Config` class.
         */
        private fun fromJson(json: String): V4Config {
            val gson = GsonBuilder().setPrettyPrinting().setLenient().create()

            return gson.fromJson(json, V4Config::class.java)
        }

        /**
         * The function checks if a given JSON configuration string is missing certain key-value pairs.
         *
         * @param jsonConfig A string representing a JSON configuration.
         * @return a boolean value.
         */
        fun isOldConfig(jsonConfig: String): Boolean {
            return !jsonConfig.contains("\"disc-loottables-limit\"") ||
                    !jsonConfig.contains("\"fragment-loottables-limit\"") ||
                    !jsonConfig.contains("\"discs-random-chance\"") ||
                    !jsonConfig.contains("\"fragments-random-chance\"")
        }

        /**
         * The function `migrateToNewConfig` takes a JSON configuration string and a JavaPlugin object, and replaces
         * specific values in the JSON string with values from the old configuration.
         *
         * @param jsonConfig The `jsonConfig` parameter is a string representation of a JSON configuration file. It
         * contains the configuration settings that need to be migrated to a new format.
         * @param plugin The `plugin` parameter is an instance of the `JavaPlugin` class, which represents a plugin in a
         * Java-based application or framework. It provides various methods and resources related to the plugin, such as
         * accessing files and resources within the plugin's directory.
         * @return The function `migrateToNewConfig` returns a modified version of the `config.json` file.
         */
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

/**
 * The above code defines a data class called V5Config, which represents a configuration object with various
 * properties and methods for migration.
 * @property {String} LANGUAGE_MODE - The `LANGUAGE_MODE` property represents the language mode for the V5Config. It is a
 * String value that determines the language used in the configuration.
 * @property {Boolean} FORCE_RESOURCE_PACK - A boolean value indicating whether to force the resource pack.
 * @property {Boolean} IGNORE_FAILED_DOWNLOAD - The property IGNORE_FAILED_DOWNLOAD is a boolean value that determines
 * whether to ignore failed downloads or not. If set to true, the program will ignore any failed downloads. If set to
 * false, the program will not ignore failed downloads and will handle them accordingly.
 * @property {Boolean} ALLOW_MUSIC_OVERLAPPING - The property ALLOW_MUSIC_OVERLAPPING is a boolean value that determines
 * whether music overlapping is allowed or not. If set to true, music tracks can overlap each other. If set to false, music
 * tracks will not overlap and will stop playing before a new track starts.
 * @property {Boolean} ALLOW_METRICS - The `ALLOW_METRICS` property is a boolean value that determines whether metrics
 * tracking is allowed or not. If set to `true`, metrics tracking is enabled. If set to `false`, metrics tracking is
 * disabled.
 * @property {Boolean} JUKEBOX_GUI - JUKEBOX_GUI is a boolean property that represents whether the jukebox behavior is set
 * to use the legacy GUI or the vanilla behavior. If JUKEBOX_GUI is true, it means the legacy GUI is enabled. If
 * JUKEBOX_GUI is false, it means the vanilla behavior
 * @property {Int} DISCS_RANDOM_CHANCE - The `DISCS_RANDOM_CHANCE` property is an integer value that represents the chance
 * of obtaining a disc item in the game. It is used to determine the probability of getting a disc when certain conditions
 * are met.
 * @property {Int} FRAGMENTS_RANDOM_CHANCE - FRAGMENTS_RANDOM_CHANCE is an integer property that represents the chance of
 * obtaining a fragment in the game.
 * @property {HashMap<String, Int>} DISC_LIMIT - DISC_LIMIT is a HashMap that stores the limit for each disc loottable. The
 * key is a String representing the disc name, and the value is an Int representing the limit for that disc.
 * @property {HashMap<String, Int>} FRAGMENT_LIMIT - FRAGMENT_LIMIT is a HashMap that stores the limit for each fragment
 * loottable. The key of the HashMap is a String representing the loottable name, and the value is an Int representing the
 * limit for that loottable.
 */
data class V5Config (
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
        /**
         * The function `fromJson` takes a JSON string as input and uses Gson library to deserialize it into an instance of
         * the `V5Config` class.
         *
         * @param json The `json` parameter is a string that represents a JSON object. It is the input that will be
         * converted to a `V5Config` object.
         * @return an instance of the `V5Config` class.
         */
        private fun fromJson(json: String): V5Config {
            val gson = GsonBuilder().setPrettyPrinting().setLenient().create()

            return gson.fromJson(json, V5Config::class.java)
        }

        /**
         * The function checks if a given JSON configuration string contains a specific key.
         *
         * @param jsonConfig A string representing a JSON configuration.
         * @return The function isOldConfig is returning a boolean value.
         */
        fun isOldConfig(jsonConfig: String): Boolean {
            return !jsonConfig.contains("\"jukebox-behaviour\"")
        }

        /**
         * The function `migrateToNewConfig` takes a JSON configuration string and a JavaPlugin object, and replaces
         * specific values in the JSON string with values from the old configuration.
         *
         * @param jsonConfig A JSON string representing the old configuration.
         * @param plugin The `plugin` parameter is an instance of the `JavaPlugin` class, which represents a plugin in a
         * Java-based application or framework. It provides various methods and resources related to the plugin, such as
         * accessing files and resources within the plugin's directory.
         * @return The function `migrateToNewConfig` returns a modified version of the `config.json` file, with certain
         * values replaced based on the values in the `oldconfig` object.
         */
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
/**
 * The above code defines a data class called V1Disc, which represents a disc with various properties such as
 * title, author, disc namespace, model data, creeper drop, and lores.
 * @property {String} TITLE - The title of the disc.
 * @property {String} AUTHOR - The `AUTHOR` property represents the author of the disc.
 * @property {String} DISC_NAMESPACE - The `DISC_NAMESPACE` property represents the namespace of the disc. In the context
 * of this code, it is a string that identifies the namespace of the disc.
 * @property {Int} MODEL_DATA - The `MODEL_DATA` property in the `V1Disc` class represents the model data of the disc. It
 * is of type `Int`, which means it can hold integer values.
 * @property {Boolean} CREEPER_DROP - A boolean value indicating whether the disc can be dropped by creepers.
 * @property {List<String>} LORE - LORE is a list of strings that represents the lore of the V1Disc. Lore is additional
 * information or backstory about the disc that can be displayed in the game.
 */
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
        /**
         * The function checks if a JSON configuration string contains the "loot-tables" keyword.
         *
         * @param jsonConfig A string representing a JSON configuration.
         * @return The function isOldConfig is returning a boolean value.
         */
        fun isOldConfig(jsonConfig: String) : Boolean {
            return !jsonConfig.contains("loot-tables")
        }
    }
}

/**
 * The above code defines a data class in Kotlin called V2Disc, which represents a disc with various properties such as
 * title, author, disc namespace, model data, creeper drop, lores, and loot tables.
 * @property {String} TITLE - The `TITLE` property represents the title of the disc.
 * @property {String} AUTHOR - The `AUTHOR` property represents the author of the disc.
 * @property {String} DISC_NAMESPACE - The `DISC_NAMESPACE` property represents the namespace of the disc. It is a string
 * value that identifies the category or group to which the disc belongs.
 * @property {Int} MODEL_DATA - The `MODEL_DATA` property is an integer that represents the model data of the disc. It is
 * used to differentiate between different variations or versions of the same disc.
 * @property {Boolean} CREEPER_DROP - A boolean value indicating whether the disc can be dropped by creepers.
 * @property {List<String>} LORE - LORE is a property of type List<String> which represents the list of lore lines for the
 * V2Disc.
 * @property {List<String>?} LOOT_TABLES - LOOT_TABLES is a list of strings that represents the loot tables associated with
 * the V2Disc.
 */
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
        /**
         * The function checks if a given JSON configuration string does not contain the keys "duration" or
         * "fragment-loot-tables".
         *
         * @param jsonConfig The `jsonConfig` parameter is a string that represents a JSON configuration.
         * @return The function isOldConfig is returning a boolean value.
         */
        fun isOldConfig(jsonConfig: String) : Boolean {
            return !jsonConfig.contains("duration") || !jsonConfig.contains("fragment-loot-tables")
        }
    }
}
//endregion
class ConfigVersionManager {
    companion object {
        /**
         * The function `updateConfig` reads a JSON config file as text and migrates it to a new version if it is an old
         * version.
         *
         * @param file The `file` parameter is of type `File` and represents the configuration file that needs to be
         * updated.
         * @param plugin The "plugin" parameter is an instance of the JavaPlugin class. It is used to access the plugin's
         * resources and other functionalities.
         * @return The code is returning the result of writing the migrated configuration to the file.
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
         * The function `updateDiscs` reads a JSON configuration file and checks if it matches the format of either V1Disc
         * or V2Disc, sending a message to the console if it is an old format.
         *
         * @param file A File object representing a JSON configuration file.
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
         * The function updates a language file by adding new key-value pairs from a given language map.
         *
         * @param file The `file` parameter is of type `File` and represents the file that contains the language
         * configuration in JSON format.
         * @param lang The `lang` parameter is a `Map<String, String>` that represents the language translations to be
         * updated. The keys of the map are the language keys, and the values are the corresponding translations.
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