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

/**
 * Language manager is a class used to load and manage the plugin's languages.
 *
 * @property plugin The plugin instance used to load the languages.
 * @constructor Loads the languages from the jar file and creates the default language file.
 */
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

    /**
     * The function replaces placeholders in a string with corresponding values from a hashmap.
     *
     * @param string The `string` parameter is a string that contains placeholders for parameters. These placeholders are
     * represented by `%key%`, where `key` is the name of the parameter.
     * @param params The `params` parameter is a `HashMap` that maps `String` keys to `String` values. It is used to
     * replace placeholders in the `string` parameter with the corresponding values from the `params` map.
     * @return The function `replaceParameters` returns a new string with the parameters replaced.
     */
    fun replaceParameters(string: String, params: HashMap<String, String>) : String {
        var newString = string

        params.forEach { (key, value) ->
            newString = newString.replace("%$key%", value)
        }

        return newString
    }

    /**
     * The function retrieves a value from the language map and replaces the parameters.
     *
     * @param commandSender The command sender to get the locale from.
     * @param key The key to get the value from.
     * @param params A hashmap containing the parameters to replace.
     */
    fun getKey(commandSender: CommandSender, key: String, params: HashMap<String, String> = HashMap()) : String {
        if(commandSender !is Player) {
            return LANG.replaceParameters(languageMap["en_us"]!![key]!!, params)
        }

        return when(CONFIG.LANGUAGE_MODE.lowercase()) {
            "auto" -> {
                if(hasLanguage(commandSender.locale.lowercase())) LANG.replaceParameters(languageMap[commandSender.locale.lowercase()]!![key]!!, params)
                else LANG.replaceParameters(languageMap["en_us"]!![key]!!, params)
            }
            "custom" -> LANG.replaceParameters(languageMap["custom"]!![key]!!, params)
            else -> {
                val lang = if(hasLanguage(commandSender.locale.lowercase())) commandSender.locale.lowercase()
                else "en_us"

                return LANG.replaceParameters(languageMap[lang]!![key]!!, params)
            }
        }
    }

    /**
     * The function checks if a given locale is present in a language map.
     *
     * @param locale A string representing the locale or language code.
     */
    fun hasLanguage(locale: String) = languageMap.containsKey(locale.lowercase())
    /**
     * The function retrieves a value from a nested map based on the given locale and key.
     *
     * @param locale The `locale` parameter is a string that represents the language or region for which you want to
     * retrieve a translation. It is used to look up the translation in the `languageMap` map.
     * @param key The `key` parameter is a string that represents the key of the value you want to retrieve from the
     * `languageMap`.
     */
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
        const val MUSIC_DISC_FORMAT_OLD = "[§aJEXT§f] music disc format is old, you can update it by importing and re-exporting the resource pack in the generator\n§6[§2https://spartacus04.github.io/jext-reborn/§6]"
        const val BEDROCK_NOT_SUPPORTED = "[§cJEXT§f] §cJukebox GUI is not supported on bedrock edition!"

        /**
         * The function "load" initializes a LanguageManager object with the given JavaPlugin.
         *
         * @param plugin the plugin instance to load the language manager for.
         */
        internal fun load(plugin: JavaPlugin) {
            LANG = LanguageManager(plugin)
        }
    }
}

/**
 * The function gets a value from the language map, replaces the parameters, and sends it to the command sender.
 *
 * @param key The language key to get the value from.
 * @param params A hashmap containing the parameters to replace.
 */
fun CommandSender.sendJEXTMessage(key: String, params: HashMap<String, String> = HashMap()) {
    if(this !is Player) {
        return sendMessage(
            LANG.replaceParameters("[§aJEXT§f] ${LANG["en_us", key]}", params)
        )
    }

    when(CONFIG.LANGUAGE_MODE.lowercase()) {
        "auto" -> sendMessage(
            LANG.replaceParameters("[§aJEXT§f] ${LANG[this.locale.lowercase(), key]}", params)
        )
        "custom" -> sendMessage(
            LANG.replaceParameters("[§aJEXT§f] ${LANG["custom", key]}", params)
        )
        "silent" -> {}
        else -> {
            val lang = if(LANG.hasLanguage(this.locale.lowercase())) this.locale.lowercase()
            else "en_us"

            sendMessage(
                LANG.replaceParameters("[§aJEXT§f] ${LANG[lang, key]}", params)
            )
        }
    }
}
