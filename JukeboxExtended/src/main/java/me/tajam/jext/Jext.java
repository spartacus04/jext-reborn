package me.tajam.jext;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;

import me.tajam.jext.command.*;
import me.tajam.jext.command.tab.CompletorDisc;
import me.tajam.jext.command.tab.CompletorFloat;
import me.tajam.jext.command.tab.TabCompletorAdaptor;
import me.tajam.jext.config.ConfigManager;
import me.tajam.jext.listener.*;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Jext extends JavaPlugin {

  private static final String ENABLED_MESSAGE = "Enabled Jukebox Extender, Do Re Mi!";
  private static final String DISABLED_MESSAGE = "Disabled Jukebox Extender, Mi Re Do!";

  @Override
  public void onEnable() {
    try {
      load();
    } catch (Exception e) {
      e.printStackTrace();
      this.getServer().getPluginManager().disablePlugin(this);
    }
    Logger.success(ENABLED_MESSAGE);
  }

  @Override
  public void onDisable() {
    Logger.warning(DISABLED_MESSAGE);
  }

  private void load() {

    // Load configurations
    ConfigManager.getInstance().setPlugin(this).load();

    // Packet manager
    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    protocolManager.addPacketListener(new RecordPacketListener(this, ListenerPriority.NORMAL));

    // Setup commands
    setCommand(
      getCommand("disc"), 
      new ExecutorDisc("jext.disc", new int[]{1}), 
      new TabCompletorAdaptor()
        .addCompletor(new CompletorDisc())
    );

    setCommand(
      getCommand("playmusic"), 
      new ExecutorPlay("jext.play", new int[]{1, 2, 3}), 
      new TabCompletorAdaptor()
        .addCompletor(new CompletorDisc())
        .addCompletor(new CompletorFloat(new Float[]{ 3.0f, 1.0f, 0.5f }))
        .addCompletor(new CompletorFloat(new Float[]{ 1.0f }))
    );

    setCommand(
      getCommand("stopmusic"), 
      new ExecutorStop("jext.stop", new int[]{0, 1}), 
      new TabCompletorAdaptor()
        .addCompletor(new CompletorDisc())
    );

    // Register event
    PluginManager pluginManager = getServer().getPluginManager();
    pluginManager.registerEvents(new JukeboxEventListener(), this);
    pluginManager.registerEvents(new ResourceStatusListener(), this);
  }

  private void setCommand(PluginCommand command, ExecutorAdapter executor, TabCompletorAdaptor completor) {
    command.setExecutor(executor);
    command.setTabCompleter(completor);
  }

}