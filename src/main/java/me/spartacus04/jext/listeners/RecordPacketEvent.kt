package me.spartacus04.jext.listeners

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketEvent
import me.spartacus04.jext.State.LANG
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.listeners.utils.JextPacketListener
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.block.Jukebox
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

internal class RecordPacketEvent : JextPacketListener(packetType = PacketType.Play.Server.WORLD_EVENT) {
    override fun onPacketSending(event: PacketEvent) {
        val packet = event.packet
        val player = event.player
        val block = packet.blockPositionModifier.values[0].toLocation(player.world).block
        val blockState = block.state
        val data = packet.integers.read(1)

        if (blockState is Jukebox && data != 0) {
            val disc = blockState.record

            val container = Disc.fromItemstack(disc) ?: return

            object : BukkitRunnable() {
                override fun run() = actionBarDisplay(player, container)
            }.runTaskLater(plugin, 1)
        }
    }

    fun actionBarDisplay(player: Player, container: Disc) {
        player.spigot().sendMessage(
            ChatMessageType.ACTION_BAR,
            *TextComponent.fromLegacyText(
                LANG.getKey(player, "now-playing", mapOf(
                    "name" to container.displayName
                ))
            )
        )
    }
}