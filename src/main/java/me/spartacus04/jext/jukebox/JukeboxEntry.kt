package me.spartacus04.jext.jukebox

import de.tr7zw.nbtapi.NBT
import me.spartacus04.jext.config.ConfigData
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscPlayer
import me.spartacus04.jext.disc.DiscPlayer.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Jukebox
import org.bukkit.inventory.ItemStack

data class JukeboxEntry(
    var type: String,
    var value: String
) {
    fun play(location: Location) : Long {
        return if(type == "jext") {
            val disc = ConfigData.DISCS.first { it.DISC_NAMESPACE == value }

            // Due to a spigot bug the redstone parity is not enabled for jukebox guis
            // if(location.block.type == Material.JUKEBOX) {
            //     val jukebox = location.block.state as Jukebox
            //
            //    jukebox.setRecord(ItemStack(Material.MUSIC_DISC_11))
            // }

            DiscPlayer(DiscContainer(disc)).play(location)

            disc.DURATION
        } else {
            val material = Material.matchMaterial(value) ?: return 0

            location.world?.playSound(location, DiscContainer.SOUND_MAP[material]!!.sound, 1f, 1f)

            DiscContainer.SOUND_MAP[material]!!.duration
        }.toLong()
    }

    fun stop(location: Location) {
        if(type == "jext") {
            val disc = ConfigData.DISCS.first { it.DISC_NAMESPACE == value }

            // Due to a spigot bug the redstone parity is not enabled for jukebox guis
            // if(location.block.type == Material.JUKEBOX) {
            //     val jukebox = location.block.state as Jukebox
            //
            //    jukebox.setRecord(null)
            // }

            DiscPlayer(DiscContainer(disc)).stop(location)
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