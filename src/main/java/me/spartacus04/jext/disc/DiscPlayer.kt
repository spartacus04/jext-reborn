package me.spartacus04.jext.disc

import io.github.bananapuncher714.nbteditor.NBTEditor
import org.bukkit.*
import org.bukkit.entity.Player

class DiscPlayer private constructor() {
    companion object {
        fun stop(player: Player, namespace: String) {
            player.stopSound(namespace, SoundCategory.RECORDS)
        }

        fun stop(player: Player) {
            player.stopSound(SoundCategory.RECORDS)
        }

        fun stop(location: Location, namespace: String) {
            for (player in location.world!!.players) {
                player.stopSound(namespace, SoundCategory.RECORDS)
            }

            if(location.block.type != Material.JUKEBOX) return

            NBTEditor.set(location.block,NBTEditor.getLong(location.block, "RecordStartTick") + 72 * 20, "TickCount")
        }

        fun stop(location: Location) {
            for (player in location.world!!.players) {
                if (player.location.distance(location) <= 64) {
                    player.stopSound(SoundCategory.RECORDS)
                }
            }

            if(location.block.type != Material.JUKEBOX) return

            NBTEditor.set(location.block,NBTEditor.getLong(location.block, "RecordStartTick") + 72 * 20, "TickCount")
        }
    }
}