package com.tajam.jext;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.tajam.jext.command.*;
import com.tajam.jext.config.ConfigManager;
import com.tajam.jext.listener.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Jext extends JavaPlugin {

  private static final String ENABLED_MESSAGE = ChatColor.GREEN + "Enabled Jukebox Extender, Do Re Mi!";
  private static final String DISABLED_MESSAGE = ChatColor.YELLOW + "Disabled Jukebox Extender, Mi Re Do!";

  private ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();

  @Override
  public void onEnable() {
    try {
      load();
    } catch (Exception e) {
      e.printStackTrace();
      this.getServer().getPluginManager().disablePlugin(this);
    }
    consoleSender.sendMessage(ENABLED_MESSAGE);
  }

  @Override
  public void onDisable() {
    consoleSender.sendMessage(DISABLED_MESSAGE);
  }

  public void load() {

    // Load configurations
    ConfigManager.getInstance().setPlugin(this).load();

    // Packet manager
    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    protocolManager.addPacketListener(new RecordPacketListener(this, ListenerPriority.NORMAL));

    // Setup commands
    getCommand("disc").setExecutor(new ExecutorDisc(consoleSender, "jext.disc", new int[]{1}));
    getCommand("playmusic").setExecutor(new ExecutorPlay(consoleSender, "jext.play", new int[]{1, 2, 3}));
    getCommand("stopmusic").setExecutor(new ExecutorStop(consoleSender, "jext.stop", new int[]{0, 1}));

    // Register event
    PluginManager pluginManager = getServer().getPluginManager();
    pluginManager.registerEvents(new JukeboxEventListener(), this);
    pluginManager.registerEvents(new ResourceStatusListener(), this);
  }

}