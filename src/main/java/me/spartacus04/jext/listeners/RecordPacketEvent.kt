package me.spartacus04.jext.listeners

import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEffect
import me.spartacus04.colosseum.ColosseumPlugin
import me.spartacus04.colosseum.listeners.ColosseumPacketListener
import me.spartacus04.jext.discs.Disc
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Location
import org.bukkit.block.Jukebox
import org.bukkit.entity.Player

internal class RecordPacketEvent(val plugin: ColosseumPlugin) : ColosseumPacketListener(plugin) {
    override fun onPacketSend(event: PacketSendEvent) {
        if(event.packetType != PacketType.Play.Server.EFFECT) return

        val packet = WrapperPlayServerEffect(event)

        // https://minecraft.wiki/w/Java_Edition_protocol/Packets#World_Event

        if(packet.type != 1010) return

        val player = event.getPlayer<Player>()

        val position = packet.position.toVector3d()

        val location = Location(player.world, position.x, position.y, position.z)

        plugin.scheduler.runTaskLater(location, {
            val blockState = location.block.state

            if (blockState !is Jukebox) return@runTaskLater
            val disc = Disc.fromItemstack(blockState.record) ?: return@runTaskLater

            actionBarDisplay(player, disc)
        }, 1)

    }

    private fun actionBarDisplay(player: Player, disc: Disc) {
        player.spigot().sendMessage(
            ChatMessageType.ACTION_BAR,
            TextComponent(plugin.i18nManager!![player, "now-playing",
                "name" to disc.displayName
            ])
        )
    }
}