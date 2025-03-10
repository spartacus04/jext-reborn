package me.spartacus04.jext.utils

import me.spartacus04.jext.JextState.CONFIG
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.net.InetAddress

internal class BaseUrl {
    fun getUrl(commandSender: CommandSender) : String {
        var hostName = getBaseUrl(commandSender)

        if(!hostName.contains(":")) {
            val split = hostName.split("/")

            if(split.size > 3) {
                hostName = split[0] + "//" + split[2] + ":${CONFIG.WEB_INTERFACE_PORT}" + "/" + split.subList(3, split.size).joinToString("/")
            } else {
                hostName += ":${CONFIG.WEB_INTERFACE_PORT}"
            }
        }

        if(!hostName.startsWith("https://") && !hostName.startsWith("http://")) {
            hostName = "http://$hostName"
        }

        if(hostName.endsWith("/")) {
            hostName = hostName.substring(0, hostName.length - 1)
        }

        return hostName
    }

    private fun getBaseUrl(commandSender: CommandSender) : String {
        if(CONFIG.WEB_INTERFACE_BASE_URL.isNotBlank()) {
            return CONFIG.WEB_INTERFACE_BASE_URL
        } else if(Bukkit.getIp().isNotBlank()) {
            return Bukkit.getIp()
        }

        if(commandSender is Player) {
            return InetAddress.getLocalHost().hostAddress
        }

        return "127.0.0.1"
    }
}
