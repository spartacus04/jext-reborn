package me.spartacus04.jext.config

import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler
import me.spartacus04.jext.ServerVersion
import org.bukkit.plugin.java.JavaPlugin

/**
 * This utility class stores most of the plugin's data, it should be only used to access the plugin's data.
 *
 * @constructor The class is a singleton, so the constructor is private.
 */
class ConfigData private constructor() {
    companion object {
        /**
         * The CONFIG property stores the plugin's config.
         */
        lateinit var CONFIG: Config
        /**
         * The DISCS property stores the plugin's discs.
         */
        lateinit var DISCS: List<Disc>
        /**
         * The LANG property stores the plugin's language manager.
         */
        lateinit var LANG: LanguageManager
        /**
         * The VERSION property stores the plugin's server version.
         */
        internal lateinit var VERSION: ServerVersion
        /**
         * The PLUGIN property stores the plugin's instance.
         */
        lateinit var PLUGIN: JavaPlugin
        /**
         * The SCHEDULER property stores the plugin's universal scheduler.
         */
        lateinit var SCHEDULER: TaskScheduler
    }
}