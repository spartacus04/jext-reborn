package me.spartacus04.jext.disc

import io.github.bananapuncher714.nbteditor.NBTEditor
import org.bukkit.*
import org.bukkit.entity.Player

/**
 * The disc player class is used to stop a sound being played by a player.
 *
 * @constructor The constructor is private because it's a singleton
 */
class DiscPlayer private constructor() {
    companion object {
        /**
         * The function "stop" stops a sound being played by a player in a specific namespace.
         *
         * @param player The "player" parameter is an instance of the Player class. It represents the player who will stop
         * the sound.
         * @param namespace The namespace parameter is a string that represents the namespace of the sound. It is used to
         * identify the specific sound that needs to be stopped.
         */
        fun stop(player: Player, namespace: String) {
            player.stopSound(namespace, SoundCategory.RECORDS)
        }

        /**
         * The function "stop" stops the sound being played by a player.
         *
         * @param player The "player" parameter is of type "Player".
         */
        fun stop(player: Player) {
            player.stopSound(SoundCategory.RECORDS)
        }

        /**
         * The function stops a sound playing in a specific location for all players and updates the tick count for a
         * jukebox block.
         *
         * @param location The "location" parameter is the location where the stop function is being called. It represents
         * a specific block in the game world.
         * @param namespace The `namespace` parameter is a string that represents the namespace of the sound. It is used to
         * identify the specific sound that needs to be stopped.
         * @return The code is returning nothing (void).
         */
        fun stop(location: Location, namespace: String) {
            for (player in location.world!!.players) {
                player.stopSound(namespace, SoundCategory.RECORDS)
            }

            if(location.block.type != Material.JUKEBOX) return

            NBTEditor.set(location.block,NBTEditor.getLong(location.block, "RecordStartTick") + 72 * 20, "TickCount")
        }

        /**
         * The function stops playing music for players within a certain distance of a given location and updates the tick
         * count for a jukebox block.
         *
         * @param location The "location" parameter represents the location where the "stop" function is being called. It
         * is of type "Location", which is a class in the Bukkit API that represents a specific location in a Minecraft
         * world.
         * @return The code is not returning anything.
         */
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