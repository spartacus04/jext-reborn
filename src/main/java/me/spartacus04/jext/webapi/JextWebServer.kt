package me.spartacus04.jext.webapi

import com.sun.net.httpserver.HttpServer
import kotlinx.coroutines.*
import me.spartacus04.jext.State.CONFIG
import me.spartacus04.jext.State.LANG
import me.spartacus04.jext.State.PLUGIN
import me.spartacus04.jext.State.SCHEDULER
import me.spartacus04.jext.language.LanguageManager.Companion.WEBAPI_RESOURCEPACK_NOT_FOUND
import me.spartacus04.jext.language.LanguageManager.Companion.WEBSERVER_STARTED
import me.spartacus04.jext.language.LanguageManager.Companion.WEBSERVER_STOPPED
import org.bukkit.Bukkit
import java.net.InetSocketAddress

class JextWebServer {
    private var server : HttpServer? = null
    private var resourcePackHostEnabled = CONFIG.RESOURCE_PACK_HOST
    private var apiEnabled = CONFIG.WEB_INTERFACE_API_ENABLED
    private var scope: Job? = null

    init {
        if(CONFIG.WEB_INTERFACE_API_ENABLED || CONFIG.RESOURCE_PACK_HOST) {
            start()
        }
    }

    private fun start() {
        SCHEDULER.runTaskAsynchronously {
            scope = CoroutineScope(Dispatchers.Default).launch {
                if(server != null) return@launch

                server = HttpServer.create(InetSocketAddress(CONFIG.WEB_INTERFACE_PORT), 0)
                server!!.executor = null

                server!!.start()

                SCHEDULER.runTask {
                    Bukkit.getConsoleSender().sendMessage(LANG.replaceParameters(WEBSERVER_STARTED, mapOf(
                        "port" to CONFIG.WEB_INTERFACE_PORT.toString()
                    )))
                }

                if (resourcePackHostEnabled) {
                    server!!.createContext("/resource-pack.zip", ResourcePackHandler())

                    val file = PLUGIN.dataFolder.resolve("resource-pack.zip")

                    if (!file.exists()) {
                        Bukkit.getConsoleSender().sendMessage(WEBAPI_RESOURCEPACK_NOT_FOUND)
                    }
                }
            }
        }
    }

    fun reload() {
        if(CONFIG.WEB_INTERFACE_API_ENABLED || CONFIG.RESOURCE_PACK_HOST) {
            if(server != null && server!!.address.port != CONFIG.WEB_INTERFACE_PORT) {
                stop()
            }

            start()

            if(CONFIG.WEB_INTERFACE_API_ENABLED != apiEnabled) {
                apiEnabled = CONFIG.WEB_INTERFACE_API_ENABLED
            }

            if(CONFIG.RESOURCE_PACK_HOST != resourcePackHostEnabled) {
                resourcePackHostEnabled = CONFIG.RESOURCE_PACK_HOST

                if(resourcePackHostEnabled) {
                    server!!.createContext("/resource-pack.zip", ResourcePackHandler())

                    val file = PLUGIN.dataFolder.resolve("resource-pack.zip")

                    if(!file.exists()) {
                        Bukkit.getConsoleSender().sendMessage(WEBAPI_RESOURCEPACK_NOT_FOUND)
                    }
                } else {
                    server!!.removeContext("/resource-pack.zip")
                }
            }
        } else {
            stop()
        }

        if(CONFIG.WEB_INTERFACE_API_ENABLED) {
            if (server == null || server!!.address.port != CONFIG.WEB_INTERFACE_PORT) {
                stop()
                start()
            } else if(CONFIG.RESOURCE_PACK_HOST != resourcePackHostEnabled) {
                if(resourcePackHostEnabled) {
                    server!!.removeContext("/resource-pack.zip")
                } else {
                    server!!.createContext("/resource-pack.zip", ResourcePackHandler())
                }
                resourcePackHostEnabled = CONFIG.RESOURCE_PACK_HOST
            }
        } else if(server != null) {
            stop()
        }
    }

    fun stop() {
        server?.stop(0)
        server = null
        runBlocking {
            scope?.cancelAndJoin()
        }
        Bukkit.getConsoleSender().sendMessage(WEBSERVER_STOPPED)
    }
}