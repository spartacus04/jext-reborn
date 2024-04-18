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
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URI
import java.util.zip.ZipFile

class AssetsManager {
    private lateinit var resourcePack: File
    private var legacyMode: Boolean = false

    internal suspend fun reloadAssets() {
        if(CONFIG.RESOURCE_PACK_HOST && PLUGIN.dataFolder.resolve(("resource-pack.zip")).exists()) {
            resourcePack = PLUGIN.dataFolder.resolve("resource-pack.zip")
            legacyMode = false
        } else if(Bukkit.getServer().resourcePack.isNotBlank()) {
            val name = Bukkit.getServer().resourcePackHash.ifBlank { "current" }

            if(tryDownloadRP(
                Bukkit.getServer().resourcePack,
                name
            )) {
                resourcePack = PLUGIN.dataFolder.resolve("caches").resolve("$name.zip")
                legacyMode = false
            } else {
                resourcePack = PLUGIN.dataFolder
                legacyMode = true
            }
        } else {
            resourcePack = PLUGIN.dataFolder
            legacyMode = true
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

    fun getFile(name: String) : InputStream? {
        val str = arrayListOf(
            if(!legacyMode) {
                "jext"
            } else "",
            name,
            "json"
        ).filter { it.isNotBlank() }.joinToString(".")

        if(legacyMode) {
            val file = resourcePack.resolve(str)

            return if(file.exists()) {
                file.inputStream()
            } else {
                null
            }
        } else {
            val zipFile = ZipFile(resourcePack)

            zipFile.getEntry(str).let {
                return if(it != null) {
                    zipFile.getInputStream(it)
                } else {
                    null
                }
            }
        }
    }

    internal fun getDefaultFile() = getFile(if(legacyMode) {
        "discs"
    } else {
        "jext"
    })
}