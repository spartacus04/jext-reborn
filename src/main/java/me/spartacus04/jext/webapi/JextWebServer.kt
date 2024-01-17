package me.spartacus04.jext.webapi

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.spartacus04.jext.State.CONFIG

class JextWebServer {
    private var server : NettyApplicationEngine? = null

    init {
        System.setProperty("KTOR_LOG_LEVEL", "ERROR")
        reload()
    }

    fun reload() {
        if(CONFIG.WEB_INTERFACE_API_ENABLED && server == null) {
            server = embeddedServer(Netty, port = CONFIG.WEB_INTERFACE_PORT, host = "0.0.0.0", module = Application::module)
            server!!.start(wait = false)
        } else if(!CONFIG.WEB_INTERFACE_API_ENABLED && server != null) {
            server!!.stop(1000, 1000)
            server = null
        }
    }

    fun stop() = if(server != null) server!!.stop(1000, 1000) else Unit
}