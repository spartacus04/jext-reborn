package me.spartacus04.jext.utils

import me.spartacus04.jext.JextState.PLUGIN
import me.spartacus04.jext.JextState.SCHEDULER
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URI

internal class Updater {
        fun getVersion(consumer: (String) -> Unit) {
            SCHEDULER.runTaskAsynchronously {
                try {
                    val reader = BufferedReader(InputStreamReader(URI("https://api.github.com/repos/spartacus04/jext-reborn/releases/latest").toURL().openStream()))
                    val text = reader.use {
                        it.readText()
                    }

                    val version = Regex("\"tag_name\": ?\"([^\"]+)\"").find(text)?.groupValues?.get(1)!!
                    consumer(version)
                } catch (exception: IOException) {
                    PLUGIN.logger.info("Unable to check for updates: " + exception.message)
                }
            }
        }
}