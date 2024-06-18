package me.spartacus04.jext

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import me.spartacus04.jext.JextState.CONFIG
import me.spartacus04.jext.JextState.PLUGIN
import me.spartacus04.jext.language.LanguageManager.Companion.RESOURCEPACK_DOWNLOAD_FAIL
import me.spartacus04.jext.language.LanguageManager.Companion.RESOURCEPACK_DOWNLOAD_START
import me.spartacus04.jext.language.LanguageManager.Companion.RESOURCEPACK_DOWNLOAD_SUCCESS
import org.bukkit.Bukkit
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URI
import java.util.zip.ZipFile

class AssetsManager {
    private val localToRpMap = hashMapOf(
        "discs" to "",
        "nbs" to "nbs",
    )

    fun registerConfigFile(id: String) {
        localToRpMap[id] = id
    }

    fun registerAsset(id: String, path: String) {
        localToRpMap[id] = path
    }

    private suspend fun getResourcePack() : File? {
        return if(CONFIG.RESOURCE_PACK_HOST && PLUGIN.dataFolder.resolve(("resource-pack.zip")).exists()) {
            PLUGIN.dataFolder.resolve("resource-pack.zip")
        } else if(resourcePack.isNotBlank()) {
            val name = resourcePackHash.ifBlank { "current" }

            if(tryDownloadRP(
                    resourcePack,
                    name
                )) {
                PLUGIN.dataFolder.resolve("caches").resolve("$name.zip")
            } else {
                null
            }
        } else {
            null
        }
    }

    internal suspend fun reloadAssets() {
        val file = getResourcePack()

        if(file != null) {
            runBlocking {
                withContext(Dispatchers.IO) {
                    val zipFile = ZipFile(file)

                    localToRpMap.entries.forEach {
                        try {
                            val zipEntry = zipFile.getEntry(getNameFromId(it.value))
                            val entry = PLUGIN.dataFolder.resolve("${it.key}.json")

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

    private suspend fun tryDownloadRP(url: String, path: String) : Boolean {
        val file = PLUGIN.dataFolder.resolve("caches").resolve("$path.zip")

        if(!PLUGIN.dataFolder.resolve("caches").exists())
            PLUGIN.dataFolder.resolve("caches").mkdir()

        if(file.exists()) {
            if(path == "current") {
                file.delete()
            } else {
                return true
            }
        }

        Bukkit.getConsoleSender().sendMessage(RESOURCEPACK_DOWNLOAD_START)

        return try {
            val rpUrl = URI(url).toURL()

            val successFullDownload = runBlocking {
                withContext(Dispatchers.IO) {
                    return@withContext try{
                        val inputStream = BufferedInputStream(rpUrl.openStream())
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

            Bukkit.getConsoleSender().sendMessage(
                if(successFullDownload) {
                    RESOURCEPACK_DOWNLOAD_SUCCESS
                } else {
                    RESOURCEPACK_DOWNLOAD_FAIL
                }
            )

            successFullDownload
        } catch (_: Exception) {
            Bukkit.getConsoleSender().sendMessage(RESOURCEPACK_DOWNLOAD_FAIL)
            false
        }
    }

    private fun getNameFromId(id: String) = listOf(
        "jext",
        id,
        "json"
    ).filter { it.isNotBlank() }.joinToString(".")

    fun getAsset(id: String) : FileInputStream? {
        val file = PLUGIN.dataFolder.resolve("$id.json")

        return if(file.exists()) {
            file.inputStream()
        } else null
    }

    fun saveAsset(id: String, content: String) {
        val file = PLUGIN.dataFolder.resolve("$id.json")

        if(!file.exists()) {
            file.createNewFile()
        }

        file.writeText(content)
    }

    private val resourcePack: String
        get() = try {
            Bukkit.getServer().resourcePack
        } catch (_: NoSuchMethodError) {
            val propertiesFile = File(".").resolve("server.properties")

            propertiesFile.readLines().find { it.startsWith("resource-pack=") }!!.substringAfter("resource-pack=")
        }

    val resourcePackHash: String
        get() = try {
            Bukkit.getServer().resourcePackHash
        } catch (_: NoSuchMethodError) {
            val propertiesFile = File(".").resolve("server.properties")

            propertiesFile.readLines().find { it.startsWith("resource-pack-sha1=") }!!.substringAfter("resource-pack-sha1=")
        }

    suspend fun tryExportResourcePack() : Boolean {
        val file = getResourcePack() ?: return false

        runBlocking {
            withContext(Dispatchers.IO) {
                val zipFile = ZipFile(file)

                localToRpMap.entries.forEach {
                    try {
                        val zipEntry = zipFile.getEntry(getNameFromId(it.value))
                        val entry = PLUGIN.dataFolder.resolve("${it.key}.json")

                        if(zipEntry.time < entry.lastModified()) {
                            zipFile.getInputStream(zipEntry).use { input ->
                                entry.outputStream().use { output ->
                                    input.copyTo(output)
                                }
                            }
                        }
                    } catch (_: Exception) { }
                }

                val exportedFile = PLUGIN.dataFolder.resolve("exported.zip")

                if(exportedFile.exists()) {
                    exportedFile.delete()
                }

                file.copyTo(exportedFile)
            }
        }

        return true
    }
}