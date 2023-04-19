package me.spartacus04.jext.config

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.spartacus04.jext.disc.DiscPlayer
import me.spartacus04.jext.jukebox.JukeboxContainer
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.lang.reflect.Type

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
        }

        private fun <T> deserialize(file: File, type: Type) : T {
            val gson = GsonBuilder().setLenient().setPrettyPrinting().create()

            return gson.fromJson(
                file.readText(),
                type
            )
        }

        fun load(plugin: JavaPlugin) : Boolean {
            configFile = plugin.dataFolder.resolve("config.json")
            discsFile = plugin.dataFolder.resolve("discs.json")

            defaultConfig(plugin)
            if(!discsFile.exists()) return false

            val configType = object : TypeToken<Config>() {}.type
            val discsType = object : TypeToken<List<Disc>>() {}.type

            ConfigVersionManager.updateConfig(configFile, plugin)
            ConfigData.CONFIG = deserialize(configFile, configType)

            ConfigVersionManager.updateDiscs(discsFile)
            ConfigData.DISCS = deserialize(discsFile, discsType)

            JukeboxContainer.reload(plugin)
            DiscPlayer.plugin = plugin

            return true
        }
    }
}