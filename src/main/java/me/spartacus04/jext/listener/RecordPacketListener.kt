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
                override fun run() {
                    player.stopSound(
                        DiscContainer.SOUND_MAP[container.material]!!.sound,
                        SoundCategory.RECORDS
                    )

                    actionBarDisplay(player, container)
                }
            }.runTaskLater(plugin, 1)
        }
    }

    fun actionBarDisplay(player: Player, container: DiscContainer) {
        player.spigot().sendMessage(
            ChatMessageType.ACTION_BAR,
            *TextComponent.fromLegacyText(
                if(container.author.trim() != "") {
                    LANG.format(player, "now-playing", true)
                        .replace("%author%", container.author)
                        .replace("%title%", container.toString())
                }
                else {
                    LANG.format(player, "now-playing-no-author", true)
                        .replace("%title%", container.toString())
                }
            )
        )
    }
}