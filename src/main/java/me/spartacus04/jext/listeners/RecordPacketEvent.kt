package me.spartacus04.jext.listeners

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketEvent
import me.spartacus04.jext.JextState.LANG
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

        if (blockState is Jukebox) {
            val disc = Disc.fromItemstack(blockState.record) ?: return

            object : BukkitRunnable() {
                override fun run() = actionBarDisplay(player, disc)
            }.runTaskLater(plugin, 1)
        }
    }

    fun actionBarDisplay(player: Player, disc: Disc) {
        player.spigot().sendMessage(
            ChatMessageType.ACTION_BAR,
            TextComponent(LANG.getKey(player, "now-playing", mapOf(
                "name" to disc.displayName
            )))
        )
    }
}