package com.tajam.jext;

import java.util.logging.Logger;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.tajam.jext.command.ExecutorDisc;
import com.tajam.jext.config.ConfigManager;
import com.tajam.jext.listener.*;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Jext extends JavaPlugin {

  private static final String ENABLED_MESSAGE = ChatColor.GREEN + "Enabled Jukebox Extender, Do Re Mi!";
  private static final String DISABLED_MESSAGE = ChatColor.YELLOW + "Disabled Jukebox Extender, Mi Re Do!";
  
  private Logger logger = getLogger();

  @Override
  public void onEnable() {
    try {
      load();
    } catch (Exception e) {
      e.printStackTrace();
      this.getServer().getPluginManager().disablePlugin(this);
    }

    logger.info(ENABLED_MESSAGE);
  }

  @Override
  public void onDisable() {
    logger.info(DISABLED_MESSAGE);
  }

  public void load() {
    ConfigManager configManager = ConfigManager.getInstance();

    // Load configurations
    this.saveDefaultConfig();
    configManager.setPlugin(this);
    configManager.load();

    // Packet manager
    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    protocolManager.addPacketListener(new RecordPacketListener(this, ListenerPriority.NORMAL));

    // Setup commands
    getCommand("disc").setExecutor(new ExecutorDisc(logger, "jext.disc", new int[]{1}));

    // Register event
    PluginManager pluginManager = getServer().getPluginManager();
    pluginManager.registerEvents(new JukeboxEventListener(), this);
    pluginManager.registerEvents(new ResourceStatusListener(), this);
  }

}