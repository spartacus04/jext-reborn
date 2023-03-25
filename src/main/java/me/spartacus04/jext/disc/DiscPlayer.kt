package me.spartacus04.jext.disc

import io.github.bananapuncher714.nbteditor.NBTEditor
import me.spartacus04.jext.SpigotVersion.Companion.MAJORVERSION
import me.spartacus04.jext.SpigotVersion.Companion.MINORVERSION
import me.spartacus04.jext.config.ConfigData.Companion.CONFIG
import org.bukkit.*
import org.bukkit.plugin.java.JavaPlugin

class DiscPlayer(private val namespace: String?, private val duration: Int) {
    private var volume: Float
    private var pitch: Float

    constructor(disc: DiscContainer) : this(disc.namespace, disc.duration)

    init {
        volume = JUKEBOX_VOLUME
        pitch = 1.0f
    }

    fun setVolume(value: Float): DiscPlayer {
        volume = value
        return this
    }

    fun setPitch(value: Float): DiscPlayer {
        pitch = value
        return this
    }

    fun play(location: Location) {
        if (!CONFIG.ALLOW_MUSIC_OVERLAPPING) {
            stop(location)
        }

        location.world!!.playSound(location, namespace!!, SoundCategory.RECORDS, volume, pitch)

        if(location.block.type != Material.JUKEBOX) return
        if(MAJORVERSION < 19 || MAJORVERSION == 19 && MINORVERSION < 4) return

        Bukkit.getScheduler().runTaskLater(plugin!!, Runnable {
            val startTickCount = NBTEditor.getLong(location.block,"RecordStartTick")
            NBTEditor.set(location.block,startTickCount - (duration - 72) * 20 + 1, "TickCount")
        }, 1)
    }

    fun stop(location: Location) {
        for (player in location.world!!.players) {
            if (player.location.distance(location) <= JUKEBOX_VOLUME * JUKEBOX_RANGE_MULTIPLY) {
                player.stopSound(namespace!!, SoundCategory.RECORDS)
            }
        }

        if(location.block.type != Material.JUKEBOX) return
        if(MAJORVERSION < 19 || MAJORVERSION == 19 && MINORVERSION < 4) return

        NBTEditor.set(location.block,NBTEditor.getLong(location.block, "RecordStartTick") + 72 * 20, "TickCount")
    }

    companion object {
        private const val JUKEBOX_RANGE_MULTIPLY = 16.0
        private const val JUKEBOX_VOLUME = 4.0f

        var plugin: JavaPlugin? = null
    }
}