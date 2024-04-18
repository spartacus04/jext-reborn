package me.spartacus04.jext.discs.discplaying

import org.bukkit.Location
import org.bukkit.SoundCategory
import org.bukkit.entity.Player

class DefaultDiscPlayingMethod : DiscPlayingMethod {
    override fun playLocation(location: Location, namespace: String, volume : Float, pitch : Float) {
        location.world!!.players.forEach {
            it.playSound(location, namespace, SoundCategory.RECORDS, volume, pitch)
        }
    }

    override fun playPlayer(player: Player, namespace: String, volume : Float, pitch : Float) {
        player.playSound(player.location, namespace, SoundCategory.RECORDS, volume, pitch)
    }
}