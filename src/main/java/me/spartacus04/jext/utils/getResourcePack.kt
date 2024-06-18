package me.spartacus04.jext.utils

import org.bukkit.Bukkit
import java.io.File

fun getResourcePack(): String {
    try {
        return Bukkit.getServer().resourcePack
    } catch (_: NoSuchMethodError) {
        val propertiesFile = File(".").resolve("server.properties")

        return propertiesFile.readLines().find { it.startsWith("resource-pack=") }!!.substringAfter("resource-pack=")
    }
}

fun getResourcePackHash(): String {
    try {
        return Bukkit.getServer().resourcePackHash
    } catch (_: NoSuchMethodError) {
        val propertiesFile = File(".").resolve("server.properties")

        return propertiesFile.readLines().find { it.startsWith("resource-pack-sha1=") }!!.substringAfter("resource-pack-sha1=")
    }
}