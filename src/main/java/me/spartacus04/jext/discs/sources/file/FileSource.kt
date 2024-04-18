package me.spartacus04.jext.discs.sources.file

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import me.spartacus04.jext.JextState.CONFIG
import me.spartacus04.jext.JextState.PLUGIN
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.discs.sources.DiscSource
import me.spartacus04.jext.language.LanguageManager.Companion.DOWNLOADING_RESOURCEPACK
import me.spartacus04.jext.language.LanguageManager.Companion.RESOURCEPACK_DOWNLOADED
import me.spartacus04.jext.language.LanguageManager.Companion.SHA1_REQUIRED
import org.bukkit.Bukkit
import java.io.*
import java.net.URI
import java.util.zip.ZipFile


internal class FileSource : DiscSource {
    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    private suspend fun unpackResourcePack(file: File): String? {
        return withContext(Dispatchers.IO) {
            val zipFile = ZipFile(file)

            zipFile.getEntry("jext.json").let {
                return@withContext if(it != null) {
                    zipFile.getInputStream(it).bufferedReader().readText()
                } else {
                    null
                }
            }
        }
    }

    private suspend fun getDiscsFile(): String? {
        if(CONFIG.RESOURCE_PACK_HOST) {
            val file = PLUGIN.dataFolder.resolve("resource-pack.zip")

            if(file.exists()) {
                val data = unpackResourcePack(file)

                if(data != null) {
                    return data
                }
            }
        }

        // check resource pack url in server.properties

        val sha1 = Bukkit.getServer().resourcePackHash.ifEmpty {
            null
        } ?: return run {
            Bukkit.getConsoleSender().sendMessage(SHA1_REQUIRED)
            null
        }
        val sha1CachePath = PLUGIN.dataFolder.resolve("caches").resolve("$sha1.zip")

        if(sha1CachePath.exists()) {
            val data = unpackResourcePack(sha1CachePath)

            if(data != null) {
                return data
            }
        }

        Bukkit.getConsoleSender().sendMessage(DOWNLOADING_RESOURCEPACK)

        val rpUrl = Bukkit.getServer().resourcePack

        if(rpUrl.isNotEmpty()) {
            val url = URI(rpUrl).toURL()

            runBlocking {
                withContext(Dispatchers.IO) {
                    if(!PLUGIN.dataFolder.resolve("caches").exists())
                        PLUGIN.dataFolder.resolve("caches").mkdir()

                    val inputStream = BufferedInputStream(url.openStream())
                    val outputStream = FileOutputStream(PLUGIN.dataFolder.resolve("caches").resolve("$sha1.zip"))

                    val buffer = ByteArray(1024)
                    var bytesRead: Int

                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }

                    outputStream.close()
                    inputStream.close()
                }
            }

            Bukkit.getConsoleSender().sendMessage(RESOURCEPACK_DOWNLOADED)

            return unpackResourcePack(PLUGIN.dataFolder.resolve("caches").resolve("$sha1.zip"))
        }

        return null
    }

    override suspend fun getDiscs(): List<Disc> {
        val discTypeToken = object : TypeToken<List<FileDisc>>() {}.type

        val contents = getDiscsFile() ?: if(PLUGIN.dataFolder.resolve("discs.json").exists()) {
            PLUGIN.dataFolder.resolve("discs.json").readText()
        } else {
            return emptyList()
        }

        return gson.fromJson<List<FileDisc>>(contents, discTypeToken).map { it.toJextDisc() }
    }
}