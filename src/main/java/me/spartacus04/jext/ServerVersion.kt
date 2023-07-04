package me.spartacus04.jext

import me.spartacus04.jext.config.ConfigData.Companion.VERSION
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class ServerVersion(versionString: String, val serverType: String = "unknown") {
    private val majorVersion = versionString.split(".")[0].toInt()
    private val minorVersion = versionString.split(".")[1].toInt()
    private val patchVersion = try {
        versionString.split(".")[2].toInt()
    } catch (_: IndexOutOfBoundsException) {
        0
    }

    operator fun compareTo(version: ServerVersion): Int {
        if(majorVersion > version.majorVersion) return 1
        if(majorVersion < version.majorVersion) return -1

        if(minorVersion > version.minorVersion) return 1
        if(minorVersion < version.minorVersion) return -1

        if(patchVersion > version.patchVersion) return 1
        if(patchVersion < version.patchVersion) return -1

        return 0
    }

    operator fun compareTo(version: String): Int = compareTo(ServerVersion(version))

    companion object {
        fun load(plugin: JavaPlugin) {
            VERSION = ServerVersion(plugin.server.bukkitVersion.split("-")[0], Bukkit.getServer().name)
        }
    }
}