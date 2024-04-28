package me.spartacus04.jext.language

import com.google.gson.reflect.TypeToken
import me.spartacus04.jext.JextState.CONFIG
import me.spartacus04.jext.JextState.GSON
import me.spartacus04.jext.JextState.PLUGIN
import me.spartacus04.jext.config.fields.FieldLanguageMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.File
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile

/**
 * Language manager is a class used to load and manage the plugin's languages.
 *
 */
class LanguageManager {
    private val languageMap = HashMap<String, Map<String, String>>()

    init {
        // this loads all languages in the languageMap
        JarFile(File(javaClass.protectionDomain.codeSource.location.path).absolutePath.replace("%20", " ")).use {
            val entries: Enumeration<JarEntry> = it.entries() //gives ALL entries in jar
            while (entries.hasMoreElements()) {
                val element = entries.nextElement()
                if (element.name.startsWith("langs/") && element.name.endsWith(".json")) {
                    val langName = element.name.replaceFirst("langs/", "")

                    PLUGIN.getResource("langs/$langName")!!.bufferedReader().use {file ->
                        val mapType = object : TypeToken<Map<String, String>>() {}.type
                        val languageMap : Map<String, String> = GSON.fromJson(file.readText(), mapType)

                        this.languageMap.put(langName.replace(".json", "").lowercase(), languageMap)
                    }
                }
            }
        }

        if(CONFIG.LANGUAGE_MODE == FieldLanguageMode.CUSTOM) {
            val customFile = PLUGIN.dataFolder.resolve("lang.json")

            if (!customFile.exists()) {
                PLUGIN.getResource("langs/en_US.json")!!.bufferedReader().use {
                    customFile.createNewFile()
                    customFile.writeText(it.readText())
                }
            }

            updateLang(customFile, languageMap["en_us"]!!)

            customFile.bufferedReader().use {
                val mapType = object : TypeToken<Map<String, String>>() {}.type
                val languageMap : Map<String, String> = GSON.fromJson(it.readText(), mapType)

                this.languageMap.put("custom", languageMap)
            }
        }
    }

    /**
     * Updates the language file to the latest version
     *
     * @param file The language file
     * @param lang The language map
     */
    private fun updateLang(file: File, lang: Map<String, String>) {
        val jsonConfig = file.readText()

        val mapType = object : TypeToken<HashMap<String, String>>() {}.type
        val languageMap = GSON.fromJson<HashMap<String, String>>(jsonConfig, mapType)

        lang.keys.forEach {
            if(!languageMap.containsKey(it)) {
                languageMap[it] = lang[it]!!
            }
        }

        file.writeText(GSON.toJson(languageMap))
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
    fun replaceParameters(string: String, params: Map<String, String>) : String {
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
    fun getKey(commandSender: CommandSender, key: String, params: Map<String, String> = emptyMap()) : String {
        if(commandSender !is Player) {
            return replaceParameters(languageMap["en_us"]!![key]!!, params)
        }

        return when(CONFIG.LANGUAGE_MODE) {
            FieldLanguageMode.AUTO -> {
                if(hasLanguage(commandSender.locale.lowercase())) replaceParameters(languageMap[commandSender.locale.lowercase()]!![key]!!, params)
                else replaceParameters(languageMap["en_us"]!![key]!!, params)
            }
            FieldLanguageMode.CUSTOM -> replaceParameters(languageMap["custom"]!![key]!!, params)
            else -> {
                val lang = CONFIG.LANGUAGE_MODE.name.lowercase()

                return replaceParameters(languageMap[lang]!![key]!!, params)
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

        const val UPDATE_LINK = "§6[§2https://github.com/spartacus04/jext-reborn/releases/latest/§6]"
        const val JEXT_VERSION = "[§aJEXT§f]§a v%version%"

        const val CROWDIN_MESSAGE = "[§aJEXT§f] It looks like your language isn't in JEXT yet. Why not contribute and add it yourself here?"
        const val CROWDIN_LINK = "§6[§2https://crwd.in/jext-reborn§6]"

        const val VULNERABLE_MESSAGE = "[§cJEXT§f] §cSpigot version is outdated and is vulnerable to a crash exploit. Please update it."
        const val BEDROCK_NOT_SUPPORTED = "[§cJEXT§f] §cJukebox GUI is not supported on bedrock edition!"

        const val WEBAPI_RESOURCEPACK_NOT_FOUND = "[§cJEXT§f] §cresource-pack.zip not found, please provide it in the plugin directory"

        const val WEBSERVER_STARTED = "[§aJEXT§f] §aWebserver started on port %port%"
        const val WEBSERVER_STOPPED = "[§eJEXT§f] §eWebserver stopped"

        const val GEYSER_RELOAD = "[§eJEXT§f]§e Warning: Geyser won't apply the resource pack changes until you restart the server!"

        const val RESOURCEPACK_DOWNLOAD_START = "[§aJEXT§f]§a Downloading resource pack..."
        const val RESOURCEPACK_DOWNLOAD_SUCCESS = "[§aJEXT§f]§a Resource pack downloaded!"
        const val RESOURCEPACK_DOWNLOAD_FAIL = "[§cJEXT§f]§c Resource pack download failed! No disc will be loaded."

        const val NBS_NOT_FOUND = "[§cJEXT§f]§c No %name%.nbs file found in the nbs folder! This disc won't be loaded."
    }
}
