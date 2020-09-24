package me.tajam.jext;

import me.tajam.jext.command.CommandsRegistrant;
import me.tajam.jext.config.ConfigManager;
import me.tajam.jext.configuration.DiscData;
import me.tajam.jext.configuration.JextConfiguration;
import me.tajam.jext.listener.ListenersRegistrant;

import java.util.Map.Entry;

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
    JextNamespace.registerNamespace(this);

    // Load configurations
    ConfigManager.getInstance().setPlugin(this).load();

    // Setup commands
    CommandsRegistrant.getInstance().registerCommands(this);

    // Register listeners
    ListenersRegistrant.getInstance().registerListeners(this);

    //test
    new JextConfiguration().load(this);
    System.out.println(JextConfiguration.Jext.FORCE_RESOURCE_PACK);
    System.out.println(JextConfiguration.Jext.RESOURCE_PACK_DECLINE_KICK_MESSAGE);
    System.out.println(JextConfiguration.Jext.IGNORE_FAILED_DOWNLOAD);
    System.out.println(JextConfiguration.Jext.FAILED_DOWNLOAD_KICK_MESSAGE);
    System.out.println(JextConfiguration.Jext.ALLOW_MUSIC_OVERLAPPING);
    for (Entry<String, DiscData> entry : JextConfiguration.Jext.DISC.entrySet()) {
      System.out.println(entry.getKey());
      System.out.println(entry.getValue().NAMESPACE);
      System.out.println(entry.getValue().AUTHOR);
      System.out.println(entry.getValue().MODEL_DATA);
      System.out.println(entry.getValue().CREEPER_DROP);
      for (String lore: entry.getValue().LORE) {
        System.out.println(lore);
      }
    }
  }

}