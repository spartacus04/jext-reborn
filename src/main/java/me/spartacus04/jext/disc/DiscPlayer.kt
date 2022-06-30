package me.spartacus04.jext.disc

import me.spartacus04.jext.config.ConfigData.Companion.CONFIG
import org.bukkit.*

class DiscPlayer(private val namespace: String?) {
    private var volume: Float
    private var pitch: Float

    constructor(disc: DiscContainer) : this(disc.namespace)

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
    }

    fun stop(location: Location) {
        for (player in location.world!!.players) {
            if (player.location.distance(location) <= JUKEBOX_VOLUME * JUKEBOX_RANGE_MULTIPLY) {
                player.stopSound(namespace!!, SoundCategory.RECORDS)
            }
        }
    }

    companion object {
        private const val JUKEBOX_RANGE_MULTIPLY = 16.0
        private const val JUKEBOX_VOLUME = 4.0f
    }
}