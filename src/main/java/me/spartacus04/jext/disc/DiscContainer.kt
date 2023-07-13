package me.spartacus04.jext.disc

import io.github.bananapuncher714.nbteditor.NBTEditor
import me.spartacus04.jext.config.ConfigData
import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.config.ConfigData.Companion.PLUGIN
import me.spartacus04.jext.config.ConfigData.Companion.VERSION
import me.spartacus04.jext.config.Disc
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack


class DiscContainer {
    private val title: String
    val author: String
    val namespace: String
    val duration: Int
    private val customModelData : Int
    private val lores: ArrayList<String>
    private val material: Material = Material.MUSIC_DISC_11

    /**
     * The above code defines a Kotlin class called `DiscContainer` that represents a container for a music disc, with various
     * properties and methods for manipulating and playing the disc.
     *
     * @return The `toString()` function is being overridden to return the `title` property of the `DiscContainer` class.
     */
    override fun toString() = title

    constructor(data: Disc) {
        title = data.TITLE
        author = data.AUTHOR
        namespace = data.DISC_NAMESPACE
        customModelData = data.MODEL_DATA
        lores = data.LORE.toCollection(ArrayList())
        duration = data.DURATION
    }

    constructor(disc: ItemStack) {
        if (isCustomDisc(disc)) {
            val meta = disc.itemMeta
            customModelData = meta!!.customModelData

            val helper = DiscPersistentDataContainer(meta)
            author = helper.author!!
            namespace = helper.namespaceID!!
            title = helper.title!!

            lores = DISCS.find { it.DISC_NAMESPACE == namespace }?.LORE?.toCollection(ArrayList()) ?: ArrayList()
            duration = DISCS.find { it.DISC_NAMESPACE == namespace }?.DURATION ?: -1
        } else {
            throw IllegalStateException("Custom disc identifier missing!")
        }
    }

    // region items
    val discItem: ItemStack
        get() {
            val disc = ItemStack(material)
            val meta = disc.itemMeta

            meta!!.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)

            meta.setCustomModelData(customModelData)

            // Store custom disc data
            val helper = DiscPersistentDataContainer(meta)
            helper.author = author
            helper.namespaceID = namespace
            helper.title = title
            helper.setIdentifier()

            meta.lore = getProcessedLores()
            disc.itemMeta = meta
            return disc
        }

    val fragmentItem: ItemStack
        get() {
            val fragment = ItemStack(Material.DISC_FRAGMENT_5)
            val meta = fragment.itemMeta

            meta!!.setCustomModelData(customModelData)
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)

            val helper = DiscPersistentDataContainer(meta)
            helper.author = author
            helper.namespaceID = namespace
            helper.title = title
            helper.setIdentifier()

            val lores = getProcessedLores()

            meta.lore = lores
            fragment.itemMeta = meta

            return fragment
        }
    //endregion

    private fun getProcessedLores(): ArrayList<String> {
        val lores = ArrayList<String>()
        lores.add("${ChatColor.GRAY}$author - $title")
        lores.addAll(this.lores)

        return lores
    }

    private fun isCustomDisc(disc: ItemStack): Boolean {
        if (!disc.hasItemMeta()) {
            return false
        }

        val meta = disc.itemMeta
        val helper = DiscPersistentDataContainer(meta)

        try {
            if (!helper.checkIdentifier()) {
                return false
            }
        } catch (e: Exception) {
            return false
        }

        return true
    }


    /**
     * The function "play" plays a sound at a given location with optional volume and pitch parameters, and also handles
     * stopping any overlapping music and updating the tick count for jukeboxes.
     *
     * @param location The `location` parameter represents the location where the sound will be played. It is of type
     * `Location`, which typically includes the world, x, y, and z coordinates.
     * @param volume The "volume" parameter is used to control the volume of the sound being played. It is a float value
     * ranging from 0.0 to 1.0, where 0.0 is silent and 1.0 is the maximum volume. The default value is 4.0f
     * @param pitch The "pitch" parameter in the "play" function is used to control the pitch of the sound being played. A
     * pitch value of 1.0 represents the original pitch, while higher values increase the pitch and lower values decrease
     * the pitch.
     * @return The code does not explicitly return any value.
     */
    fun play(location: Location, volume : Float = 4.0f, pitch : Float = 1.0f) {
        if (!ConfigData.CONFIG.ALLOW_MUSIC_OVERLAPPING) {
            DiscPlayer.stop(location, namespace)
        }

        location.world!!.playSound(location, namespace, SoundCategory.RECORDS, volume, pitch)

        if(location.block.type != Material.JUKEBOX) return

        Bukkit.getScheduler().runTaskLater(PLUGIN, Runnable {
            location.world!!.players.forEach {
                it.stopSound(
                    SOUND_MAP[material]!!.sound,
                    SoundCategory.RECORDS
                )
            }

            val startTickCount = NBTEditor.getLong(location.block,"RecordStartTick")
            NBTEditor.set(location.block,startTickCount - (duration - 72) * 20 + 5, "TickCount")
        }, 5)
    }

    /**
     * The function "play" plays a sound for a player with optional volume and pitch parameters, and stops any currently
     * playing sound if music overlapping is not allowed.
     *
     * @param player The "player" parameter represents the player who will be playing the sound.
     * @param volume The volume parameter determines the volume of the sound being played. It is a float value ranging from
     * 0.0 to 1.0, where 0.0 is silent and 1.0 is the maximum volume. The default value is 4.0f, which means the sound
     * @param pitch The pitch parameter determines the pitch of the sound being played. A pitch of 1.0f represents the
     * original pitch, while a higher value increases the pitch and a lower value decreases it.
     */
    fun play(player: Player, volume : Float = 4.0f, pitch : Float = 1.0f) {
        if (!ConfigData.CONFIG.ALLOW_MUSIC_OVERLAPPING) {
            DiscPlayer.stop(player, namespace)
        }

        player.playSound(player.location, namespace, SoundCategory.RECORDS, volume * 500, pitch)
    }

    /**
     * The function checks if two objects of type DiscContainer are equal by comparing their hash codes.
     *
     * @param other The "other" parameter is of type "Any?", which means it can be any type of object or null.
     * @return The code is returning a boolean value indicating whether the hash codes of the two objects are equal.
     */
    override operator fun equals(other: Any?): Boolean {
        if(other !is DiscContainer) return false

        return hashCode() == other.hashCode()
    }

    /**
     * The function calculates the hash code of an object based on its title, author, namespace, duration, customModelData,
     * lores, and material.
     *
     * @return The result of the hashCode calculation is being returned.
     */
    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + namespace.hashCode()
        result = 31 * result + duration
        result = 31 * result + customModelData
        result = 31 * result + lores.hashCode()
        result = 31 * result + material.hashCode()
        return result
    }


    companion object {
        val SOUND_MAP = HashMap<Material, SoundData>()

        init {
            SOUND_MAP[Material.MUSIC_DISC_11] = SoundData(Sound.MUSIC_DISC_11, 72)
            SOUND_MAP[Material.MUSIC_DISC_13] = SoundData(Sound.MUSIC_DISC_13, 179)
            SOUND_MAP[Material.MUSIC_DISC_BLOCKS] = SoundData(Sound.MUSIC_DISC_BLOCKS, 346)
            SOUND_MAP[Material.MUSIC_DISC_CAT] = SoundData(Sound.MUSIC_DISC_CAT, 186)
            SOUND_MAP[Material.MUSIC_DISC_CHIRP] = SoundData(Sound.MUSIC_DISC_CHIRP, 186)
            SOUND_MAP[Material.MUSIC_DISC_FAR] = SoundData(Sound.MUSIC_DISC_FAR, 175)
            SOUND_MAP[Material.MUSIC_DISC_MALL] = SoundData(Sound.MUSIC_DISC_MALL, 198)
            SOUND_MAP[Material.MUSIC_DISC_MELLOHI] = SoundData(Sound.MUSIC_DISC_MELLOHI, 97)
            SOUND_MAP[Material.MUSIC_DISC_STAL] = SoundData(Sound.MUSIC_DISC_STAL, 151)
            SOUND_MAP[Material.MUSIC_DISC_STRAD] = SoundData(Sound.MUSIC_DISC_STRAD, 189)
            SOUND_MAP[Material.MUSIC_DISC_WAIT] = SoundData(Sound.MUSIC_DISC_WAIT, 238)
            SOUND_MAP[Material.MUSIC_DISC_WARD] = SoundData(Sound.MUSIC_DISC_WARD, 252)

            if(VERSION >= "1.16") {
                SOUND_MAP[Material.MUSIC_DISC_PIGSTEP] = SoundData(Sound.MUSIC_DISC_PIGSTEP, 149)
            }

            if(VERSION >= "1.18") {
                SOUND_MAP[Material.MUSIC_DISC_OTHERSIDE] = SoundData(Sound.MUSIC_DISC_OTHERSIDE, 196)
            }

            if(VERSION >= "1.19") {
                SOUND_MAP[Material.MUSIC_DISC_5] = SoundData(Sound.MUSIC_DISC_5, 179)
            }

            if(VERSION >= "1.20") {
                SOUND_MAP[Material.MUSIC_DISC_RELIC] = SoundData(Sound.MUSIC_DISC_RELIC, 218)
            }
        }

        data class SoundData(val sound: Sound, val duration: Int)
    }
}

val Material.isRecordFragment: Boolean
    get() {
        if(VERSION < "1.19") return false

        return when (this) {
            Material.DISC_FRAGMENT_5 -> true
            else -> false
        }
    }