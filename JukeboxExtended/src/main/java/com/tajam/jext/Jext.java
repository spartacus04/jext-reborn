package com.tajam.jext;

import java.util.logging.Logger;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.tajam.jext.command.ExecutorDisk;
import com.tajam.jext.packets.RecordPacketListener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Jext extends JavaPlugin {

  Logger logger = getLogger();
  DiscBuffer buffer;

  @Override
  public void onEnable() {
    logger.info("Enabling Jukebox Extender, Do Re Mi!");

    try {
      load();
    } catch (Exception e) {
      e.printStackTrace();
      this.getServer().getPluginManager().disablePlugin(this);
    }
  }

  @Override
  public void onDisable() {
    logger.info("Disabling Jukebox Extender, Mi Re Do!");
  }

  public void load() {
    buffer = new DiscBuffer();

    // Load configurations
    saveDefaultConfig();
    FileConfiguration file = getConfig();
    buffer.loadDiscs(file);

    // Packet manager
    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    protocolManager.addPacketListener(new RecordPacketListener(this, ListenerPriority.NORMAL));
    JextAPI.getInstance().setProtocolManager(protocolManager);

    // Setup commands
    getCommand("disc").setExecutor(new ExecutorDisk(logger, buffer, "jext.disc", new int[]{1}));

    // Register event
    getServer().getPluginManager().registerEvents(new JextListener(), this);
  }

}