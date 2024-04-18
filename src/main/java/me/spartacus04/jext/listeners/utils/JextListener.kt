package me.spartacus04.jext.listeners.utils

import me.spartacus04.jext.JextState.PLUGIN
import me.spartacus04.jext.JextState.VERSION
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

internal open class JextListener(private val minVersion: String? = null) : Listener, AbstractJextListener {
    override fun register() {
        if(minVersion == null || VERSION >= minVersion) {
            PLUGIN.server.pluginManager.registerEvents(this, PLUGIN)
        }
    }

    override fun unregister() {
        HandlerList.unregisterAll(this)
    }
}
