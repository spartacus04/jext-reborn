package me.spartacus04.jext.discs.discstopping

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI
import com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer
import org.bukkit.Location
import org.bukkit.entity.Player

class NbsDiscStoppingMethod : DiscStoppingMethod {
    override val requires = listOf("NoteBlockAPI")

    override fun stop(player: Player) {
        NoteBlockAPI.stopPlaying(player)
    }

    override fun stop(player: Player, namespace: String) {
        NoteBlockAPI.stopPlaying(player)
    }

    override fun stop(location: Location) {
        location.world!!.players.forEach {
            val songPlayers = NoteBlockAPI.getSongPlayersByPlayer(it)

            songPlayers?.forEach { songPlayer ->
                if(songPlayer is PositionSongPlayer && songPlayer.targetLocation == location) {
                    songPlayer.isPlaying = false
                    songPlayer.destroy()
                }
            }
        }
    }

    override fun stop(location: Location, namespace: String) {
        location.world!!.players.forEach {
            val songPlayers = NoteBlockAPI.getSongPlayersByPlayer(it)

            songPlayers?.forEach { songPlayer ->
                if(
                    songPlayer is PositionSongPlayer &&
                    songPlayer.targetLocation == location &&
                    songPlayer.song.path.nameWithoutExtension == namespace
                ) {
                    songPlayer.isPlaying = false
                    songPlayer.destroy()
                }
            }
        }
    }
}