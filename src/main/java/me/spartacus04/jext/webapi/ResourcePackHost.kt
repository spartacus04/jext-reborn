package me.spartacus04.jext.webapi

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.spartacus04.jext.State.CONFIG
import me.spartacus04.jext.State.PLUGIN
import me.spartacus04.jext.language.LanguageManager.Companion.WEBAPI_RESOURCEPACK_NOT_FOUND
import org.bukkit.Bukkit

fun Application.resourcePackHost() {
    routing {
        if(CONFIG.RESOURCE_PACK_HOST) {
            val resourcePack = PLUGIN.dataFolder.resolve("resource-pack.zip")

            if(!resourcePack.exists()) {
                Bukkit.getConsoleSender().sendMessage(WEBAPI_RESOURCEPACK_NOT_FOUND)
            }

            get("/resource-pack.zip") {
                call.respondFile(resourcePack)
            }
        }
    }
}