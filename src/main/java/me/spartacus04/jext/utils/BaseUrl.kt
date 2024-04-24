package me.spartacus04.jext.utils

import me.spartacus04.jext.JextState.CONFIG
import me.spartacus04.jext.JextState.PLUGIN
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URI

internal class BaseUrl {
    private lateinit var ip: String

    fun updatePublicIp() {
        try {
            val reader = BufferedReader(InputStreamReader(URI("https://api.ipify.org/").toURL().openStream()))
            val text = reader.use {
                it.readText()
            }

            ip = text
        } catch (exception: IOException) {
            PLUGIN.logger.info("Unable to check public ip: " + exception.message)
        }
    }

    init {
        updatePublicIp()
    }

    fun getBaseUrl(commandSender: CommandSender) : String {
        if(CONFIG.WEB_INTERFACE_BASE_URL.isNotBlank()) {
            return CONFIG.WEB_INTERFACE_BASE_URL
        }

        return if(commandSender is ConsoleCommandSender || (commandSender as Player).address?.address?.isLoopbackAddress == true) {
            "127.0.0.1"
        } else {
            ip
        }
    }
}