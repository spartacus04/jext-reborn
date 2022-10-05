package me.spartacus04.jext

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException
import java.net.URL
import java.util.*
import java.util.function.Consumer


class Updater(private val plugin : JavaPlugin, private val resId : Int) {
    fun getVersion(consumer: Consumer<String?>) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            try {
                URL("https://api.spigotmc.org/legacy/update.php?resource=$resId").openStream()
                    .use { inputStream ->
                        Scanner(inputStream).use { scanner ->
                            if (scanner.hasNext()) {
                                consumer.accept(scanner.next())
                            }
                        }
                    }
            } catch (exception: IOException) {
                plugin.logger.info("Unable to check for updates: " + exception.message)
            }
        })
    }
}