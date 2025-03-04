package me.spartacus04.jext.discs

import io.github.bananapuncher714.nbteditor.NBTEditor
import me.spartacus04.jext.JextState.CONFIG
import me.spartacus04.jext.JextState.DISCS
import me.spartacus04.jext.JextState.SCHEDULER
import me.spartacus04.jext.JextState.VERSION
import me.spartacus04.jext.discs.discplaying.DiscPlayingMethod
import me.spartacus04.jext.discs.sources.file.FileDisc
import me.spartacus04.jext.utils.Constants.JEXT_DISC_MATERIAL
import me.spartacus04.jext.utils.Constants.SOUND_MAP
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * The class `Disc` is a data class that represents a custom disc in the plugin.
 * 
 * @param sourceId The id of the disc source.
 * @param baseDiscItemStack The itemstack of the disc.
 * @param baseFragmentItemStack The itemstack of the disc fragment.
 * @param namespace The namespace of the disc.
 * @param displayName The display name of the disc.
 * @param duration The duration of the disc.
 * @param creeperDrop Whether the disc can be dropped by creepers.
 * @param lootTables The loot tables of the disc.
 * @param fragmentLootTables The loot tables of the disc fragment.
 * @param discPlayingMethod The method used to play the disc.
 */
open class Disc(
    val sourceId: String,
    private val baseDiscItemStack: ItemStack,
    private val baseFragmentItemStack: ItemStack? = null,
    val namespace: String,
    val displayName: String,
    val duration: Int,
    val creeperDrop: Boolean,
    val lootTables: HashMap<String, Int>,
    val fragmentLootTables: HashMap<String, Int>,
    private val discPlayingMethod: DiscPlayingMethod
) {
    // Stupid Ahh abstraction to prevent disc being cancelled from existence

    /**
     * Returns the itemstack of the disc.
     *
     * @return The itemstack of the disc.
     */
    val discItemStack: ItemStack
        get() {
            return baseDiscItemStack.clone()
        }

    /**
     * Returns the itemstack of the disc fragment.
     *
     * @return The itemstack of the disc fragment.
     */
    val fragmentItemStack: ItemStack?
        get() {
            return baseFragmentItemStack?.clone()
        }

    /**
     * Returns the display name of the disc.
     * 
     * @return The display name of the disc.
     */
    override fun toString() = displayName

    /**
     * Compares disc objects based on their hash codes.
     * 
     * @param other The other object to compare.
     * 
     * @return Whether the objects are equal.
     */
    override operator fun equals(other: Any?): Boolean {
        other ?: return false

        return when(other) {
            is Disc -> { hashCode() == other.hashCode() }
            is FileDisc -> other.toJextDisc().hashCode() == hashCode()
            else -> false
        }
    }

    /**
     * Returns the hash code of the disc.
     * 
     * @return The hash code of the disc.
     */
    override fun hashCode(): Int {
        var result = discItemStack.hashCode()
        result = 31 * result + namespace.hashCode()
        result = 31 * result + displayName.hashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + creeperDrop.hashCode()
        return result
    }

    /**
     * Plays the custom disc at the specified location
     *
     * @param location The location
     * @param volume The volume of the disc (A.K.A. the distance at which the disc can be heard)
     * @param pitch The pitch of the disc
     */
    fun play(location: Location, volume : Float = 4.0f, pitch : Float = 1.0f) {
        if (CONFIG.DISABLE_MUSIC_OVERLAP) {
            DISCS.stop(location, namespace)
        }

        discPlayingMethod.playLocation(location, namespace, volume, pitch)

        if(location.block.type != Material.JUKEBOX) return

        SCHEDULER.runTaskLater({
            location.world!!.players.forEach {
                it.stopSound(
                    SOUND_MAP[JEXT_DISC_MATERIAL]!!.sound,
                    SoundCategory.RECORDS
                )
            }

            if(location.block.type == Material.JUKEBOX) {
                if(VERSION <= "1.21") {
                    val startTickCount = NBTEditor.getLong(location.block,"RecordStartTick")

                    NBTEditor.set(location.block,startTickCount - (duration - 72) * 20 + 5, "TickCount")
                } else {
                    NBTEditor.set(location.block, ((72 - duration) * 20 + 5).toLong(), "ticks_since_song_started")
                }
            }
        }, 5)
    }

    /**
     * Plays the custom disc for the specified player
     *
     * @param player The player
     * @param volume The volume of the disc (A.K.A. the distance at which the disc can be heard)
     * @param pitch The pitch of the disc
     */
    fun play(player: Player, volume : Float = 4.0f, pitch : Float = 1.0f) {
        if (CONFIG.DISABLE_MUSIC_OVERLAP) {
            DISCS.stop(player, namespace)
        }

        discPlayingMethod.playPlayer(player, namespace, volume, pitch)
    }

    companion object {
        /**
         * Checks if the itemstack is a custom disc.
         * 
         * @param disc The itemstack
         * 
         * @return Whether the itemstack is a custom disc
         */
        fun isCustomDisc(disc: ItemStack): Boolean {
            if (!disc.hasItemMeta()) {
                return false
            }

            val meta = disc.itemMeta
            val helper = meta?.let { DiscPersistentDataContainer(it) } ?: return false

            try {
                if (!helper.checkIdentifier()) {
                    return false
                }
            } catch (_: Exception) {
                return false
            }

            return true
        }

        /**
         * Gets the custom disc from the itemstack.
         * 
         * @param itemStack The itemstack
         * 
         * @return The custom disc
         */
        fun fromItemstack(itemStack: ItemStack) : Disc? {
            if (isCustomDisc(itemStack)) {
                val meta = itemStack.itemMeta!!

                return DISCS[meta]
            }

            return null
        }
    }
}
