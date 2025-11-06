package me.spartacus04.jext

import com.github.retrooper.packetevents.PacketEvents
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder
import me.spartacus04.colosseum.ColosseumPlugin
import me.spartacus04.jext.commands.AdminGuiCommand
import me.spartacus04.jext.commands.DiscCommand
import me.spartacus04.jext.commands.DiscGiveCommand
import me.spartacus04.jext.commands.ExportCommand
import me.spartacus04.jext.commands.FragmentCommand
import me.spartacus04.jext.commands.FragmentGiveCommand
import me.spartacus04.jext.commands.JukeboxGuiCommand
import me.spartacus04.jext.commands.PlayAtCommand
import me.spartacus04.jext.commands.PlayMusicCommand
import me.spartacus04.jext.commands.ReloadCommand
import me.spartacus04.jext.commands.StopMusicCommand
import me.spartacus04.jext.commands.WebUICommand
import me.spartacus04.jext.config.Config
import me.spartacus04.jext.config.ConfigFactory
import me.spartacus04.jext.discs.DiscManager
import me.spartacus04.jext.discs.sources.file.FileSource
import me.spartacus04.jext.discs.sources.nbs.NbsSource
import me.spartacus04.jext.geyser.GeyserManager
import me.spartacus04.jext.gui.JukeboxGui
import me.spartacus04.jext.integrations.PermissionsIntegrationManager
import me.spartacus04.jext.language.DefaultMessages.DISABLED_MESSAGE
import me.spartacus04.jext.language.DefaultMessages.DOCUMENTATION_LINK
import me.spartacus04.jext.language.DefaultMessages.ENABLED_MESSAGE
import me.spartacus04.jext.language.DefaultMessages.NO_DISCS_FOUND
import me.spartacus04.jext.language.DefaultMessages.UPDATE_LINK
import me.spartacus04.jext.listeners.ListenerRegistrant
import me.spartacus04.jext.utils.BaseUrl
import me.spartacus04.jext.webapi.JextWebServer

/**
 * The class `Jext` is the main class of the plugin. It extends the `JavaPlugin` class, which is the main class of all
 * Bukkit plugins.
 *
 * @constructor Creates a new Jext plugin.
 */
@Suppress("unused")
class Jext : ColosseumPlugin() {
    override fun onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this))
        PacketEvents.getAPI().load()
    }

    override fun onEnable() {
        try {
            load()
        } catch (e: Exception) {
            e.printStackTrace()
            server.pluginManager.disablePlugin(this)
        }
    }

    override fun onDisable() {
        webServer.stop()
        colosseumLogger.warn(DISABLED_MESSAGE)
    }

    private fun load() {
        INSTANCE = this

        buildI18nManager {
            this.loadInternalLanguageDirectory("langs")
            this.loadExternalLanguageFiles(dataFolder.resolve("langs.json"), "custom", "en_us")
            this.setDefaultLocale("en_us")
            this.setLanguagesToLower(true)
        }

        discs.registerDiscSource(FileSource(), NbsSource()) {
            integrations.reloadDefaultIntegrations()
            JukeboxGui.loadFromFile()

            if(discs.size() == 0) {
                colosseumLogger.warn(NO_DISCS_FOUND)
                colosseumLogger.url(DOCUMENTATION_LINK)
            }
        }

        ListenerRegistrant.registerListeners(this)

        registerCommands {
            addCommands(
                AdminGuiCommand::class.java,
                DiscCommand::class.java,
                DiscGiveCommand::class.java,
                ExportCommand::class.java,
                FragmentCommand::class.java,
                FragmentGiveCommand::class.java,
                JukeboxGuiCommand::class.java,
                PlayAtCommand::class.java,
                PlayMusicCommand::class.java,
                ReloadCommand::class.java,
                StopMusicCommand::class.java,
                WebUICommand::class.java,
            )

            registerMainCommand("jext")
        }

        colosseumLogger.warn(ENABLED_MESSAGE)

        if(config.CHECK_FOR_UPDATES) {
            checkForUpdates("spartacus04/jext-reborn") {
                if(it != description.version) {
                    colosseumLogger.confirmI18n(this, "update-available")
                    colosseumLogger.url(UPDATE_LINK)
                }
            }
        }
    }

    val config: Config
        get() = ConfigFactory.createConfigObject(this)

    val baseUrl: BaseUrl
        get() = BaseUrl(this)

    val assetsManager: AssetsManager
        get() = AssetsManager(this)

    val discs: DiscManager
        get() = DiscManager(this)

    val integrations: PermissionsIntegrationManager
        get() = PermissionsIntegrationManager()

    val webServer: JextWebServer
        get() = JextWebServer(this)

    val geyserManager: GeyserManager
        get() = GeyserManager()

    companion object {
        lateinit var INSTANCE: Jext
    }
}