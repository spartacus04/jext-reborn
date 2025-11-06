package me.spartacus04.jext

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import me.spartacus04.jext.language.DefaultMessages.RESOURCEPACK_DOWNLOAD_FAIL
import me.spartacus04.jext.language.DefaultMessages.RESOURCEPACK_DOWNLOAD_START
import me.spartacus04.jext.language.DefaultMessages.RESOURCEPACK_DOWNLOAD_SUCCESS
import me.spartacus04.jext.utils.getFileSha1Hash
import org.bukkit.Bukkit
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URI
import java.util.UUID
import java.util.zip.ZipFile

/**
 * The class `AssetsManager` is a utility class that's used to import, read, save and export assets from the server's resource pack.
 */
class AssetsManager(private val plugin: Jext) {
    private val localToRpMap = hashMapOf(
        "discs" to "",
        "nbs" to "nbs",
    )

    /**
     * Registers a config file to be imported from the resource pack.
     * 
     * @param id The id and path of the config file.
     */
    @Suppress("unused")
    fun registerConfigFile(id: String) {
        localToRpMap[id] = id
    }

    /**
     * Registers an asset to be imported from the resource pack.
     * 
     * @param id The id and name of the asset.
     * @param path The path of the asset in the resource pack.
     */
    @Suppress("unused")
    fun registerAsset(id: String, path: String) {
        localToRpMap[id] = path
    }

    private fun getResourcePack() : File? {
        return if(plugin.config.RESOURCE_PACK_HOST && plugin.dataFolder.resolve(("resource-pack.zip")).exists()) {
            plugin.dataFolder.resolve("resource-pack.zip")
        } else if(resourcePack.isNotBlank()) {
            val name = resourcePackHash.ifBlank { "current" }

            if(tryDownloadRP(
                    resourcePack,
                    name
                )) {
                plugin.dataFolder.resolve("caches").resolve("$name.zip")
            } else {
                null
            }
        } else {
            Bukkit.getLogger().warning("No resource pack specified or found.");
            null
        }
    }

    internal fun reloadAssets() {
        val file = getResourcePack()

        if(file != null) {
            runBlocking {
                withContext(Dispatchers.IO) {
                    val zipFile = ZipFile(file)

                    localToRpMap.entries.forEach {
                        try {
                            val zipEntry = zipFile.getEntry(getNameFromId(it.value))
                            val entry = plugin.dataFolder.resolve("${it.key}.json")

                            if(zipEntry.time > entry.lastModified()) {
                                zipFile.getInputStream(zipEntry).use { input ->
                                    entry.outputStream().use { output ->
                                        input.copyTo(output)
                                    }
                                }
                            }
                        } catch (_: Exception) { }
                    }
                }
            }
        }
    }

    private fun tryDownloadRP(url: String, path: String) : Boolean {
        val file = plugin.dataFolder.resolve("caches").resolve("$path.zip")

        if(!plugin.dataFolder.resolve("caches").exists())
            plugin.dataFolder.resolve("caches").mkdir()

        if(file.exists()) {
            if(path == "current") {
                file.delete()
            } else {
                return true
            }
        }

        plugin.colosseumLogger.info(RESOURCEPACK_DOWNLOAD_START)

        return try {
            val rpUrl = URI(url).toURL()

            val successFullDownload = runBlocking {
                withContext(Dispatchers.IO) {
                    return@withContext try{
                        val connection = rpUrl.openConnection() as HttpURLConnection

                        connection.setRequestProperty("X-Minecraft-Username", "jext")
                        connection.setRequestProperty("X-Minecraft-UUID", UUID.randomUUID().toString())
                        connection.setRequestProperty("X-Minecraft-Version", "1.21.5")
                        connection.setRequestProperty("X-Minecraft-Version-Id", "1.21.5")
                        connection.setRequestProperty("X-Minecraft-Pack-Format", "55")
                        connection.setRequestProperty("User-Agent", "Minecraft Java/1.21.5")

                        val inputStream = BufferedInputStream(connection.inputStream)
                        val outputStream = FileOutputStream(file)

                        val buffer = ByteArray(1024)
                        var bytesRead: Int

                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                        }

                        outputStream.close()
                        inputStream.close()

                        true
                    } catch (_ : Exception) {
                        false
                    }
                }
            }

            if(successFullDownload) {
                plugin.colosseumLogger.confirm(RESOURCEPACK_DOWNLOAD_SUCCESS)
            } else {
                plugin.colosseumLogger.error(RESOURCEPACK_DOWNLOAD_FAIL)
            }

            successFullDownload
        } catch (_: Exception) {
            plugin.colosseumLogger.error(RESOURCEPACK_DOWNLOAD_FAIL)
            false
        }
    }

    private fun getNameFromId(id: String) = listOf(
        "jext",
        id,
        "json"
    ).filter { it.isNotBlank() }.joinToString(".")

    /**
     * Gets an asset from the server's resource pack.
     * 
     * @param id The id of the asset.
     * 
     * @return The asset.
     */
    fun getAsset(id: String) : FileInputStream? {
        val file = plugin.dataFolder.resolve("$id.json")

        return if(file.exists()) {
            file.inputStream()
        } else null
    }

    /**
     * Saves an asset to the server's resource pack.
     * 
     * @param id The id of the asset.
     * @param content The content of the asset.
     */
    fun saveAsset(id: String, content: String) {
        val file = plugin.dataFolder.resolve("$id.json")

        if(!file.exists()) {
            file.createNewFile()
        }

        file.writeText(content)
    }

    private val resourcePack: String
        get() = try {
            plugin.config.RESOURCE_PACK_URL.ifBlank { Bukkit.getServer().resourcePack }
        } catch (_: NoSuchMethodError) {
            val propertiesFile = File(".").resolve("server.properties")

            propertiesFile.readLines().find { it.startsWith("resource-pack=") }!!.substringAfter("resource-pack=")
        }

    internal val resourcePackHash: String
        get() = try {
            if (plugin.config.RESOURCE_PACK_URL.isNotBlank())
                plugin.config.RESOURCE_PACK_HASH
            else Bukkit.getServer().resourcePackHash
        } catch (_: NoSuchMethodError) {
            val propertiesFile = File(".").resolve("server.properties")

            propertiesFile.readLines().find { it.startsWith("resource-pack-sha1=") }!!.substringAfter("resource-pack-sha1=")
        }

    internal val resourcePackHostedHash: ByteArray
        get() = try {
            getFileSha1Hash(plugin.dataFolder.resolve("resource-pack.zip"))
        } catch (_: Exception) {
            byteArrayOf()
        }

    /**
     * Exports the server's resource pack to a zip file.
     * 
     * @return Whether the export was successful.
     */
    fun tryExportResourcePack() : Boolean {
        val file = getResourcePack() ?: return false

        runBlocking {
            withContext(Dispatchers.IO) {
                val zipFile = ZipFile(file)

                localToRpMap.entries.forEach {
                    try {
                        val zipEntry = zipFile.getEntry(getNameFromId(it.value))
                        val entry = plugin.dataFolder.resolve("${it.key}.json")

                        if(zipEntry.time < entry.lastModified()) {
                            zipFile.getInputStream(zipEntry).use { input ->
                                entry.outputStream().use { output ->
                                    input.copyTo(output)
                                }
                            }
                        }
                    } catch (_: Exception) { }
                }

                val exportedFile = plugin.dataFolder.resolve("exported.zip")

                if(exportedFile.exists()) {
                    exportedFile.delete()
                }

                file.copyTo(exportedFile)
            }
        }

        return true
    }

    internal fun clearCache() {
        localToRpMap.entries.forEach {
            plugin.dataFolder.resolve("${it.key}.json").delete()
        }
    }
}