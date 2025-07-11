package me.spartacus04.jext.listeners

import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEffect
import me.spartacus04.jext.JextState.LANG
import me.spartacus04.jext.JextState.PLUGIN
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.listeners.utils.JextPacketListener
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Location
import org.bukkit.block.Jukebox
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

internal class RecordPacketEvent : JextPacketListener() {
    override fun onPacketSend(event: PacketSendEvent) {
        if(event.packetType != PacketType.Play.Server.EFFECT) return

        val packet = WrapperPlayServerEffect(event)

        // https://minecraft.wiki/w/Java_Edition_protocol/Packets#World_Event

        if(packet.type != 1010) return

        val player = event.getPlayer<Player>()

        val position = packet.position.toVector3d()
        val block = Location(player.world, position.x, position.y, position.z).block
        val blockState = block.state

        if (blockState is Jukebox) {
            val disc = Disc.fromItemstack(blockState.record) ?: return

            object : BukkitRunnable() {
                override fun run() = actionBarDisplay(player, disc)
            }.runTaskLater(PLUGIN, 1)
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