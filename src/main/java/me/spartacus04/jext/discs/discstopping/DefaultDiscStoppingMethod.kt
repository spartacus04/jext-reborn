package me.spartacus04.jext.discs.discstopping

import com.github.retrooper.packetevents.PacketEvents
import me.spartacus04.jext.JextState.VERSION
import me.spartacus04.jext.utils.WrapperPlayServerStopSoundCategory
import org.bukkit.Location
import org.bukkit.SoundCategory
import org.bukkit.entity.Player

/**
 * The class `DefaultDiscStoppingMethod` is an implementation of the `DiscStoppingMethod` interface that stops discs played from the resource pack.
 */
class DefaultDiscStoppingMethod : DiscStoppingMethod {
    override val requires = listOf<String>()

    private fun stopOldVersions(player: Player) {
        val category = com.github.retrooper.packetevents.protocol.sound.SoundCategory.RECORD
        val packet = WrapperPlayServerStopSoundCategory(category)

        PacketEvents.getAPI().playerManager.sendPacket(player, packet)
    }

    override fun stop(player: Player) {
        if(VERSION < "1.19") {
            stopOldVersions(player)
        } else {
            player.stopSound(SoundCategory.RECORDS)
        }
    }

    override fun stop(player: Player, namespace: String) {
        player.stopSound(namespace, SoundCategory.RECORDS)
    }

    override fun stop(location: Location, namespace: String) {
        for (player in location.world!!.players) {
            player.stopSound(namespace, SoundCategory.RECORDS)
        }
    }

    override fun stop(location: Location) {
        for (player in location.world!!.players) {
            if (player.location.distance(location) <= 64) {
                if(VERSION < "1.19") {
                    stopOldVersions(player)
                } else {
                    player.stopSound(SoundCategory.RECORDS)
                }
            }
        }
    }

}