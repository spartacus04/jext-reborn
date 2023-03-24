package me.spartacus04.jext.jukebox

import de.tr7zw.nbtapi.NBT
import me.spartacus04.jext.SpigotVersion.Companion.MAJORVERSION
import me.spartacus04.jext.SpigotVersion.Companion.MINORVERSION
import me.spartacus04.jext.config.ConfigData
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscPlayer
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Jukebox
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

data class JukeboxEntry(
    var type: String,
    var value: String
) {
    fun play(location: Location, plugin: JavaPlugin) : Long {
        return if(type == "jext") {
            val disc = ConfigData.DISCS.first { it.DISC_NAMESPACE == value }

            if(MAJORVERSION == 19 && MINORVERSION >= 4 || MAJORVERSION >= 20) {
                try {
                    if (location.block.type == Material.JUKEBOX) {
                        val jukebox = location.block.state as Jukebox

                        jukebox.setRecord(DiscContainer(disc).discItem)

                        NBT.modify(jukebox) {
                            it.setBoolean("IsPlaying", true)
                        }

                        jukebox.update(true)
                    }
                } catch (e: NullPointerException) {
                    Bukkit.getConsoleSender().sendMessage("[§cJEXT§f] §cRedstone parity is disabled due to a Spigot bug. Please update Spigot.")
                }
            }

            Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                DiscPlayer(DiscContainer(disc)).play(location)
            }, 1)

            disc.DURATION
        } else {
            val material = Material.matchMaterial(value) ?: return 0

            location.world?.playSound(location, DiscContainer.SOUND_MAP[material]!!.sound, 1f, 1f)

            DiscContainer.SOUND_MAP[material]!!.duration
        }.toLong()
    }

    fun stop(location: Location, plugin: JavaPlugin) {
        if(type == "jext") {
            val disc = ConfigData.DISCS.first { it.DISC_NAMESPACE == value }

            if(MAJORVERSION == 19 && MINORVERSION >= 4 || MAJORVERSION >= 20) {
                try {
                    if (location.block.type == Material.JUKEBOX) {
                        val jukebox = location.block.state as Jukebox

                        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                            jukebox.inventory.clear()
                            jukebox.update(true)
                        }, 1L)
                    }
                } catch (e: NullPointerException) {
                    Bukkit.getConsoleSender().sendMessage("[§cJEXT§f] §cRedstone parity is disabled due to a Spigot bug. Please update Spigot.")
                }
            }

            Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                DiscPlayer(DiscContainer(disc)).stop(location)
            }, 1)
        } else {
            val material = Material.matchMaterial(value) ?: return

            location.world?.players?.forEach {
                it.stopSound(DiscContainer.SOUND_MAP[material]!!.sound)
            }
        }
    }

    fun getItemstack() : ItemStack? {
        return if(type == "jext") {
            val disc = ConfigData.DISCS.first { it.DISC_NAMESPACE == value }

            DiscContainer(disc).discItem
        } else {
            val material = Material.matchMaterial(value) ?: return null

            ItemStack(material)
        }
    }
    companion object {
        fun fromLegacyString(legacyString: String): JukeboxEntry {
            return if(ConfigData.DISCS.any { it.DISC_NAMESPACE == legacyString }) {
                JukeboxEntry("jext", legacyString)
            } else {
                JukeboxEntry("minecraft", legacyString)
            }
        }
    }
}