package me.spartacus04.jext.utils

import me.spartacus04.jext.JextState.CONFIG
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.net.InetAddress

internal class BaseUrl {
    fun getBaseUrl(commandSender: CommandSender) : String {
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