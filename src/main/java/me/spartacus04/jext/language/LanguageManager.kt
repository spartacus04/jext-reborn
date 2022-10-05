package me.spartacus04.jext.language

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.spartacus04.jext.config.ConfigVersionManager
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*
import java.util.function.Consumer
import java.util.jar.JarEntry
import java.util.jar.JarFile
import kotlin.collections.HashMap


class LanguageManager(private val autoMode : Boolean, private val plugin: JavaPlugin) {
    private val loadedLanguageMap = HashMap<String, Map<String, String>>()

    private lateinit var gson : Gson

    init {
        if(autoMode) {
            // Uses languages translated by crowdin
            gson = GsonBuilder().setLenient().setPrettyPrinting().create()

            val path = "langs"

            JarFile(File(javaClass.protectionDomain.codeSource.location.path)).use {
                val entries: Enumeration<JarEntry> = it.entries() //gives ALL entries in jar
                while (entries.hasMoreElements()) {
                    val element = entries.nextElement()
                    if (element.name.startsWith("$path/")) {
                        val langName = element.name.replaceFirst("$path/", "")

                        plugin.getResource("langs/$langName")!!.bufferedReader().use {file ->
                            val mapType = object : TypeToken<Map<String, String>>() {}.type
                            val languageMap : Map<String, String> = gson.fromJson(file.readText(), mapType)

                            loadedLanguageMap.put(langName.replace(".json", ""), languageMap)
                        }
                    }
                }
            }
        }
        else {
            // Lets the server owner set a custom file
            val customFile = plugin.dataFolder.resolve("lang.json")

            if(!customFile.exists()) {
                customFile.createNewFile()

                plugin.getResource("langs/en_us.json")!!.bufferedReader().use {
                    customFile.writeText(it.readText())
                }
            }

            ConfigVersionManager.updateLang(customFile)
            customFile.bufferedReader().use {
                val mapType = object : TypeToken<Map<String, String>>() {}.type
                val languageMap : Map<String, String> = gson.fromJson(it.readText(), mapType)

                loadedLanguageMap.put("custom", languageMap)
            }
        }
    }

    fun getString(commandSender: CommandSender, key : String) : String {
        if(!autoMode) {
            return loadedLanguageMap["custom"]!![key]!!
        }

        val locale = if (commandSender is Player) {
            commandSender.locale
        }
        else {
            "en_us"
        }

        return if(loadedLanguageMap.containsKey(locale)) {
            loadedLanguageMap[locale]!![key]!!
        }
        else {
            loadedLanguageMap["en_us"]!![key]!!
        }
    }

    fun format(commandSender: CommandSender, key: String) : String {
        return "[§aJEXT§f] ${getString(commandSender, key)}"
    }
}
