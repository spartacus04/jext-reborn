package me.spartacus04.jext.listener

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import org.bukkit.plugin.java.JavaPlugin

class ListenersRegistrant private constructor() {
    companion object {
        fun registerListeners(plugin: JavaPlugin) {

            // Register packet listeners
            val protocolManager = ProtocolLibrary.getProtocolManager()
            protocolManager.addPacketListener(RecordPacketListener(plugin, ListenerPriority.NORMAL))

            // Register spigot listeners
            val pluginManager = plugin.server.pluginManager
            pluginManager.registerEvents(JukeboxEventListener(), plugin)
            pluginManager.registerEvents(ResourceStatusListener(plugin), plugin)
            pluginManager.registerEvents(CreeperDeathListener(), plugin)
            pluginManager.registerEvents(PlayerJoinListener(plugin), plugin)
        }
    }
}