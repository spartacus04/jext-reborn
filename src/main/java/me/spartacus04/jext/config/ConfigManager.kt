package me.spartacus04.jext.config

import com.google.gson.GsonBuilder
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class ConfigManager {
    companion object {
        private lateinit var configFile: File
        private lateinit var discsFile: File

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

        private fun <T> deserialize(file: File, type: Class<T>) : T {
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

            ConfigData.CONFIG = deserialize(configFile, Config::class.java)
            ConfigData.DISCS = deserialize(discsFile, List::class.java as Class<List<Disc>>)
        }
    }
}