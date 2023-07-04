package me.spartacus04.jext.config

import me.spartacus04.jext.ServerVersion
import org.bukkit.plugin.java.JavaPlugin

class ConfigData {
    companion object {
        lateinit var CONFIG: Config
        lateinit var DISCS: List<Disc>
        lateinit var LANG: LanguageManager
        lateinit var VERSION: ServerVersion
        lateinit var PLUGIN: JavaPlugin
    }
}