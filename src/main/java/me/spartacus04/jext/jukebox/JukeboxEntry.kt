package me.spartacus04.jext.jukebox

import me.spartacus04.jext.config.ConfigData
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * The class `JukeboxEntry` is a data class that represents a jukebox entry.
 *
 * @property type The `type` property is a string that represents the type of the jukebox entry. It can be either `jext` or `vanilla`.
 * @property value The `value` property is a string that represents the value of the jukebox entry. It can be either a disc namespace or a material name.
 * @constructor Creates a new jukebox entry.
 */
data class JukeboxEntry(
    var type: String,
    var value: String
) {
    /**
     * The function `toItemStack` returns an ItemStack object based on the type and value of the jukebox entry.
     *
     * @return
     */
    fun toItemStack() : ItemStack {
        return if(type == "jext") {
            val disc = ConfigData.DISCS.first { it.DISC_NAMESPACE == value }

            DiscContainer(disc).discItem
        } else {
            val material = Material.matchMaterial(value)

            if(material != null) {
                return ItemStack(material)
            }

            arrayListOf(
                DiscContainer.SOUND_MAP.keys.map { mat -> ItemStack(mat) },
                ConfigData.DISCS.map { disc -> DiscContainer(disc).discItem }
            ).flatten().random()
        }
    }
}