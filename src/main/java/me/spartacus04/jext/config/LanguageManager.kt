package me.spartacus04.jext.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.spartacus04.jext.config.ConfigData.Companion.CONFIG
import me.spartacus04.jext.config.ConfigData.Companion.LANG
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import kotlin.collections.HashMap


class LanguageManager(private val plugin: JavaPlugin) {
    private val languageMap = HashMap<String, Map<String, String>>()
    private var gson : Gson = GsonBuilder().setLenient().setPrettyPrinting().create()

    init {
        // this loads all languages in the languagemap
        JarFile(File(javaClass.protectionDomain.codeSource.location.path).absolutePath.replace("%20", " ")).use {
            val entries: Enumeration<JarEntry> = it.entries() //gives ALL entries in jar
            while (entries.hasMoreElements()) {
                val element = entries.nextElement()
                if (element.name.startsWith("langs/") && element.name.endsWith(".json")) {
                    val langName = element.name.replaceFirst("langs/", "")

                    plugin.getResource("langs/$langName")!!.bufferedReader().use {file ->
                        val mapType = object : TypeToken<Map<String, String>>() {}.type
                        val languageMap : Map<String, String> = gson.fromJson(file.readText(), mapType)

                        this.languageMap.put(langName.replace(".json", "").lowercase(), languageMap)
                    }
                }
            }
        }

        if(CONFIG.LANGUAGE_MODE.lowercase() == "custom") {
            val customFile = plugin.dataFolder.resolve("lang.json")

            if (!customFile.exists()) {
                plugin.getResource("langs/en_US.json")!!.bufferedReader().use {
                    customFile.createNewFile()
                    customFile.writeText(it.readText())
                }
            }

            ConfigVersionManager.updateLang(customFile, languageMap["en_us"]!!)

            customFile.bufferedReader().use {
                val mapType = object : TypeToken<Map<String, String>>() {}.type
                val languageMap : Map<String, String> = gson.fromJson(it.readText(), mapType)

                this.languageMap.put("custom", languageMap)
            }
        }
    }

    fun replaceParameters(string: String, params: HashMap<String, String>) : String {
        var newString = string

        params.forEach { (key, value) ->
            newString = newString.replace("%$key%", value)
        }

        return newString
    }

    fun getKey(commandSender: CommandSender, key: String, params: HashMap<String, String> = HashMap()) : String {
        if(commandSender !is Player) {
            return LANG.replaceParameters(languageMap["en_us"]!![key]!!, params)
        }

        return when(CONFIG.LANGUAGE_MODE.lowercase()) {
            "auto" -> LANG.replaceParameters(languageMap[commandSender.locale]!![key]!!, params)
            "custom" -> LANG.replaceParameters(languageMap["custom"]!![key]!!, params)
            else -> {
                val lang = if(hasLanguage(commandSender.locale)) commandSender.locale
                else "en_us"

                return LANG.replaceParameters(languageMap[lang]!![key]!!, params)
            }
        }
    }

    fun hasLanguage(locale: String) = languageMap.containsKey(locale.lowercase())
    operator fun get(locale: String, key: String) = languageMap[locale.lowercase()]!![key]!!

    companion object {
        const val ENABLED_MESSAGE = "[§aJEXT§f]§a Enabled Jukebox Extended Reborn, Do Re Mi!"
        const val DISABLED_MESSAGE = "[§eJEXT§f]§e Disabled Jukebox Extended Reborn, Mi Re Do!"
        const val DISCS_NOT_FOUND = "[§cJEXT§f] §cDiscs.json file not found please provide it in the plugin directory\n§6[§2https://github.com/spartacus04/jext-reborn/wiki/How-to-set-up-the-plugin§6]"
        const val UPDATE_DETECTED = "[§aJEXT§f] A new update is available!"
        const val UPDATE_LINK = "§6[§2https://github.com/spartacus04/jext-reborn/releases/latest/§6]"
        const val CROWDIN_MESSAGE = "[§aJEXT§f] It looks like your language isn't in JEXT yet. Why not contribute and add it yourself here?"
        const val CROWDIN_LINK = "§6[§2https://crwd.in/jext-reborn§6]"
        const val VULNERABLE_MESSAGE = "[§cJEXT§f] §cSpigot version is outdated and is vulnerable to a crash exploit. Please update it."

        fun load(plugin: JavaPlugin) {
            LANG = LanguageManager(plugin)
        }
    }
}

fun CommandSender.sendJEXTMessage(key: String, params: HashMap<String, String> = HashMap()) {
    if(this !is Player) {
        return sendMessage(
            LANG.replaceParameters("[§aJEXT§f] ${LANG["en_us", key]}", params)
        )
    }

    when(CONFIG.LANGUAGE_MODE.lowercase()) {
        "auto" -> sendMessage(
            LANG.replaceParameters("[§aJEXT§f] ${LANG[this.locale, key]}", params)
        )
        "custom" -> sendMessage(
            LANG.replaceParameters("[§aJEXT§f] ${LANG["custom", key]}", params)
        )
        "silent" -> {}
        else -> {
            val lang = if(LANG.hasLanguage(this.locale)) this.locale
            else "en_us"

            sendMessage(
                LANG.replaceParameters("[§aJEXT§f] ${LANG[lang, key]}", params)
            )
        }
    }
}