package me.spartacus04.jext.listener

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import me.spartacus04.jext.config.ConfigData.Companion.VERSION
import me.spartacus04.jext.config.LanguageManager.Companion.VULNERABLE_MESSAGE
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
            pluginManager.registerEvents(DiscReplaceEvent(), plugin)
            pluginManager.registerEvents(JukeboxGuiListener(plugin), plugin)

            if(VERSION >= "1.19") {
                pluginManager.registerEvents(PrepareCraftingEvent(), plugin)
            }

            if(VERSION >= "1.19.4") {
                try {
                    InventoryType.JUKEBOX
                    pluginManager.registerEvents(InventoryMoveItemEvent(), plugin)
                } catch (e: NoSuchFieldError) {
                    Bukkit.getConsoleSender().sendMessage(VULNERABLE_MESSAGE)
                }
            }

            if(VERSION >= "1.20") {
                pluginManager.registerEvents(BlockBrushEvent(), plugin)
            }
        }
    }
}