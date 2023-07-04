package me.spartacus04.jext.disc

import io.github.bananapuncher714.nbteditor.NBTEditor
import org.bukkit.*

class DiscPlayer private constructor() {
    companion object {
        fun stop(location: Location, namespace: String) {
            for (player in location.world!!.players) {
                player.stopSound(namespace, SoundCategory.RECORDS)
            }

            if(location.block.type != Material.JUKEBOX) return

            NBTEditor.set(location.block,NBTEditor.getLong(location.block, "RecordStartTick") + 72 * 20, "TickCount")
        }
    }
}