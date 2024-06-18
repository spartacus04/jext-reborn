package me.spartacus04.jext.discs

import io.github.bananapuncher714.nbteditor.NBTEditor
import me.spartacus04.jext.JextState.CONFIG
import me.spartacus04.jext.JextState.DISCS
import me.spartacus04.jext.JextState.SCHEDULER
import me.spartacus04.jext.discs.discplaying.DiscPlayingMethod
import me.spartacus04.jext.discs.sources.file.FileDisc
import me.spartacus04.jext.utils.Constants.JEXT_DISC_MATERIAL
import me.spartacus04.jext.utils.Constants.SOUND_MAP
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

open class Disc(
    val sourceId: String,
    val discItemStack: ItemStack,
    val fragmentItemStack: ItemStack? = null,
    val namespace: String,
    val displayName: String,
    val duration: Int,
    val creeperDrop: Boolean,
    val lootTables: HashMap<String, Int>,
    val fragmentLootTables: HashMap<String, Int>,
    private val discPlayingMethod: DiscPlayingMethod
) {
    override fun toString() = displayName

    override operator fun equals(other: Any?): Boolean {
        other ?: return false

        return when(other) {
            is Disc -> { hashCode() == other.hashCode() }
            is FileDisc -> other.toJextDisc().hashCode() == hashCode()
            else -> false
        }
    }

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

            val startTickCount = NBTEditor.getLong(location.block,"RecordStartTick")
            NBTEditor.set(location.block,startTickCount - (duration - 72) * 20 + 5, "TickCount")
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
            } catch (e: Exception) {
                return false
            }

            return true
        }

        fun fromItemstack(itemStack: ItemStack) : Disc? {
            if (isCustomDisc(itemStack)) {
                val meta = itemStack.itemMeta!!

                return DISCS[meta]
            }

            return null
        }
    }
}
