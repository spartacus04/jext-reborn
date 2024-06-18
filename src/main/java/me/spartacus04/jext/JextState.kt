package me.spartacus04.jext

import com.github.Anon8281.universalScheduler.UniversalScheduler
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import me.spartacus04.jext.config.Config
import me.spartacus04.jext.config.ConfigFactory
import me.spartacus04.jext.discs.DiscManager
import me.spartacus04.jext.geyser.GeyserManager
import me.spartacus04.jext.integrations.PermissionsIntegrationManager
import me.spartacus04.jext.language.LanguageManager
import me.spartacus04.jext.utils.BaseUrl
import me.spartacus04.jext.utils.ServerVersion
import me.spartacus04.jext.webapi.JextWebServer
import org.bukkit.plugin.java.JavaPlugin

object JextState {
    internal val BASE_URL = BaseUrl()
    val PLUGIN: JavaPlugin = JavaPlugin.getPlugin(Jext::class.java)

    internal val GSON: Gson = GsonBuilder().setStrictness(Strictness.LENIENT).setPrettyPrinting().create()
    internal val ASSETS_MANAGER = AssetsManager()
    internal val SCHEDULER: TaskScheduler = UniversalScheduler.getScheduler(PLUGIN)

    val VERSION = ServerVersion(PLUGIN.server.bukkitVersion.split("-")[0])
    val CONFIG: Config = ConfigFactory.createConfigObject()
    val LANG = LanguageManager()
    val DISCS = DiscManager()
    val INTEGRATIONS = PermissionsIntegrationManager()

    internal val WEBSERVER = JextWebServer()
    internal val GEYSER = GeyserManager()
}