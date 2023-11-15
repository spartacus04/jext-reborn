package me.spartacus04.jext.listeners.utils

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import me.spartacus04.jext.State
import me.spartacus04.jext.State.PLUGIN

open class JextPacketListener(private val minVersion: String? = null, listenerPriority: ListenerPriority = ListenerPriority.NORMAL, packetType: PacketType) : PacketAdapter(PLUGIN, listenerPriority, packetType), AbstractJextListener {
    override fun register() {
        if(minVersion == null || State.VERSION >= minVersion) {
            ProtocolLibrary.getProtocolManager().addPacketListener(this)
        }
    }

    override fun unregister() {
        ProtocolLibrary.getProtocolManager().removePacketListener(this)
    }
}