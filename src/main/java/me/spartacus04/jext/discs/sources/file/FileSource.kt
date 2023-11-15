package me.spartacus04.jext.discs.sources.file

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.spartacus04.jext.State.CONFIG
import me.spartacus04.jext.State.PLUGIN
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.discs.sources.DiscSource
import org.bukkit.Bukkit
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.PrintWriter
import java.math.BigInteger
import java.net.URL
import java.security.MessageDigest
import java.util.zip.ZipFile
import javax.net.ssl.HttpsURLConnection

class FileSource : DiscSource {
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

        val sha1 = Bukkit.getServer().resourcePackHash
        val sha1CachePath = PLUGIN.dataFolder.resolve("caches").resolve("$sha1.zip")

        if(sha1CachePath.exists()) {
            val data = unpackResourcePack(sha1CachePath)

            if(data != null) {
                return data
            }
        }

        val rpUrl = Bukkit.getServer().resourcePack

        if(rpUrl.isNotEmpty()) {
            // fetch resource pack from url

            val url = URL(rpUrl)
            val connection = withContext(Dispatchers.IO) {
                url.openConnection()
            } as HttpsURLConnection
            val reader = BufferedReader(InputStreamReader(connection.inputStream))

            val content = StringBuilder()
            var line: String?
            while (withContext(Dispatchers.IO) {
                    reader.readLine()
                }.also { line = it } != null) {
                content.append(line)
            }

            // Calculate SHA-1 hash
            val md = MessageDigest.getInstance("SHA-1")
            val messageDigest = md.digest(content.toString().toByteArray())
            val no = BigInteger(1, messageDigest)
            var hashText = no.toString(16)
            while (hashText.length < 32) {
                hashText = "0$hashText"
            }

            // Write content to a file named with the SHA-1 hash
            val writer = withContext(Dispatchers.IO) {
                PrintWriter(PLUGIN.dataFolder.resolve("caches").resolve("$hashText.zip").absolutePath, "UTF-8")
            }

            writer.println(content.toString())
            writer.close()

            return unpackResourcePack(PLUGIN.dataFolder.resolve("caches").resolve("$hashText.zip"))
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