package me.spartacus04.jext

import me.spartacus04.jext.config.ConfigData.Companion.SCHEDULER
import org.bukkit.plugin.java.JavaPlugin
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.util.function.Consumer

/**
 * The class `Updater` is a utility class that checks for updates on GitHub.
 *
 * @property plugin The plugin instance.
 * @constructor Creates a new updater object.
 */
internal class Updater(private val plugin : JavaPlugin) {
    /**
     * The function `getVersion` retrieves the latest version of a GitHub repository and passes it to a consumer.
     *
     * @param consumer The `consumer` parameter is a functional interface that accepts a single argument of type `String?`
     * (nullable String) and returns no result. It is used to consume or process the version string obtained from the API
     * call.
     */
    fun getVersion(consumer: Consumer<String?>) {
        SCHEDULER.runTaskAsynchronously {
            try {
                val reader = BufferedReader(InputStreamReader(URL("https://api.github.com/repos/spartacus04/jext-reborn/releases/latest").openStream()))
                val text = reader.use {
                    it.readText()
                }

                val version = Regex("\"tag_name\": ?\"([^\"]+)\"").find(text)?.groupValues?.get(1)
                consumer.accept(version)
            } catch (exception: IOException) {
                plugin.logger.info("Unable to check for updates: " + exception.message)
            }
        }
    }
}