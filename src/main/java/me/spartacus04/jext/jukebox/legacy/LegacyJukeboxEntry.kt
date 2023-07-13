package me.spartacus04.jext.jukebox.legacy

import me.spartacus04.jext.config.ConfigData
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscPlayer
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval

@Suppress("deprecation")
@ScheduledForRemoval(inVersion = "1.3")
@Deprecated("This is part of the legacy jukebox gui system. It's going to be removed in the next major update.")
data class LegacyJukeboxEntry(
    var type: String,
    var value: String
) {
    fun play(location: Location) : Long {
        return if(type == "jext") {
            val disc = ConfigData.DISCS.first { it.DISC_NAMESPACE == value }

            DiscContainer(disc).play(location)

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

            DiscPlayer.stop(location, DiscContainer(disc).namespace)
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
        fun fromLegacyString(legacyString: String): LegacyJukeboxEntry {
            return if(ConfigData.DISCS.any { it.DISC_NAMESPACE == legacyString }) {
                LegacyJukeboxEntry("jext", legacyString)
            } else {
                LegacyJukeboxEntry("minecraft", legacyString)
            }
        }
    }
}