package me.spartacus04.jext.discs.discstopping

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import me.spartacus04.jext.JextState.VERSION
import org.bukkit.Location
import org.bukkit.SoundCategory
import org.bukkit.entity.Player

/**
 * The class `DefaultDiscStoppingMethod` is a implementation of the `DiscStoppingMethod` interface that stops discs played from the resource pack.
 */
class DefaultDiscStoppingMethod : DiscStoppingMethod {
    override val requires = listOf<String>()

    private fun stopOldVersions(player: Player) {
        val packet = PacketContainer(PacketType.Play.Server.STOP_SOUND)

        packet.soundCategories.write(0, com.comphenix.protocol.wrappers.EnumWrappers.SoundCategory.RECORDS)

        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet)
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