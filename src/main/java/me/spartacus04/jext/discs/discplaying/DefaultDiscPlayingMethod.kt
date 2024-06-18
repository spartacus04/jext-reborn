package me.spartacus04.jext.discs.discplaying

import me.spartacus04.jext.JextState.CONFIG
import org.bukkit.Location
import org.bukkit.SoundCategory
import org.bukkit.entity.Player

class DefaultDiscPlayingMethod : DiscPlayingMethod {
    override fun playLocation(location: Location, namespace: String, volume : Float, pitch : Float) {
        location.world!!.players.forEach {
            if(location.distance(it.location) <= CONFIG.JUKEBOX_RANGE) {
                it.playSound(location, namespace, SoundCategory.RECORDS, volume, pitch)
            }
        }
    }

    override fun playPlayer(player: Player, namespace: String, volume : Float, pitch : Float) {
        player.playSound(player.location, namespace, SoundCategory.RECORDS, volume, pitch)
    }
}