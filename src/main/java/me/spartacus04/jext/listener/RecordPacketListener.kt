package me.spartacus04.jext.listener

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import me.spartacus04.jext.config.ConfigData.Companion.LANG
import me.spartacus04.jext.disc.DiscContainer
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.block.Jukebox
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

internal class RecordPacketListener(plugin: Plugin?, priority: ListenerPriority?) :
    PacketAdapter(plugin, priority, PacketType.Play.Server.WORLD_EVENT) {

    /**
     * The function checks if a player is interacting with a jukebox and if a music disc is playing, then displays an
     * action bar message to the player.
     *
     * @param event The `event` parameter is of type `PacketEvent`. It represents an event that occurs when a packet is
     * being sent.
     * @return The code is returning nothing (Unit) in the `onPacketSending` function.
     */
    override fun onPacketSending(event: PacketEvent) {
        val packet = event.packet
        val position = packet.blockPositionModifier
        val blockPosition = position.values[0]
        val player = event.player
        val world = player.world
        val location =
            Location(world, blockPosition.x.toDouble(), blockPosition.y.toDouble(), blockPosition.z.toDouble())
        val block = world.getBlockAt(location)
        val blockState = block.state
        val data = packet.integers.read(1)

        if (blockState is Jukebox && data != 0) {
            val disc = blockState.record

            val container: DiscContainer = try {
                DiscContainer(disc)
            } catch (e: IllegalStateException) {
                return
            }

            object : BukkitRunnable() {
                override fun run() = actionBarDisplay(player, container)
            }.runTaskLater(plugin, 1)
        }
    }

    /**
     * The function `actionBarDisplay` sends an action bar message to a player with information about the currently playing
     * disc container, including the author and title.
     *
     * @param player The `player` parameter is an object of type `Player`, which represents a player in the game.
     * @param container The `container` parameter is of type `DiscContainer`. It represents a container that holds
     * information about a disc, such as its author and title.
     */
    fun actionBarDisplay(player: Player, container: DiscContainer) {
        player.spigot().sendMessage(
            ChatMessageType.ACTION_BAR,
            *TextComponent.fromLegacyText(
                if(container.author.trim() != "") {
                    LANG.getKey(player, "now-playing", hashMapOf(
                        "author" to container.author,
                        "title" to container.toString()
                    ))
                }
                else {
                    LANG.getKey(player, "now-playing-no-author", hashMapOf(
                        "title" to container.toString()
                    ))
                }
            )
        )
    }
}