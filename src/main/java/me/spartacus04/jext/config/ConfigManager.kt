package me.spartacus04.jext.config

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.spartacus04.jext.jukebox.JukeboxContainer
import me.spartacus04.jext.jukebox.legacy.LegacyJukeboxContainer
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileNotFoundException
import java.lang.reflect.Type

/**
 * Config manager is a class used to load the plugin's config.
 *
 * @constructor The class is a singleton, so the constructor is private.
 */
internal class ConfigManager private constructor() {
    companion object {
        /**
         * The configFile property stores the plugin's config file.
         */
        private lateinit var configFile: File
        /**
         * The discsFile property stores the plugin's discs file.
         */
        private lateinit var discsFile: File

        /**
         * The function `createDefaultConfig` creates the default config file.
         *
         * @param plugin The plugin instance used to create the config file.
         */
        private fun createDefaultConfig(plugin: JavaPlugin) {
            if(!plugin.dataFolder.exists()) plugin.dataFolder.mkdirs()

            if(!configFile.exists()) {
                configFile.createNewFile()

                plugin.getResource("config.json")!!.bufferedReader().use {
                    configFile.writeText(it.readText())
                }
            }
        }

        /**
         * The function `deserialize` deserializes a file.
         *
         * @param file The file to deserialize.
         * @param type The type of the file.
         * @return The deserialized file.
         */
        private fun <T> deserialize(file: File, type: Type) : T {
            val gson = GsonBuilder().setLenient().setPrettyPrinting().create()

            return gson.fromJson(
                file.readText(),
                type
            )
        }

        /**
         * The function `load` loads the plugin's config.
         *
         * @param plugin The plugin instance used to load the config.
         */
        fun load(plugin: JavaPlugin) {
            configFile = plugin.dataFolder.resolve("config.json")
            discsFile = plugin.dataFolder.resolve("discs.json")

            if(!configFile.exists()) createDefaultConfig(plugin)
            if(!discsFile.exists()) {
                Bukkit.getConsoleSender().sendMessage(LanguageManager.DISCS_NOT_FOUND)

                throw FileNotFoundException("discs.json file not found!")
            }

            val configType = object : TypeToken<Config>() {}.type
            val discsType = object : TypeToken<List<Disc>>() {}.type

            ConfigVersionManager.updateConfig(configFile, plugin)
            ConfigData.CONFIG = deserialize(configFile, configType)

            ConfigVersionManager.updateDiscs(discsFile)
            ConfigData.DISCS = deserialize(discsFile, discsType)

            LegacyJukeboxContainer.reload(plugin)
            JukeboxContainer.loadFromFile()
        }
    }
}