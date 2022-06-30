package me.spartacus04.jext

import org.bukkit.plugin.java.JavaPlugin


class SpigotVersion private constructor() {
    companion object {
        var VERSION = 0

        fun load(plugin: JavaPlugin) {
            val vers = plugin.server.javaClass.`package`.name.replace(".", ",").split(",".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()[3]

            VERSION = vers.replace("1_", "").replace("_R\\d", "").toInt()
        }

    }
}