package me.spartacus04.jext

import org.bukkit.plugin.java.JavaPlugin


class SpigotVersion private constructor() {
    companion object {
        var MAJORVERSION = 0
        var MINORVERSION = 0

        fun load(plugin: JavaPlugin) {
            val versionString = plugin.server.bukkitVersion.split("-")[0]

            MAJORVERSION = versionString.split(".")[1].toInt()
            try {
                MINORVERSION = versionString.split(".")[2].toInt()
            } catch (_: IndexOutOfBoundsException) { }
        }
    }
}