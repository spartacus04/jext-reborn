package me.spartacus04.jext.discs

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import com.google.gson.annotations.SerializedName
import io.github.bananapuncher714.nbteditor.NBTEditor
import me.spartacus04.jext.State.CONFIG
import me.spartacus04.jext.State.DISCS
import me.spartacus04.jext.State.SCHEDULER
import me.spartacus04.jext.State.VERSION
import me.spartacus04.jext.discs.sources.file.FileDisc
import me.spartacus04.jext.utils.Constants.JEXT_DISC_MATERIAL
import me.spartacus04.jext.utils.Constants.SOUND_MAP
import net.md_5.bungee.api.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

data class Disc(
    val discItemStack: ItemStack,
    val fragmentItemStack: ItemStack? = null,
    val namespace: String,
    val displayName: String,
    val duration: Int,
    val creeperDrop: Boolean,
    val lootTables: HashMap<String, Int>,
    val fragmentLootTables: HashMap<String, Int>,
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
            stop(location, namespace)
        }

        location.world!!.players.forEach {
            it.playSound(location, namespace, SoundCategory.RECORDS, volume, pitch)
        }

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
            stop(player, namespace)
        }

        player.playSound(player.location, namespace, SoundCategory.RECORDS, volume * 500, pitch)
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

        //# region stop
        /**
         * The function "stop" stops a sound being played by a player in a specific namespace.
         *
         * @param player The "player" parameter is an instance of the Player class. It represents the player who will stop
         * the sound.
         * @param namespace The namespace parameter is a string that represents the namespace of the sound. It is used to
         * identify the specific sound that needs to be stopped.
         */
        fun stop(player: Player, namespace: String) {
            player.stopSound(namespace, SoundCategory.RECORDS)
        }

        /**
         * The function "stop" stops the sound being played by a player.
         *
         * @param player The "player" parameter is of type "Player".
         */
        fun stop(player: Player) {
            if(VERSION < "1.19") {
                val packet = PacketContainer(PacketType.Play.Server.STOP_SOUND)

                packet.soundCategories.write(0, com.comphenix.protocol.wrappers.EnumWrappers.SoundCategory.RECORDS)

                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet)
            } else {
                player.stopSound(SoundCategory.RECORDS)
            }
        }

        /**
         * The function stops a sound playing in a specific location for all players and updates the tick count for a
         * jukebox block.
         *
         * @param location The "location" parameter is the location where the stop function is being called. It represents
         * a specific block in the game world.
         * @param namespace The `namespace` parameter is a string that represents the namespace of the sound. It is used to
         * identify the specific sound that needs to be stopped.
         * @return The code is returning nothing (void).
         */
        fun stop(location: Location, namespace: String) {
            for (player in location.world!!.players) {
                player.stopSound(namespace, SoundCategory.RECORDS)
            }

            if(location.block.type != Material.JUKEBOX) return

            NBTEditor.set(location.block,NBTEditor.getLong(location.block, "RecordStartTick") + 72 * 20, "TickCount")
        }

        /**
         * The function stops playing music for players within a certain distance of a given location and updates the tick
         * count for a jukebox block.
         *
         * @param location The "location" parameter represents the location where the "stop" function is being called. It
         * is of type "Location", which is a class in the Bukkit API that represents a specific location in a Minecraft
         * world.
         * @return The code is not returning anything.
         */
        fun stop(location: Location) {
            for (player in location.world!!.players) {
                if (player.location.distance(location) <= 64) {
                    if(VERSION < "1.19") {
                        val packet = PacketContainer(PacketType.Play.Server.STOP_SOUND)

                        packet.soundCategories.write(0, com.comphenix.protocol.wrappers.EnumWrappers.SoundCategory.RECORDS)

                        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet)
                    } else {
                        player.stopSound(SoundCategory.RECORDS)
                    }
                }
            }

            if(location.block.type != Material.JUKEBOX) return

            NBTEditor.set(location.block,NBTEditor.getLong(location.block, "RecordStartTick") + 72 * 20, "TickCount")
        }
        //#  endregion
    }
}
