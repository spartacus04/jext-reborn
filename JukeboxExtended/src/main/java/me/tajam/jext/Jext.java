package me.tajam.jext;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;

import me.tajam.jext.command.CommandsRegistrant;
import me.tajam.jext.config.ConfigManager;
import me.tajam.jext.listener.*;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Jext extends JavaPlugin {

  private static final SMS ENABLED_MESSAGE = new SMS().okay().t("Enabled Jukebox Extender, Do Re Mi!");
  private static final SMS DISABLED_MESSAGE = new SMS().okay().t("Disabled Jukebox Extender, Mi Re Do!");

  @Override
  public void onEnable() {
    try {
      load();
    } catch (Exception e) {
      e.printStackTrace();
      this.getServer().getPluginManager().disablePlugin(this);
    }
    ENABLED_MESSAGE.send();
  }

  @Override
  public void onDisable() {
    DISABLED_MESSAGE.send();
  }

  private void load() {

    // Load configurations
    ConfigManager.getInstance().setPlugin(this).load();

    // Packet manager
    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    protocolManager.addPacketListener(new RecordPacketListener(this, ListenerPriority.NORMAL));

    // Setup commands
    CommandsRegistrant.getInstance().registerCommands(this);

    // Register listeners
    PluginManager pluginManager = getServer().getPluginManager();
    pluginManager.registerEvents(new JukeboxEventListener(), this);
    pluginManager.registerEvents(new ResourceStatusListener(), this);
  }

}