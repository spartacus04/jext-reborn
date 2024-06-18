package me.spartacus04.jext.gui

import me.spartacus04.jext.JextState.DISCS
import me.spartacus04.jext.utils.Constants.SOUND_MAP
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
        return when(type) {
            "jext" -> {
                val disc = DISCS[value]

                disc?.discItemStack
                    ?: arrayListOf(
                        SOUND_MAP.keys.map { mat -> ItemStack(mat) },
                        DISCS.map { discm -> discm.discItemStack }
                    ).flatten().random()
            }
            "jext-nbs" -> {
                val disc = DISCS[value]

                disc?.discItemStack
                    ?: arrayListOf(
                        SOUND_MAP.keys.map { mat -> ItemStack(mat) },
                        DISCS.map { discm -> discm.discItemStack }
                    ).flatten().random()
            }
            else -> {
                val material = Material.matchMaterial(value)

                if(material != null) {
                    return ItemStack(material)
                }

                arrayListOf(
                    SOUND_MAP.keys.map { mat -> ItemStack(mat) },
                    DISCS.map { disc -> disc.discItemStack }
                ).flatten().random()
            }
        }
    }
}