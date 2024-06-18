package me.spartacus04.jext.listeners.utils

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import me.spartacus04.jext.JextState
import me.spartacus04.jext.JextState.PLUGIN

internal open class JextPacketListener(private val minVersion: String? = null, listenerPriority: ListenerPriority = ListenerPriority.NORMAL, packetType: PacketType) : PacketAdapter(PLUGIN, listenerPriority, packetType), AbstractJextListener {
    override fun register() {
        if(minVersion == null || JextState.VERSION >= minVersion) {
            ProtocolLibrary.getProtocolManager().addPacketListener(this)
        }
    }

    override fun unregister() {
        ProtocolLibrary.getProtocolManager().removePacketListener(this)
    }
}