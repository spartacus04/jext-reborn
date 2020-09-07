package me.tajam.jext.listener;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenersRegistrant {

  private static ListenersRegistrant instance = null;
  public static ListenersRegistrant getInstance() {
    if (instance == null) {
      instance = new ListenersRegistrant();
    }
    return instance;
  }

  private ListenersRegistrant() {}

  public void registerListeners(JavaPlugin plugin) {

    // Register packet listeners
    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    protocolManager.addPacketListener(new RecordPacketListener(plugin, ListenerPriority.NORMAL));
    
    // Register spigot listeners
    PluginManager pluginManager = plugin.getServer().getPluginManager();
    pluginManager.registerEvents(new JukeboxEventListener(), plugin);
    pluginManager.registerEvents(new ResourceStatusListener(plugin), plugin);
    pluginManager.registerEvents(new CreeperDeathListener(), plugin);
  }

}