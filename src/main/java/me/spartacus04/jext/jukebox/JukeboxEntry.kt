package me.spartacus04.jext.jukebox

import de.tr7zw.nbtapi.NBT
import me.spartacus04.jext.config.ConfigData
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscPlayer
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class JukeboxEntry(
    var type: String,
    var value: String
) {
    fun play(location: Location) : Long {
        return if(type == "jext") {
            val disc = ConfigData.DISCS.first { it.DISC_NAMESPACE == value }

            if(location.block.type == Material.JUKEBOX) {
                NBT.modify(location.block.state) {
                    it.setItemStack("RecordItem", DiscContainer(disc).discItem)
                    it.setBoolean("IsPlaying", true)
                }
            }

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

            if(location.block.type == Material.JUKEBOX) {
                NBT.modify(location.block.state) {
                    it.removeKey("RecordItem")
                    it.setBoolean("IsPlaying", false)
                }
            }

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