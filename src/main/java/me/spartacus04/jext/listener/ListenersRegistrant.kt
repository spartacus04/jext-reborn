package me.spartacus04.jext.listener

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import me.spartacus04.jext.SpigotVersion.Companion.MAJORVERSION
import me.spartacus04.jext.SpigotVersion.Companion.MINORVERSION
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryType
import org.bukkit.plugin.java.JavaPlugin

class ListenersRegistrant private constructor() {
    companion object {
        fun registerListeners(plugin: JavaPlugin) {

            // Register packet listeners
            val protocolManager = ProtocolLibrary.getProtocolManager()
            protocolManager.addPacketListener(RecordPacketListener(plugin, ListenerPriority.NORMAL))

            // Register spigot listeners
            val pluginManager = plugin.server.pluginManager
            pluginManager.registerEvents(JukeboxEventListener(plugin), plugin)
            pluginManager.registerEvents(ResourceStatusListener(plugin), plugin)
            pluginManager.registerEvents(CreeperDeathListener(), plugin)
            pluginManager.registerEvents(PlayerJoinListener(plugin), plugin)
            pluginManager.registerEvents(ChestOpenEvent(), plugin)
            pluginManager.registerEvents(DiscUpdateEvent(), plugin)

            if(MAJORVERSION >= 19) {
                pluginManager.registerEvents(PrepareCraftingEvent(), plugin)
            }

            if(MAJORVERSION == 19 && MINORVERSION >= 4 || MAJORVERSION >= 20) {
                try {
                    InventoryType.JUKEBOX
                    pluginManager.registerEvents(InventoryMoveItemEvent(), plugin)
                } catch (e: NoSuchFieldError) {
                    Bukkit.getConsoleSender().sendMessage("[§cJEXT§f] §cSpigot version is outdated and is vulnerable to a crash exploit. Please update it.§f" +
                            "\n§cIf you see this message again after updating please run BuildTools.")
                }
            }
        }
    }
}