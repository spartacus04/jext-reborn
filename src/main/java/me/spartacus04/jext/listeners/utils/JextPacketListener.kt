package me.spartacus04.jext.listeners.utils

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketListener
import com.github.retrooper.packetevents.event.PacketListenerPriority
import me.spartacus04.jext.JextState

internal open class JextPacketListener(private val minVersion: String? = null) : PacketListener, AbstractJextListener {
    override fun register() {
        if(minVersion == null || JextState.VERSION >= minVersion) {
            PacketEvents.getAPI().eventManager.registerListener(this, PacketListenerPriority.NORMAL)
        }
    }

    override fun unregister() = Unit
}