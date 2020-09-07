package me.tajam.jext;

import me.tajam.jext.command.CommandsRegistrant;
import me.tajam.jext.config.ConfigManager;
import me.tajam.jext.listener.ListenersRegistrant;
import me.tajam.jext.namespace.JextNamespace;

import org.bukkit.plugin.java.JavaPlugin;

public class Jext extends JavaPlugin {

  private static final Log ENABLED_MESSAGE = new Log().okay().t("Enabled Jukebox Extender, Do Re Mi!");
  private static final Log DISABLED_MESSAGE = new Log().warn().t("Disabled Jukebox Extender, Mi Re Do!");

  @Override
  public void onEnable() {
    try {
      new SpigotVersion(this);
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

    // Register namespaces
    JextNamespace.getInstance().registerNamespace(this);

    // Load configurations
    ConfigManager.getInstance().setPlugin(this).load();

    // Setup commands
    CommandsRegistrant.getInstance().registerCommands(this);

    // Register listeners
    ListenersRegistrant.getInstance().registerListeners(this);
  }

}