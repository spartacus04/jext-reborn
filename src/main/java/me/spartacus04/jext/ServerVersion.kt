package me.spartacus04.jext

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

/**
 * The class `ServerVersion` is a utility class that represents the version of a server.
 *
 * @param versionString The `versionString` parameter is a `String` representing the version of a server.
 * @constructor Creates a new server version object.
 */
class ServerVersion(versionString: String) {
    private val majorVersion = versionString.split(".")[0].toInt()
    private val minorVersion = versionString.split(".")[1].toInt()
    private val patchVersion = try {
        versionString.split(".")[2].toInt()
    } catch (_: IndexOutOfBoundsException) {
        0
    }

    /**
     * The function compares two ServerVersion objects and returns an integer indicating their relative order.
     *
     * @param version The `version` parameter is of type `ServerVersion`. It represents the version of a server.
     * @return an integer value.
     */
    operator fun compareTo(version: ServerVersion): Int {
        if(majorVersion > version.majorVersion) return 1
        if(majorVersion < version.majorVersion) return -1

        if(minorVersion > version.minorVersion) return 1
        if(minorVersion < version.minorVersion) return -1

        if(patchVersion > version.patchVersion) return 1
        if(patchVersion < version.patchVersion) return -1

        return 0
    }

    /**
     * The function compares the current version with a given version.
     *
     * @param version The `version` parameter is a `String` representing a version number.
     */
    operator fun compareTo(version: String): Int = compareTo(ServerVersion(version))
}