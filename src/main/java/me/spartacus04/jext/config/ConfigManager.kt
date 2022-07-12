package me.spartacus04.jext.config

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.lang.reflect.Type

class ConfigManager {
    companion object {
        private lateinit var configFile: File
        private lateinit var discsFile: File
        private lateinit var langFile: File

        private fun defaultConfig(plugin: JavaPlugin) {
            if(!plugin.dataFolder.exists()) plugin.dataFolder.mkdirs()

            if(!configFile.exists()) {
                configFile.createNewFile()

                plugin.getResource("config.json")!!.bufferedReader().use {
                    configFile.writeText(it.readText())
                }
            }

            if(!discsFile.exists()) {
                println("discs.json file does not exist, generate it with the provided tool (TODO)")
                return plugin.server.pluginManager.disablePlugin(plugin)
            }
        }

        private fun <T> deserialize(file: File, type: Type) : T {
            val gson = GsonBuilder().setLenient().setPrettyPrinting().create()

            return gson.fromJson(
                file.readText(),
                type
            )
        }

        @Suppress("UNCHECKED_CAST")
        fun load(plugin: JavaPlugin) {
            configFile = plugin.dataFolder.resolve("config.json")
            discsFile = plugin.dataFolder.resolve("discs.json")

            defaultConfig(plugin)

            val configType = object : TypeToken<Config>() {}.type
            val discsType = object : TypeToken<List<Disc>>() {}.type
            val langType = object : TypeToken<Messages>() {}.type

            ConfigVersionManager.updateConfig(configFile, plugin)

            ConfigData.CONFIG = deserialize(configFile, configType)
            ConfigData.DISCS = deserialize(discsFile, discsType)

            langFile = plugin.dataFolder.resolve("${ConfigData.CONFIG.LANGUAGE_FILE}.json")

            if(!langFile.exists()) {
                langFile.createNewFile()

                plugin.getResource("EN_US.json")!!.bufferedReader().use {
                    langFile.writeText(it.readText())
                }
            }

            ConfigData.LANG = deserialize(langFile, langType)
        }
    }
}