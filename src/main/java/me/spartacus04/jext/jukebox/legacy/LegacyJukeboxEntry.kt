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
    /**
     * The function "play" takes a location as input and plays a sound or disc based on the type and value provided,
     * returning the duration of the sound or disc.
     *
     * @param location The `location` parameter represents the location where the play action will occur. It is of type
     * `Location`.
     * @return The code is returning a Long value.
     */
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

    /**
     * The function `stop` stops playing a sound or disc at a given location in Kotlin.
     *
     * @param location The `location` parameter represents a specific location in the game world where the action will take
     * place. It is typically represented by an object of the `Location` class, which contains information such as the
     * coordinates (x, y, z) and the world in which the location exists.
     * @return In the given code snippet, nothing is being explicitly returned. However, if the condition
     * `Material.matchMaterial(value)` evaluates to `null`, then the function will return and no further code will be
     * executed.
     */
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

    /**
     * The function `getItemstack` returns an `ItemStack` object based on the provided `type` and `value` parameters.
     *
     * @return The function `getItemstack()` returns an `ItemStack` object.
     */
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
        /**
         * The function "fromLegacyString" takes a legacyString as input and returns a LegacyJukeboxEntry object with the
         * appropriate namespace based on whether the legacyString matches any of the DISC_NAMESPACE values in the
         * ConfigData.DISCS array.
         *
         * @param legacyString The `legacyString` parameter is a string that represents a legacy jukebox entry.
         * @return a LegacyJukeboxEntry object.
         */
        fun fromLegacyString(legacyString: String): LegacyJukeboxEntry {
            return if(ConfigData.DISCS.any { it.DISC_NAMESPACE == legacyString }) {
                LegacyJukeboxEntry("jext", legacyString)
            } else {
                LegacyJukeboxEntry("minecraft", legacyString)
            }
        }
    }
}