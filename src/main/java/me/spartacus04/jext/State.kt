package me.spartacus04.jext

import com.github.Anon8281.universalScheduler.UniversalScheduler
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler
import me.spartacus04.jext.config.Config
import me.spartacus04.jext.config.ConfigFactory
import me.spartacus04.jext.discs.DiscManager
import me.spartacus04.jext.integrations.IntegrationsManager
import me.spartacus04.jext.language.LanguageManager
import me.spartacus04.jext.utils.ServerVersion
import me.spartacus04.jext.webapi.JextWebServer
import org.bukkit.plugin.java.JavaPlugin

object State {
    val PLUGIN: JavaPlugin = JavaPlugin.getPlugin(Jext::class.java)
    val VERSION = ServerVersion(PLUGIN.server.bukkitVersion.split("-")[0])
    val CONFIG: Config = ConfigFactory.createConfigObject()
    val LANG = LanguageManager()
    val DISCS = DiscManager()
    internal val SCHEDULER: TaskScheduler = UniversalScheduler.getScheduler(PLUGIN)
    val INTEGRATIONS = IntegrationsManager()
    internal val WEBSERVER = JextWebServer()
}